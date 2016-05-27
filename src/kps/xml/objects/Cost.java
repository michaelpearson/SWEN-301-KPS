package kps.xml.objects;

import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.TransportType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Cost extends ModelObject {
    @XmlElement private String company;
    @XmlElement private int to;
    @XmlElement private int from;
    @XmlElement(name="type") private TransportType transportType;
    @XmlElement(name="weightcost") private int weightCost;
    @XmlElement(name="volumecost") private int volumeCost;
    @XmlElement private int maxWeight;
    @XmlElement private int maxVolume;
    @XmlElement private int duration;
    @XmlElement private int frequency;
    @XmlElement(name="day") private DayOfWeek day;

    public Cost() {
    }

    public Cost(Simulation s) {
        super(s);

    }

    public String getCompany() {
        return company;
    }

    public Location getTo() {
        return getSimulation().getLocationById(to);
    }

    public Location getFrom() {
        return getSimulation().getLocationById(from);
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

}
