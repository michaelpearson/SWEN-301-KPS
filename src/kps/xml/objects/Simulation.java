package kps.xml.objects;

import kps.xml.objects.abstracts.BusinessEvent;
import kps.xml.objects.abstracts.ModelObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement(name="simulation") @XmlAccessorType(XmlAccessType.FIELD) public class Simulation {

    private static Set<SimulationUpdateListener> updateListeners = new HashSet<>();

    @XmlElement(name="cost") private List<Cost> costs;
    @XmlElement(name="mail") private List<Mail> mail;
    @XmlElement(name="price") private List<Price> price;
    @XmlElement(name="discontinue") private List<Discontinue> discontinuedRoutes;
    @XmlElement(name="location") private List<Location> locations;

    public interface SimulationUpdateListener {
        void simulationUpdated();
    }

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

    @Nullable public Location getLocationByName(@NotNull String name) {
        for(Location l : locations) {
            if(name.equals(l.getName())) {
                return l;
            }
        }
        return null;
    }

    public void addUpdateListener(SimulationUpdateListener updateListener) {
        updateListeners.add(updateListener);
    }

    public void fireUpdateListeners() {
        new Thread() {
            @Override
            public void run() {
                Simulation.updateListeners.forEach(SimulationUpdateListener::simulationUpdated);
            }
        }.start();
    }

    public List<BusinessEvent> getAllBusinessEvents() {
        ArrayList<BusinessEvent> build = new ArrayList<>();
        build.addAll(costs);
        build.addAll(mail);
        build.addAll(price);
        build.addAll(discontinuedRoutes);
        build.sort((left, right) -> right.getDate().compareTo(left.getDate()));
        return build;
    }
}
