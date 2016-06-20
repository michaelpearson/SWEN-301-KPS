package kps.xml.objects.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * An enum which defines the type of transport.
 * (used with reflection)
 */
@SuppressWarnings("unused") @XmlEnum public enum TransportType {
    @XmlEnumValue("Land") Land,
    @XmlEnumValue("Sea") Sea,
    @XmlEnumValue("Air") Air
}
