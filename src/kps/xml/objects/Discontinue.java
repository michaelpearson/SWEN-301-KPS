package kps.xml.objects;

import kps.xml.objects.enums.TransportType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Discontinue extends ModelObject {
    @XmlElement private String company;

    public Discontinue(Simulation s) {
        super(s);
    }

    public Discontinue() {
    }

    @XmlElement private String to;
    @XmlElement private String from;
    @XmlElement private TransportType transportType;
}
