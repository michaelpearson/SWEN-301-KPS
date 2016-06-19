package kps.gui.models;

import kps.xml.objects.Simulation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class DestinationModel extends LocationsModel {

    private OriginModel origin = null;

    public DestinationModel(Simulation simulation) {
        super(simulation);
    }

    public DestinationModel(Simulation simulation, OriginModel origin) {
        super(simulation);
        this.origin = origin;
    }

    public void setOrigin(OriginModel origin){
        this.origin = origin;
    }

    @Override public void updateModel(){
        if(origin==null || origin.getSelectedItem() == null){
            super.updateModel();
        }else {
            validLocations = new ArrayList<>(simulation.getValidDestinations((String) origin.getSelectedItem()));
        }
    }

}
