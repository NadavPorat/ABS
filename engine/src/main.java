import engine.ABSEngine;
import engine.Engine;

public class main {

    public static void main(String[] args) {
        Engine e = new ABSEngine();

        //TODO add input from user
        e.buildFromXML("/Users/nporat/repositories/java_course/ABS/engine/src/engine/resources/ex1-big.xml");
        ((ABSEngine) e).printABS();

    }
}
