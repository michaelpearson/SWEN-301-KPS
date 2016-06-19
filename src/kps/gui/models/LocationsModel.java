package kps.gui.models;

import kps.xml.objects.Simulation;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class LocationsModel extends AbstractListModel<String> implements ComboBoxModel<String> {
    protected final Simulation simulation;
    private Object selectedObject = null;
    private final String dummyLocation = "Add new location...";
    private int size;
    protected List<String> validLocations;

    public LocationsModel(Simulation simulation) {
        this.simulation = simulation;
        size = getSize();
        updateModel();
    }

    @Override public void setSelectedItem(Object item) {
        selectedObject = item;
    }

    @Override public Object getSelectedItem() {
        return selectedObject;
    }

    @Override protected void fireContentsChanged(Object source, int index0, int index1) {
        updateModel();
        super.fireContentsChanged(source, index0, index1);
    }

    public void updateModel() {
        validLocations = new ArrayList<>(simulation.getLocations());
    }

    @Override public int getSize() {
        //Why was this check performed?
        if(validLocations == null || size != validLocations.size()) {
            fireContentsChanged(this, -1, -1);
        }
        return validLocations.size() + 1;
    }

    @Override public String getElementAt(int index) {
        if(index >= validLocations.size()) {
            return dummyLocation;
        }
        return validLocations.get(index);
    }

    public String getDummyLocation() {
        return dummyLocation;
    }
}
