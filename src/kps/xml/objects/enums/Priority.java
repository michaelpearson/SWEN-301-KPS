package kps.xml.objects.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum Priority {
    @XmlEnumValue("Domestic") DOMESTIC,
    @XmlEnumValue("International Air") INTERNATIONAL_AIR,
    @XmlEnumValue("International Standard") INTERNATIONAL_STANDARD;

    @Override
    public String toString() {
        switch(this) {
            case DOMESTIC:
            default:
                return "Domestic standard";
            case INTERNATIONAL_AIR:
                return "International air";
            case INTERNATIONAL_STANDARD:
                return "International standard";
        }
    }

    public boolean willSettleFor(Priority priority) {
        if(this == DOMESTIC || this == INTERNATIONAL_STANDARD) {
            return true;
        }
        if(this == INTERNATIONAL_AIR && priority != INTERNATIONAL_AIR) {
            return false;
        }
        return true;
    }

    public boolean willSettleFor(TransportType transportType) {
        if(this == DOMESTIC || this == INTERNATIONAL_STANDARD) {
            return true;
        }
        if(this == INTERNATIONAL_AIR && transportType != TransportType.Air) {
            return false;
        }
        return true;
    }
}
