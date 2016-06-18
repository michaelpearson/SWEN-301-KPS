package kps.xml;

import kps.xml.exceptions.XMLException;
import kps.xml.objects.Simulation;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class XML {

    @Test public void checkThatTheFirstLocationInTheTestXmlDataIsWellington() throws FileNotFoundException, XMLException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream(new File("test_data/Test.xml")));
        Assert.assertTrue(s.getLocations().contains("Wellington"));
    }
}
