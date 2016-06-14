package kps.xml.objects.abstracts;

import kps.xml.objects.Simulation;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient public abstract class BusinessEventWithLocation extends BusinessEvent {
    @NotNull @XmlElement private String to = "Unknown";
    @NotNull @XmlElement private String from = "Unknown";

    public BusinessEventWithLocation() {}

    public BusinessEventWithLocation(Simulation s) {
        super(s);
    }

    public void setTo(@NotNull String to) {
        if (!getSimulation().isLocationValid(to)) {
            throw new RuntimeException("Invalid location"); //Todo: this should be handled nicely.
        }
        this.to = to;
    }

    public void setFrom(@NotNull String from) {
        if(!getSimulation().isLocationValid(from)) {
            throw new RuntimeException("Invalid location"); //Todo: this should be handled nicely.
        }
        this.from = from;
    }

    @NotNull @Override public String getTo() {
        return to;
    }

    @NotNull @Override public String getFrom() {
        return from;
    }

    @Override public String toString() {
        return "{ to=" + getTo() + ", from=" + getFrom() + '}';
    }
}
