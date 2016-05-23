package xml.objects;

import xml.objects.enums.DayOfWeek;
import xml.objects.enums.Priority;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Mail {
    @XmlElement private DayOfWeek day;
    @XmlElement private String to;
    @XmlElement private String from;
    @XmlElement private int weight;
    @XmlElement private int volume;
    @XmlElement private Priority priority;
}
