package com.company;
import com.company.loaderPackage.*;

public class Main {

    public static void main(String[] args) {
        String fileName;
        try {
            fileName=args[0];
        } catch (ArrayIndexOutOfBoundsException ex) {
            fileName="goldenStateWarriors.properties";
        }
        Team firstBasketballTeam = Team.getInstance();
        firstBasketballTeam.setPropertyNameFile(fileName);
        System.out.println("информация о команде " + firstBasketballTeam.toString());
        Team secondBasketballTeam = Team.getInstance();
        secondBasketballTeam.refresh();
        System.out.println(firstBasketballTeam.toString());
    }
}
