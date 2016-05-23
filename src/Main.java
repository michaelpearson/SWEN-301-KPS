import xml.SimulationReader;
import xml.exceptions.XMLException;
import xml.objects.Simulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws XMLException, FileNotFoundException {

        Simulation s = SimulationReader.readSimulationFromFile(new FileInputStream(new File("Test.xml")));

        SimulationReader.writeSimulation(s, System.out);

        //new ApplicationWindow();
    }
}
