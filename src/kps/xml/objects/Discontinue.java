package kps.xml.objects;

import com.sun.istack.internal.Nullable;
import kps.xml.objects.abstracts.BusinessEvent;
import kps.xml.objects.abstracts.ModelObject;
import kps.xml.objects.enums.TransportType;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.awt.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Discontinue extends BusinessEvent {
    @XmlElement private String company;
    @XmlElement private int to;
    @XmlElement private int from;
    @XmlElement private TransportType transportType;

    public Discontinue(Simulation s) {
        super(s);
    }

    public Discontinue() {}

    @Override public String getEventType() {
        return "Route discontinued";
    }

    @Override public void edit(Frame owner) {
        throw new NotImplementedException();
    }


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Nullable public Location getTo() {
        return getSimulation().getLocationById(to);
    }

    public void setTo(int to) {
        this.to = to;
    }

    @Nullable @Override public Location getFrom() {
        return getSimulation().getLocationById(from);
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }
}
