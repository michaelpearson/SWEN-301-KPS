package xml;

import xml.exceptions.XMLException;
import xml.objects.Simulation;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;


public class SimulationReader extends StreamReaderDelegate {
    public static xml.objects.Simulation readSimulationFromFile(String xmlPath) throws FileNotFoundException, XMLException {
        try {
            JAXBContext jc = JAXBContext.newInstance(Simulation.class);
            XMLInputFactory xif = XMLInputFactory.newInstance();
            XMLStreamReader xsr = xif.createXMLStreamReader(new FileInputStream(xmlPath));
            xsr = new SimulationReader(xsr);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            return (Simulation) unmarshaller.unmarshal(xsr);
        } catch(JAXBException | XMLStreamException e) {
            throw new XMLException(e);
        }
    }

    public static void writeSimulation(Simulation simulation, PrintStream output) throws XMLException {
        try {
            JAXBContext jc = JAXBContext.newInstance(Simulation.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(simulation, output);
        } catch(JAXBException e) {
            throw new XMLException(e);
        }
    }

    private SimulationReader(XMLStreamReader xsr) {
        super(xsr);
    }
    @Override
    public String getAttributeLocalName(int index) {
        return super.getAttributeLocalName(index).toLowerCase();
    }
    @Override
    public String getLocalName() {
        return super.getLocalName().toLowerCase();
    }
}
