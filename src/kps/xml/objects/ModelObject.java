package kps.xml.objects;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class ModelObject {
    protected Simulation simulation;
    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }
    protected Simulation getSimulation() {
        return simulation;
    }

    public ModelObject(Simulation s) {
        setSimulation(s);
    }

    public ModelObject() {
    }
}