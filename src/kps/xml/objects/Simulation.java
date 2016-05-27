package kps.xml.objects;

import com.sun.istack.internal.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name="simulation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Simulation {

    @XmlElement(name="cost") List<Cost> costs;
    @XmlElement(name="mail") List<Mail> mail;
    @XmlElement(name="price") List<Price> price;
    @XmlElement(name="discontinue") List<Discontinue> discontinuedRoutes;
    @XmlElement(name="location") List<Location> locations;

    public List<Cost> getCosts() {
        return costs;
    }

    public List<Mail> getMail() {
        return mail;
    }

    public List<Price> getPrice() {
        return price;
    }

    public List<Discontinue> getDiscontinuedRoutes() {
        return discontinuedRoutes;
    }

    public List<Location> getLocations() {
        return locations;
    }

    @Nullable public Location getLocationById(int id) {
        for(Location l : locations) {
            if(l.getId() == id) {
                return l;
            }
        }
        return null;
    }
}
