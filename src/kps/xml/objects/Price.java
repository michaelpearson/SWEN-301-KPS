package kps.xml.objects;

import kps.xml.objects.abstracts.BusinessEvent;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.awt.*;

@XmlAccessorType(XmlAccessType.FIELD) public class Price extends BusinessEvent {

    @XmlElement private int to;
    @XmlElement private int from;
    @XmlElement private Priority priority;
    @XmlElement(name="weightcost") private int weightCost;
    @XmlElement(name="volumecost") private int volumeCost;

    public Price(Simulation s) {
        super(s);
    }

    public Price() {}

    @Override public String getEventType() {
        return "Price Change";
    }

    @Override public void edit(Frame owner) {
        throw new RuntimeException("Cannot edit price object yet");
    }

    @Nullable
    public Location getTo() {
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public int getWeightCost() {
        return weightCost;
    }

    public void setWeightCost(int weightCost) {
        this.weightCost = weightCost;
    }

    public int getVolumeCost() {
        return volumeCost;
    }

    public void setVolumeCost(int volumeCost) {
        this.volumeCost = volumeCost;
    }

    @Override public double getExpenditure() {
        return 0;
    }

    @Override public double getRevenue() {
        return 0;
    }
}
