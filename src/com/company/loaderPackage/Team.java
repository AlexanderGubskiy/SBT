package com.company.loaderPackage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Team {
    private RipperOfClass ripper;
    private static volatile Team instance;
    private static final Logger logger = Logger.getLogger(Team.class.getName());
    private String propertyNameFile;

    @Marker(name = "Team.Integer")
    private Integer Integer;
    @Marker(name = "Team.Double")
    private Double Double;
    @Marker(name = "Team.name")
    private String name;
    @Marker(name = "Team.owner")
    private Owner owner;
    @Marker(name = "Team.budget")
    private Integer budget;
    @Marker(name = "Team.stadium")
    private Stadium stadium;
    @Marker(name = "Team.secStadium", value = "sssd")
    private Stadium secStadium;
    @Marker(name = "Team.league")
    private League league;
    @Marker(name = "Team.secLeague")
    private League secLeague;

    public void setPropertyNameFile(String propertyNameFile) {
        this.propertyNameFile = propertyNameFile;
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

    public double getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * Подгружает параметры из файла, указанного в файле конфигураций
     */
    private void loadProperties() {
        try {
            //определение файла для подкачки параметров
            FileInputStream configFile = new FileInputStream("config.properties");
            Properties prop = new Properties();
            prop.load(configFile);
            propertyNameFile = prop.getProperty("propertyForLoad");
            //выгрузка параметров
            FileInputStream in = new FileInputStream(propertyNameFile);
            prop.load(in);
            ripper = new RipperOfClass(this, prop);
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
        if (instance != null) {
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
                "\t\'Integer\':\'" + Integer + "\',\n" +
                "\t\'Double\':\'" + Double + "\',\n" +
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
