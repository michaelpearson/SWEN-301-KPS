package kps.xml.objects;

import kps.xml.objects.abstracts.BusinessEvent;
import kps.xml.objects.abstracts.ModelObject;
import kps.xml.objects.enums.Priority;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Price extends BusinessEvent {

    @XmlElement private String to;
    @XmlElement private String from;
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

}
