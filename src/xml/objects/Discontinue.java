package xml.objects;

import xml.objects.enums.TransportType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Discontinue {
    @XmlElement private String company;
    @XmlElement private String to;
    @XmlElement private String from;
    @XmlElement private TransportType transportType;
}
