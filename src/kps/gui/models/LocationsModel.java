package kps.gui.models;

import kps.xml.objects.Location;
import kps.xml.objects.Simulation;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the model for the location drop-downs used in dialogs
 */
public class LocationsModel extends AbstractListModel<String> implements ComboBoxModel<String> {
    private final Simulation simulation;
    private Object selectedObject = null;
    private final String dummyLocation = "Add new location...";
    private int size;
    private List<Location> allLocations;

    /**
     * Main constructor which creates a LocationsModel
     * @param simulation the simulation to work from
     */
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
        allLocations = simulation.getLocations();
    }

    @Override public int getSize() {
        if(size != simulation.getLocations().size() + 1) {
            fireContentsChanged(this, -1, -1);
        }
        return simulation.getLocations().size() + 1;
    }

    @Override public String getElementAt(int index) {
        if(index >= allLocations.size()) {
            return dummyLocation;
        }
        return allLocations.get(index).getName();
    }

    /**
     * Dummy location is the "Add new location..." option
     * @return the object which represents the "Add new location..." option
     */
    public String getDummyLocation() {
        return dummyLocation;
    }
}
