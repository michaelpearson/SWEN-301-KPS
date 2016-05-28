package kps.xml.objects;

import com.sun.istack.internal.Nullable;
import kps.gui.windows.MailDialog;
import kps.xml.objects.abstracts.BusinessEvent;
import kps.xml.objects.abstracts.ModelObject;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.awt.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Mail extends BusinessEvent {
    @XmlElement(name="day") private DayOfWeek day;
    @XmlElement private int to;
    @XmlElement private int from;
    @XmlElement(name="weight") private int weight;
    @XmlElement(name="volume") private int volume;
    @XmlElement(name="priority") private Priority priority;

    public Mail(Simulation s) {
        super(s);
    }

    public Mail() {}

    @Override public String getEventType() {
        return "Mail delivery";
    }

    @Override public void edit(Frame owner) {
        new MailDialog(owner, getSimulation(), this);
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

    @Nullable public Location getTo() {
        return getSimulation().getLocationById(to);
    }

    @Nullable public Location getFrom() {
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
