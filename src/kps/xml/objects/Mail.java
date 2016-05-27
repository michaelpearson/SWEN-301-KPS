package kps.xml.objects;

import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.Priority;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Mail extends ModelObject {
    @XmlElement private DayOfWeek day;
    @XmlElement private String to;
    @XmlElement private String from;
    @XmlElement private int weight;

    public Mail(Simulation s) {
        super(s);
    }

    public Mail() {
    }

    @XmlElement private int volume;
    @XmlElement private Priority priority;
}
