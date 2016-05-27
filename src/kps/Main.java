package kps;

import kps.gui.ApplicationWindow;
import kps.xml.SimulationXML;
import kps.xml.exceptions.XMLException;
import kps.xml.objects.Simulation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws XMLException, FileNotFoundException {
        if(args.length < 1) {
            System.err.println("You must provide the simulation log file as the first argument");
            System.exit(1);
        }
        Simulation simulation = SimulationXML.readSimulationFromFile(new FileInputStream(args[0]));
        new ApplicationWindow(simulation);
    }
}
