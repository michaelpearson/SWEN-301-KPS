package xml;

import org.junit.Assert;
import org.junit.Test;
import xml.exceptions.XMLException;
import xml.objects.Simulation;

import java.io.*;

public class XML {

    @Test
    public void testReadingAndWritingSimulationXML() throws IOException, XMLException {
        InputStream xml = new FileInputStream(new File("Tests/data/Test.xml"));
        Simulation s = SimulationReader.readSimulationFromFile(xml);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream sw = new PrintStream(byteArrayOutputStream);
        SimulationReader.writeSimulation(s, sw);
        xml = new FileInputStream(new File("Tests/data/Test.xml"));
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
}
