package kps.xml.objects.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum TransportType {
    @XmlEnumValue("Land") Land,
    @XmlEnumValue("Sea") Sea,
    @XmlEnumValue("Air") Air
}
