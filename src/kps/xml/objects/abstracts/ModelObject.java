package kps.xml.objects.abstracts;

import kps.xml.objects.Simulation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient public abstract class ModelObject {
    @Nullable @XmlTransient protected Simulation simulation;

    public ModelObject(Simulation s) {
        setSimulation(s);
    }

    protected ModelObject() {}

    public void setSimulation(@NotNull Simulation simulation) {
        this.simulation = simulation;
    }

    @Nullable @XmlTransient protected Simulation getSimulation() {
        return simulation;
    }

}
