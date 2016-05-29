package kps.xml.objects;

import kps.xml.objects.abstracts.BusinessEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement(name="simulation") @XmlAccessorType(XmlAccessType.FIELD) public class Simulation {

    private static Set<SimulationUpdateListener> updateListeners = new HashSet<>();
    @XmlTransient private HashMap<Integer, Location> locationIdCache = new HashMap<>();
    @XmlTransient private HashMap<String, Location> locationNameCache = new HashMap<>();

    @XmlElement(name="cost") private List<Cost> costs;
    @XmlElement(name="mail") private List<Mail> mail;
    @XmlElement(name="price") private List<Price> price;
    @XmlElement(name="location") private List<Location> locations;

    public Simulation() {
        this.costs = new ArrayList<>();
        this.mail = new ArrayList<>();
        this.price = new ArrayList<>();
        this.locations = new ArrayList<>();
    }

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


    public List<Location> getLocations() {
        return locations;
    }

    @Nullable public Location getLocationById(int id) {
        if(locationIdCache.get(id) != null) {
            return locationIdCache.get(id);
        }
        for(Location l : locations) {
            if(l.getId() == id) {
                locationIdCache.put(id, l);
                return l;
            }
        }
        return null;
    }

    @Nullable public Location getLocationByName(@NotNull String name) {
        if(locationNameCache.get(name) != null) {
            return locationNameCache.get(name);
        }
        for(Location l : locations) {
            if(name.equals(l.getName())) {
                locationNameCache.put(name, l);
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
        build.sort((left, right) -> right.getDate().compareTo(left.getDate()));
        return build;
    }
}
