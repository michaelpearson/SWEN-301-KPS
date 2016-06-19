package kps.xml.objects.abstracts;

import kps.xml.objects.Simulation;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Defines a business event which has a location.
 */
@XmlTransient public abstract class BusinessEventWithLocation extends BusinessEvent {
    @NotNull @XmlElement private String to = "Unknown";
    @NotNull @XmlElement private String from = "Unknown";

    //Todo: Remove this because its bad :(
    @XmlTransient private static final String DOMESTIC_REFERENCE = "new zealand";

    public BusinessEventWithLocation() {}

    public BusinessEventWithLocation(Simulation s) {
        super(s);
    }

    public void setTo(@NotNull String to) {
        if (getSimulation() != null && !getSimulation().isLocationValid(to)) {
            throw new RuntimeException("Invalid location"); //Todo: this should be handled nicely.
        }
        this.to = to;
    }

    public void setFrom(@NotNull String from) {
        if(getSimulation() != null && !getSimulation().isLocationValid(from)) {
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

    public boolean isDomestic() {
        return getTo().toLowerCase().equals(DOMESTIC_REFERENCE) && getFrom().toLowerCase().equals(DOMESTIC_REFERENCE);
    }
}
