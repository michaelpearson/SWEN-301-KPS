package kps.gui.windows.dialogs;

import kps.gui.FormDialog;
import kps.xml.objects.CustomerPrice;
import kps.xml.objects.Location;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.Priority;
import kps.xml.objects.enums.TransportType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class CustomerPriceDialog extends FormDialog {
    private @NotNull CustomerPrice customerPrice;
    private boolean isInDocument;

    private enum FieldNames {
        LocationTo,
        LocationFrom,
        Priority,
        WeightCost,
        VolumeCost,
    }

    public CustomerPriceDialog(Frame owner, Simulation simulation) {
        this(owner, simulation, null);
    }

    public CustomerPriceDialog(Frame owner, Simulation simulation, @Nullable CustomerPrice previousCustomerPrice) {
        this(owner, simulation, false, previousCustomerPrice);
    }
    public CustomerPriceDialog(@Nullable Frame owner, @NotNull Simulation simulation, boolean isUpdate, @Nullable CustomerPrice previousCustomerPrice) {
        super(owner, previousCustomerPrice == null ? "Add Customer Price" : "Edit Customer Price", true, simulation);
        if(isUpdate) {
            isInDocument = false;
            setTitle("Update route");
        } else {
            this.isInDocument = previousCustomerPrice != null;
        }
        this.customerPrice = previousCustomerPrice == null ? new CustomerPrice(simulation) : previousCustomerPrice;
        buildDialog();
        setVisible(true);
    }



    @Override
    protected JComponent[][] getAllFields() {
        return new JComponent[][]{
                getField(FieldNames.LocationFrom, "Location from", customerPrice.getFrom(), Location.class),
                getField(FieldNames.LocationTo, "Location to", customerPrice.getTo(), Location.class),
                getField(FieldNames.Priority, "Priority", customerPrice.getPriority(), Priority.class),
                getField(FieldNames.WeightCost, "Weight cost (cents/grams)", customerPrice.getWeightCost(), Integer.class),
                getField(FieldNames.VolumeCost, "Volume cost (cents/cm^3)", customerPrice.getVolumeCost(), Integer.class),
        };
    }

    protected void save() {
        if (!validateFields()) return;
        Map<Object, Object> entries = getAllValues();
        customerPrice.setTo((Location)entries.get(FieldNames.LocationTo));
        customerPrice.setFrom((Location)entries.get(FieldNames.LocationFrom));
        customerPrice.setPriority((Priority)entries.get(FieldNames.Priority));
        customerPrice.setVolumeCost((Integer)entries.get(FieldNames.VolumeCost));
        customerPrice.setWeightCost((Integer)entries.get(FieldNames.WeightCost));

        if(customerPrice.getPriority() == Priority.DOMESTIC && !customerPrice.isDomestic()) {
            JOptionPane.showMessageDialog(this, "Please set the route to be domestic if the priority is domestic", "Invalid option selected", JOptionPane.ERROR_MESSAGE);
            return;
        } else if(customerPrice.getPriority() != Priority.DOMESTIC && customerPrice.isDomestic()) {
            JOptionPane.showMessageDialog(this, "Please set the priority to domestic if the route is domestic", "Invalid option selected", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!isInDocument) {
            simulation.getCustomerPrices().add(customerPrice);
        }
        cancel();
    }

    protected boolean cancel() {
        dispose();
        return true;
    }

}
