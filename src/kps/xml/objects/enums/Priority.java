package kps.xml.objects.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum Priority {
    @XmlEnumValue("Domestic Standard") DOMESTIC_STANDARD,
    @XmlEnumValue("International Air") INTERNATIONAL_AIR,
    @XmlEnumValue("International Standard") INTERNATIONAL_STANDARD;
}
