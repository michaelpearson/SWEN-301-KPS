import xml.SimulationXML;
import xml.exceptions.XMLException;
import xml.objects.Simulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws XMLException, FileNotFoundException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream(new File("Test.xml")));
        SimulationXML.writeSimulation(s, System.out);

        //new ApplicationWindow();
    }
}
