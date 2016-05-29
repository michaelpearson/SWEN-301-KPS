package kps.xml.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE) public class CalculatedRoute {
    @XmlElement(name="route") private List<Route> routes = new ArrayList<>();

    public CalculatedRoute() {}

    private CalculatedRoute(List<Route> routes) {
        this.routes = routes;
    }

    public CalculatedRoute(CalculatedRoute current, Route append) {
        routes.addAll(current.routes);
        routes.add(append);
    }

    public void appendRoute(Route r) {
        routes.add(r);
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public CalculatedRoute copy() {
        return new CalculatedRoute(new ArrayList<>(routes));
    }

    @Override
    public String toString() {
        return "CalculatedRoute: " + routes;
    }
}
