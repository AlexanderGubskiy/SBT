package com.company.loaderPackage;

import java.lang.reflect.Field;

public class Owner {
    private Integer finance;
    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        lastName = lastName;
    }

    public Integer getFinance() {
        return finance;
    }

    public void setFinance(Integer finance) {
        this.finance = finance;
    }

    @Override
    public String toString() {
        return "{" +
                "\'finance\':" + finance +
                ", \'firstName\':'" + firstName + '\'' +
                ", \'LastName\':'" + lastName + '\'' +
                '}';
    }
}
