package com.company;
import com.company.loaderPackage.*;

public class Main {
    public static void main(String[] args) {
        Team firstBasketballTeam = Team.getInstance();
        System.out.println("информация о команде " + firstBasketballTeam.toString());
        Team secondBasketballTeam = Team.getInstance();
        secondBasketballTeam.refresh();
        System.out.println(firstBasketballTeam.toString());
    }
}
