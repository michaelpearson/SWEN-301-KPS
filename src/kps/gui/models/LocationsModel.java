package kps.gui.models;

import kps.xml.objects.Location;
import kps.xml.objects.Simulation;

import javax.swing.*;


public class LocationsModel extends AbstractListModel<Location> implements ComboBoxModel<Location> {
    private final Simulation simulation;
    private Object selectedObject = null;
    private final Location dummyLocation = new Location();
    private int size;

    public LocationsModel(Simulation simulation) {
        this.simulation = simulation;
        size = getSize();
        dummyLocation.setName("Add new location...");

    }

    @Override
    public void setSelectedItem(Object item) {
        selectedObject = item;
    }

    @Override
    public Object getSelectedItem() {
        return selectedObject;
    }

    @Override
    public int getSize() {
        if(size != simulation.getLocations().size() + 1) {
            fireContentsChanged(this, -1, -1);
        }
        return simulation.getLocations().size() + 1;
    }

    @Override
    public Location getElementAt(int index) {
        if(index >= simulation.getLocations().size()) {
            return dummyLocation;
        }
        return simulation.getLocations().get(index);
    }

    public Location getDummyLocation() {
        return dummyLocation;
    }
}
