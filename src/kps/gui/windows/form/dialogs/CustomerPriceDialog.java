package kps.gui.windows.form.dialogs;

import kps.gui.windows.form.FormBuilder;
import kps.gui.windows.form.FormDialog;
import kps.xml.objects.CustomerPrice;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

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
    protected void initializeForm(FormBuilder builder) {
        builder.addLocationField(FieldNames.LocationFrom, "Location from", customerPrice.getFrom());
        builder.addLocationField(FieldNames.LocationTo, "Location to", customerPrice.getTo());
        builder.addEnumField(FieldNames.Priority, "Priority", customerPrice.getPriority(), Priority.class);
        builder.addIntegerField(FieldNames.WeightCost, "Weight cost (cents/grams)", customerPrice.getWeightCost());
        builder.addIntegerField(FieldNames.VolumeCost, "Volume cost (cents/cm^3)", customerPrice.getVolumeCost());
    }

    protected void save() {
        if (!validateFields()) return;
        customerPrice.setTo((String)getValue(FieldNames.LocationTo));
        customerPrice.setFrom((String)getValue(FieldNames.LocationFrom));
        customerPrice.setPriority((Priority)getValue(FieldNames.Priority));
        customerPrice.setVolumeCost((Integer)getValue(FieldNames.VolumeCost));
        customerPrice.setWeightCost((Integer)getValue(FieldNames.WeightCost));

        if(customerPrice.getPriority().isDomestic() && !customerPrice.isDomestic()) {
            JOptionPane.showMessageDialog(this, "Please set the route to be domestic if the priority is domestic", "Invalid option selected", JOptionPane.ERROR_MESSAGE);
            return;
        } else if(!customerPrice.getPriority().isDomestic() && customerPrice.isDomestic()) {
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
