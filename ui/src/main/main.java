package main;

import app.ABSapp;
import engine.ABSEngine;

public class main {

    public static void main(String[] args) {

        //e.buildFromXML("/Users/nporat/repositories/java_course/ABS/engine/src/resources/ex1-big.xml");
        ABSapp app = new ABSapp();
        app.init(new ABSEngine());
        app.run();
    }
}

