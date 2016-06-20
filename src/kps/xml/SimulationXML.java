package kps.xml;

import kps.xml.exceptions.XMLException;
import kps.xml.objects.Simulation;
import kps.xml.objects.abstracts.ModelObject;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simplification of the JAXB api. This simplification takes care of loading the saving the simulation object.
 */
public class SimulationXML extends StreamReaderDelegate {

    /**
     * Read the simulation from an xml file
     * @param xml the input stream of the xml source
     * @return a simulation object which represents the state of the system
     * @throws XMLException if there was an error reading the stream, error in the xml file or some other xml exception.
     */
    @NotNull public static kps.xml.objects.Simulation readSimulationFromFile(@NotNull InputStream xml) throws XMLException {
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

    /**
     * Write the simulation to an xml {@link PrintStream}
     * @param simulation the simulation object to write to a {@link PrintStream}
     * @param output the output {@link PrintStream}
     * @throws XMLException
     */
    public static void writeSimulation(@NotNull Simulation simulation, @NotNull PrintStream output) throws XMLException {
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

    @Override public String getAttributeLocalName(int index) {
        return super.getAttributeLocalName(index).toLowerCase();
    }

    @Override public String getLocalName() {
        return super.getLocalName().toLowerCase();
    }
}
