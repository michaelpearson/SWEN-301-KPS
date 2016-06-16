package kps.xml.objects.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum Priority {
    @XmlEnumValue("Domestic Air") DOMESTIC_AIR(true, true),
    @XmlEnumValue("Domestic Standard") DOMESTIC_STANDARD(true, false),
    @XmlEnumValue("International Air") INTERNATIONAL_AIR(false, true),
    @XmlEnumValue("International Standard") INTERNATIONAL_STANDARD(false, false);

    private boolean domestic;
    private boolean air;

    Priority(boolean domestic, boolean air) {
        this.domestic = domestic;
        this.air = air;
    }

    @Override
    public String toString() {
        switch(this) {
            case DOMESTIC_STANDARD:
            default:
                return "Domestic standard";
            case DOMESTIC_AIR:
                return "Domestic air";
            case INTERNATIONAL_AIR:
                return "International air";
            case INTERNATIONAL_STANDARD:
                return "International standard";
        }
    }

    public boolean willSettleFor(Priority priority) {
        /*if(this == DOMESTIC_STANDARD || this == INTERNATIONAL_STANDARD) {
            return true;
        }
        if(this == DOMESTIC_AIR && priority != DOMESTIC_AIR) {
            return false;
        }
        if(this == INTERNATIONAL_AIR && priority != INTERNATIONAL_AIR) {
            return false;
        }*/
        return true;
    }

    public boolean willSettleFor(TransportType transportType) {
        /*if(this == DOMESTIC_STANDARD || this == INTERNATIONAL_STANDARD) {
            return true;
        }
        if((this == DOMESTIC_AIR || this == INTERNATIONAL_AIR) && transportType != TransportType.Air) {
            return false;
        }*/
        return true;
    }

    public boolean isDomestic() {
        return domestic;
    }

    public boolean isAir() {
        return air;
    }
}
