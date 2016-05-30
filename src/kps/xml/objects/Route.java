package kps.xml.objects;

import kps.gui.windows.dialogs.RouteDialog;
import kps.xml.objects.abstracts.BusinessEventWithLocation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.TransportType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.awt.*;

/**
 * The route object represents a route on a given transport provider.
 */
@XmlAccessorType(XmlAccessType.NONE) public class Route extends BusinessEventWithLocation {

    @XmlElement private String company;
    @XmlElement(name="type") private TransportType transportType;
    @XmlElement(name="weightcost") private int weightCost;
    @XmlElement(name="volumecost") private int volumeCost;
    @XmlElement private int maxWeight;
    @XmlElement private int maxVolume;
    @XmlElement private int duration;
    @XmlElement private int frequency;
    @XmlElement(name="day") private DayOfWeek day;
    @XmlElement private boolean discontinued;

    Route() {}

    public Route(Simulation s) {
        super(s);
    }

    @Override public String getEventType() {
        if(isDiscontinued()) {
            return "Route discontinuation";
        } else {
            return "Route update";
        }
    }

    @Override public void edit(Frame owner) {
        new RouteDialog(owner, getSimulation(), this);
    }

    public void update(Frame owner) {
        new RouteDialog(owner, getSimulation(), true, this.copy());
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public void setWeightCost(int weightCost) {
        this.weightCost = weightCost;
    }

    public void setVolumeCost(int volumeCost) {
        this.volumeCost = volumeCost;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void setMaxVolume(int maxVolume) {
        this.maxVolume = maxVolume;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public String getCompany() {
        return company;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public int getWeightCost() {
        return weightCost;
    }

    public int getVolumeCost() {
        return volumeCost;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    public int getDuration() {
        return duration;
    }

    public int getFrequency() {
        return frequency;
    }

    public DayOfWeek getDay() {
        return day;
    }

    @Override public double getExpenditure() {
        return 0;
    }

    @Override public double getRevenue() {
        return 0;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }

    private Route copy() {
        Route object = new Route(simulation);
        object.setTransportType(getTransportType());
        object.setWeightCost(getWeightCost());
        object.setVolumeCost(getVolumeCost());
        object.setMaxWeight(getMaxWeight());
        object.setMaxVolume(getMaxVolume());
        object.setCompany(getCompany());
        object.setDay(getDay());
        object.setDuration(getDuration());
        object.setFrequency(getFrequency());
        object.setFrom(getFrom());
        object.setTo(getTo());
        object.setDiscontinued(isDiscontinued());
        return object;
    }
}
