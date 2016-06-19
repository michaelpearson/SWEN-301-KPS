package kps.gui.models;

import kps.xml.objects.Simulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class OriginModel extends LocationsModel {

    private DestinationModel destination = null;

    public OriginModel(Simulation simulation) {
        super(simulation);
    }

    public OriginModel(Simulation simulation, DestinationModel destination){
        super(simulation);
        this.destination = destination;
    }

    public void setDestination(DestinationModel destination){
        this.destination = destination;
    }

    @Override public void updateModel(){
        if(destination==null || destination.getSelectedItem() == null){
            super.updateModel();
        }else {
            validLocations = new ArrayList<>(simulation.getValidOrigins((String) destination.getSelectedItem()));
        }
    }

}
