package com.company.loaderPackage;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Field;
import java.util.Properties;
import java.util.logging.Logger;


/**
 * Класс, который собирает помеченные поля и обновляет их
 */
public class RipperOfClass {
    private Team team;
    private Properties properties;
    private static final Logger logger = Logger.getLogger(RipperOfClass.class.getName());

    public RipperOfClass(Team team, Properties properties) {
        this.team = team;
        this.properties = properties;
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
        for (Field field : team.getClass().getDeclaredFields()) {
            Marker annotation = field.getAnnotation(Marker.class);
            if (annotation != null) {
                String value = properties.getProperty(annotation.name());
                String[] annotationValue = annotation.value();
                /*Если в файле *.properties не найдено значение по ключу*/
                if (value == null) {
                    /*Значение по умолчанию пустое*/
                    if (annotationValue.length == 0) {
                        setProperty(team, field, null);
                    }
                    /*Значение по умолчанию не пустое*/
                    else if (annotationValue.length > 0) {
                        setProperty(team, field, annotationValue[0]);
                    }
                }
                /*Значение из файла *.properties*/
                else {
                    setProperty(team, field, value);
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
        switch (type) {
            case "String":
            case "Double":
            case "Integer": {
                Object variable = null;
                try {
                    field.setAccessible(true);
                    if (type.equals("Double")) {
                        variable = Double.parseDouble(value);
                    } else if (type.equals("Integer")) {
                        variable = Integer.parseInt(value);
                    } else {
                        variable = value;
                    }
                } catch (NullPointerException e) {
                    logger.info("Для поля " + field.getName() + " передано значение null");
                } catch (NumberFormatException e) {
                    logger.info("Поле " + field.getName() + " имеет некорректное значение в файле *.properties");
                } finally {
                    try {
                        field.set(team, variable);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(false);
                    break;
                }
            }
            case "League": {
                boolean flag = containsAtLeague(value);
                try {
                    field.setAccessible(true);
                    if (flag) {
                        field.set(team, Enum.valueOf((Class<Enum>) field.getType(), value));
                    } else {
                        logger.info("Значение " + " в перечеслении не существует");
                        field.set(team, null);
                    }
                } catch (IllegalAccessException e) {
                    logger.info("Во время инициализации перечеслителя произошла ошибка");
                    e.printStackTrace();
                } finally {
                    field.setAccessible(false);
                }
                break;
            }
            default: {
                Class clazz = field.getType();
                Object objectInstance = null;
                Gson gson = new Gson();
                try {
                    field.setAccessible(true);
                    objectInstance = clazz.newInstance();
                    objectInstance = gson.fromJson(value, objectInstance.getClass());
                    field.set(object, objectInstance);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException e) {
                    logger.info("Во время десериализации объекта произошла ошибка");
                } finally {
                    field.setAccessible(false);
                }
                break;
            }
        }
    }
}
