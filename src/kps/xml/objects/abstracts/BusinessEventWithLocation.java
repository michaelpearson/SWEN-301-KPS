package kps.xml.objects.abstracts;

import kps.xml.objects.Location;
import kps.xml.objects.Simulation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class BusinessEventWithLocation extends BusinessEvent {
    @XmlElement private int to;
    @XmlElement private int from;

    public BusinessEventWithLocation(Simulation s) {
        super(s);
    }

    public BusinessEventWithLocation() {}

    public void setTo(@NotNull String to) {
        Location location = getSimulation().getLocationByName(to);
        if(location == null) {
            throw new RuntimeException("Location cannot be null");
        }
        this.to = location.getId();
    }

    public void setTo(@NotNull Location to) {
        this.to = to.getId();
    }

    public void setFrom(@NotNull String from) {
        Location location = getSimulation().getLocationByName(from);
        if(location == null) {
            throw new RuntimeException("Location cannot be null");
        }
        this.from = location.getId();
    }

    public void setFrom(@NotNull Location from) {
        this.from = from.getId();
    }

    @Nullable public Location getTo() {
        return getSimulation().getLocationById(to);
    }

    @Nullable public Location getFrom() {
        return getSimulation().getLocationById(from);
    }
}
