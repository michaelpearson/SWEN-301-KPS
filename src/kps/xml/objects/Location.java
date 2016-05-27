package kps.xml.objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Location extends ModelObject {
    public Location(Simulation s) {
        super(s);
    }

    public Location() {
    }

    @XmlAttribute(name="id") private int id;
    @XmlValue private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
