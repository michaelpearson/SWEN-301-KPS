import xml.SimulationReader;
import xml.exceptions.XMLException;
import xml.objects.Simulation;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws XMLException, FileNotFoundException {

        Simulation s = SimulationReader.readSimulationFromFile("Test.xml");

        SimulationReader.writeSimulation(s, System.out);

        //new ApplicationWindow();
    }
}
