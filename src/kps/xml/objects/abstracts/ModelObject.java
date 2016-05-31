package kps.xml.objects.abstracts;

import kps.xml.objects.Simulation;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient public abstract class ModelObject {
    @XmlTransient protected Simulation simulation;

    public ModelObject(Simulation s) {
        setSimulation(s);
    }

    protected ModelObject() {}

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    @XmlTransient protected Simulation getSimulation() {
        return simulation;
    }

}
