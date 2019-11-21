package com.company.loaderPackage;

import java.util.Calendar;

public class Stadium {
    private Integer capacity;
    private String name;

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "\'capacity\':" + capacity +
                ", \'name\':'" + name + '\'' +
                '}';
    }

}

