package kps.xml.objects;

import kps.xml.SimulationXML;
import kps.xml.exceptions.XMLException;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.Priority;
import kps.xml.objects.enums.TransportType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

@XmlAccessorType(XmlAccessType.FIELD)
public class Mail extends ModelObject {
    @XmlElement(name="day") private DayOfWeek day;
    @XmlElement private int to;
    @XmlElement private int from;
    @XmlElement(name="weight") private int weight;
    @XmlElement(name="volume") private int volume;
    @XmlElement(name="priority") private Priority priority;

    public Mail(Simulation s) {
        super(s);
    }

    public Mail() {
    }

    public void setTo(String to) {
        Location location = getSimulation().getLocationByName(to);
        if(location == null) {
            location = new Location(simulation);
            simulation.getLocations().add(location);
        }
        this.to = location.getId();
    }

    public void setFrom(String from) {
        Location location = getSimulation().getLocationByName(from);
        if(location == null) {
            location = new Location(simulation);
            simulation.getLocations().add(location);
        }
        this.from = location.getId();
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Location getTo() {
        return getSimulation().getLocationById(to);
    }

    public Location getFrom() {
        return getSimulation().getLocationById(from);
    }

    public Priority getPriority() { return priority; }

    public int getWeight() {
        return weight;
    }

    public int getVolume() {
        return volume;
    }

    public DayOfWeek getDay() {
        return day;
    }
}
