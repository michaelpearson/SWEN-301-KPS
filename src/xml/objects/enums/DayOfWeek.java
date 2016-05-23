package xml.objects.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum DayOfWeek {
    @XmlEnumValue("Monday") Monday,
    @XmlEnumValue("Tuesday") Tuesday,
    @XmlEnumValue("Wednesday") Wednesday,
    @XmlEnumValue("Thursday") Thursday,
    @XmlEnumValue("Friday") Friday,
    @XmlEnumValue("Saturday") Saturday,
    @XmlEnumValue("Sunday") Sunday
}