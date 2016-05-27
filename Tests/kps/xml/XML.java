package kps.xml;

import kps.xml.exceptions.XMLException;
import kps.xml.objects.Simulation;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class XML {

    @Test public void checkThatTheFirstLocationInTheTestXmlDataIsWellington() throws FileNotFoundException, XMLException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream(new File("test_data/Test.xml")));
        Assert.assertEquals(s.getLocations().get(0).getName(), "Wellington");
    }
}
