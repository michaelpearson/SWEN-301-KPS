package kps.xml.objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Location extends ModelObject {
    @XmlAttribute(name="id") private int id;
    private String name;



    public Location(Simulation s) {
        super(s);
        int maxLocationId = 0;
        for(Location l : simulation.getLocations()) {
            if(l.getId() > maxLocationId) {
                maxLocationId = l.getId();
            }
        }
        setId(maxLocationId);
    }

    public Location() {
    }



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
