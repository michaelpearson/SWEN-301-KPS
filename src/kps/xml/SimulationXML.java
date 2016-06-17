package kps.xml;

import kps.xml.exceptions.XMLException;
import kps.xml.objects.CalculatedRoute;
import kps.xml.objects.Mail;
import kps.xml.objects.Route;
import kps.xml.objects.Simulation;
import kps.xml.objects.abstracts.ModelObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class SimulationXML extends StreamReaderDelegate {

    public static kps.xml.objects.Simulation readSimulationFromFile(InputStream xml) throws FileNotFoundException, XMLException {
        try {
            JAXBContext jc = JAXBContext.newInstance(Simulation.class);
            XMLInputFactory xif = XMLInputFactory.newInstance();
            XMLStreamReader xsr = xif.createXMLStreamReader(xml);
            xsr = new SimulationXML(xsr);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            final List<ModelObject> modelObjects = new ArrayList<>();
            unmarshaller.setListener(new Unmarshaller.Listener() {
                @Override
                public void afterUnmarshal(Object target, Object parent) {
                    if(!(target instanceof Simulation) && ModelObject.class.isAssignableFrom(target.getClass())) {
                        modelObjects.add((ModelObject)target);
                    }
                }
            });
            Simulation simulation = (Simulation) unmarshaller.unmarshal(xsr);
            xml.close();
            for(ModelObject mo : modelObjects) {
                mo.setSimulation(simulation);
            }
            return simulation;
        } catch(JAXBException | XMLStreamException | IOException e) {
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

    private SimulationXML(XMLStreamReader xsr) {
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
