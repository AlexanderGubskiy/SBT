package com.company.loaderPackage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class Team {
    private RipperOfClass ripper;
    private Map<String, String> map;
    private static volatile Team instance;
    private static final Logger logger = Logger.getLogger(Team.class.getName());
    private String propertyNameFile = "goldenStateWarriors.properties";

    @Marker(name = "Team.ch", value = "g")
    private char ch;
    @Marker(name = "character")
    private Character character;
    @Marker(name = "Team.Integer", value = "123")
    private Integer Integer;
    @Marker(name = "Team.Double", value = "11.7")
    private Double Double;
    @Marker(name = "Boolean", value = "true")
    private Boolean Boolean;
    @Marker(name = "Team.name")
    private String name;
    @Marker(name = "Team.owner")
    private Owner owner;
    @Marker(name = "Team.budget")
    private long budget;
    @Marker(name = "Team.stadium")
    private Stadium stadium;
    @Marker(name = "Team.secstadium")
    private Stadium secStadium;
    @Marker(name = "Team.league")
    private League league;
    @Marker(name = "Team.secleague")
    private League secLeague;

    public void setPropertyNameFile(String propertyNameFile) {
        this.propertyNameFile = propertyNameFile;
        loadProperties();

    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * Подгружает параметры из файла, указанного в переменной propertyNameFile
     */
    private void loadProperties() {
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream(propertyNameFile);
            properties.load(in);
            map = new HashMap<String, String>();
            for (final String key : properties.stringPropertyNames())
                map.put(key, properties.getProperty(key));
            ripper = new RipperOfClass(this, map);
        } catch (IOException e) {
            logger.info("При загрузке полей из файла конфигураций произошла ошибка");
            e.printStackTrace();
        }
    }

    private Team() {
        //Подкчака полей при создании объекта
        loadProperties();
    }

    /**
     * Singleton
     */
    public static Team getInstance() {
        Team res = instance;
        if (res != null) {
            return instance;
        } else {
            synchronized (Team.class) {
                if (instance == null) {
                    instance = new Team();
                }
                return instance;
            }
        }
    }

    /**
     * Метод, осуществляющий обновления данных
     */
    public synchronized void refresh() {
        ripper.changeProperty();
    }

    @Override
    public String toString() {
        return name + " {\n" +
                "\t\'character\':\'" + character + "\',\n" +
                "\t\'ch\':\'" + ch + "\',\n" +
                "\t\'Integer\':\'" + Integer + "\',\n" +
                "\t\'Double\':\'" + Double + "\',\n" +
                "\t\'Boolean\':\'" + Boolean + "\',\n" +
                "\t\'name\':\'" + name + "\'," +
                " \'budget\':" + budget + "," +
                " \'league\':" + "\'" + league + "\'" +
                " \'secondLeague\':" + "\'" + secLeague + "\'" +
                ",\n\t\'owner\':" + owner +
                ",\n\t\'stadium\':" + stadium +
                ",\n\t\'secondStadium\':" + secStadium +
                "\n}";
    }
}
