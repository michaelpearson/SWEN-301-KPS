package kps;

import kps.gui.windows.Login;
import kps.xml.SimulationXML;
import kps.xml.exceptions.XMLException;
import kps.xml.objects.Simulation;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    static {
        try {
            UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        } catch (Exception ignore) {}
    }
    public static void main(String[] args) throws XMLException, FileNotFoundException {
        if(args.length < 1) {
            System.err.println("You must provide the simulation log file as the first argument");
            System.exit(1);
        }
        Simulation simulation;
        String fileName = args[0];
        try {
            simulation = SimulationXML.readSimulationFromFile(new FileInputStream(fileName));
        } catch (Exception e) {
            e.printStackTrace();
            simulation = new Simulation();
        }
        new Login(simulation, fileName);
    }
}
