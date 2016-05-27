package kps.xml;

import kps.xml.exceptions.XMLException;
import kps.xml.objects.Simulation;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class XML {

    @Test
    public void testReadingAndWritingSimulationXML() throws IOException, XMLException {
        InputStream xml = new FileInputStream(new File("test_data/Test.xml"));
        Simulation s = SimulationXML.readSimulationFromFile(xml);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream sw = new PrintStream(byteArrayOutputStream);
        SimulationXML.writeSimulation(s, sw);
        xml = new FileInputStream(new File("test_data/Test.xml"));
        byte xmlData[] = byteArrayOutputStream.toByteArray();
        int i = 0;
        while(true) {
            if(i == xmlData.length) {
                return;
            }
            int c = xml.read();
            if(c < 0) {
                return;
            }
            if(c != xmlData[i++]) {
                Assert.fail();
                return;
            }
        }
    }

    @Test public void checkThatTheFirstLocationInTheTestXmlDataIsWellington() throws FileNotFoundException, XMLException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream(new File("test_data/Test.xml")));
        Assert.assertEquals(s.getLocations().get(0).getName(), "Wellington");
    }
}
