package com.company.loaderPackage;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Класс, который собирает помеченные поля и обновляет их
 */
public class RipperOfClass {
    private Object obj;
    private Map<String, String> dictionary;
    private static final Logger logger = Logger.getLogger(RipperOfClass.class.getName());

    public RipperOfClass(Object obj, Map<String, String> dictionary) {
        this.obj = obj;
        this.dictionary = dictionary;
    }

    /**
     * Метод проверяет относится ли строка к примитивному типу
     * @param str строка проверки
     * @return true/false
     */
    private boolean isPrimitive(String str) {
        boolean flag = false;
        switch (str) {
            case "byte":
            case "short":
            case "char":
            case "int":
            case "long":
            case "float":
            case "double":
            case "boolean":
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        return flag;
    }

    /**
     * Метод проверяет относится ли строка к перечеселнию (Лиги)
     *
     * @param str строка проверки
     * @return true/false
     */
    private boolean containsAtLeague(String str) {
        for (League l : League.values()) {
            if (l.name().equals(str)) {
                return true;
            }
        }

        return false;
    }

    public void changeProperty() {

        Field[] fields = obj.getClass().getDeclaredFields();
        //перебор полей объекта данного класса
        for (Field field : fields) {
            String nameOfProperty = field.getName();
            if (nameOfProperty != "map" && nameOfProperty != "instance") {
                Marker annotation = field.getAnnotation(Marker.class);
                if (annotation != null) {
                    String value = dictionary.get(annotation.name());
                    String annotationValue = annotation.value();
                    boolean primitive = isPrimitive(field.getType().getSimpleName());
                    // Если в файле (prop) нет имени данного поля и значение поля value стоит не по умолчанию
                    if (value == null && !annotationValue.equals("defaultStringValue")) {
                        logger.info("Для поля " + field.getName() + " не найдено соответствие в файле prop");
                        value = annotationValue;
                    }
                    // Игнорируем все примитивные типы с значением по умолчанию
                    // (т.е инициализируются автоматически (byte=short=0 и т.д)
                    if (!(primitive && value.equals("defaultStringValue"))) {
                        setProperty(obj, field, value);
                    } else {
                        logger.info("Поле" + field.getName() + " будет проинициализировано по умолчанию");
                    }
                }
            }
        }

    }


    /**
     * Метод, осуществляющий установку значения в указанное поле
     *
     * @param object объект, по отношению к которому будут проводиться изменения его поля
     * @param field  поле
     * @param value  значение
     */
    private void setProperty(Object object, Field field, String value) {

        String type = field.getType().getSimpleName();
        String nameValue = field.getName();
        switch (type) {
            //Обработка для пользовательских классов
            case "Stadium":
            case "Owner": {
                if (value == null) {
                    logger.info("При работе с объектом класса " + type + " произошла ошибка");
                    return;
                }
                Class clazz = field.getType();
                Object objectInstance = null;
                try {
                    objectInstance = clazz.newInstance();
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field fieldOfObject : fields) {
                        JsonParser parser = new JsonParser();
                        JsonObject json = (JsonObject) parser.parse(value);
                        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
                        for (Map.Entry<String, JsonElement> entry : entrySet) {
                            String key = entry.getKey();
                            if (fieldOfObject.getName().equals(key)) {
                                String val = entry.getValue().toString();
                                setProperty(objectInstance, fieldOfObject, val);
                            }
                        }
                    }
                    field.setAccessible(true);
                    field.set(obj, objectInstance);
                } catch (InstantiationException e) {
                    logger.info("При работе с объектом класса " + clazz.getSimpleName() + " произошла ошибка");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    logger.info("Во время инициализации информации об объекте" + clazz.getSimpleName() + " произошла ошибка");
                    e.printStackTrace();
                }
            }
            break;
            //Обработка для перечеслителя
            case "League":
                boolean flag = containsAtLeague(value);
                try {
                    field.setAccessible(true);
                    if (flag) {
                        field.set(obj, Enum.valueOf((Class<Enum>) field.getType(), value));
                    } else {
                        logger.info("Значение " + " в перечеслении не существует");
                        field.set(obj, null);
                    }
                } catch (IllegalAccessException e) {
                    logger.info("Во время инициализации перечеслителя произошла ошибка");
                    e.printStackTrace();
                }
                break;
            //обработчик для базовых типов, классов оболочки, String (int-Integer и т.д.)
            default:
                String str = "{" + nameValue + ":" + value + "}";
                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(str);
                Gson gson = new Gson();
                Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
                for (Map.Entry<String, JsonElement> entry : entrySet) {
                    String key = entry.getKey();
                    field.setAccessible(true);
                    Type genType = field.getGenericType();
                    try {
                        if (entry.getValue().toString().equals("null"))
                            logger.info("Поле " + field.getName() + " проинициализировано null'ом");
                        field.set(object,
                                gson.fromJson(entry.getValue(), genType));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
