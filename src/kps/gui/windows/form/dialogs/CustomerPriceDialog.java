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

/**
 * The customer price dialog
 */
public class CustomerPriceDialog extends FormDialog {
    private @NotNull CustomerPrice customerPrice;
    private boolean isInDocument;

    /**
     * The tags for each field so the values can be retrieved.
     */
    private enum FieldNames {
        LocationTo,
        LocationFrom,
        Priority,
        WeightCost,
        VolumeCost,
    }

    /**
     * Constructor for when it is a new customer price object
     * @param owner the frame owner
     * @param simulation the simulation to add the customer price to
     */
    public CustomerPriceDialog(Frame owner, Simulation simulation) {
        this(owner, simulation, null);
    }

    /**
     * Constructor for when you have a customer price update (not a new customer price)
     * @param owner the frame owner
     * @param simulation the simulation to update the customer price
     * @param previousCustomerPrice the customer price to update
     */
    public CustomerPriceDialog(Frame owner, Simulation simulation, @Nullable CustomerPrice previousCustomerPrice) {
        this(owner, simulation, false, previousCustomerPrice);
    }

    /**
     * The main constructor with all sorts of options. (see the other constructors)
     * @param owner
     * @param simulation
     * @param isUpdate
     * @param previousCustomerPrice
     */
    public CustomerPriceDialog(@Nullable Frame owner, @NotNull Simulation simulation, boolean isUpdate, @Nullable CustomerPrice previousCustomerPrice) {
        super(owner, previousCustomerPrice == null ? "Add Customer Price" : "Edit Customer Price", true, simulation);
        if(isUpdate) {
            isInDocument = false;
            setTitle("Update customer price");
        } else {
            this.isInDocument = previousCustomerPrice != null;
        }
        this.customerPrice = previousCustomerPrice == null ? new CustomerPrice(simulation) : previousCustomerPrice;
        buildDialog();
        setVisible(true);
    }

    @Override
    protected void initializeForm(@NotNull FormBuilder builder) {
        builder.addLocationField(FieldNames.LocationTo, "Location to", customerPrice.getTo());
        builder.addEnumField(FieldNames.Priority, "Priority", customerPrice.getPriority(), Priority.class);
        builder.addIntegerField(FieldNames.WeightCost, "Weight cost (cents/grams)", customerPrice.getWeightCost());
        builder.addIntegerField(FieldNames.VolumeCost, "Volume cost (cents/cm^3)", customerPrice.getVolumeCost());
    }

    protected void save() {
        if (!validateFields()) return;
        customerPrice.setTo((String)getValue(FieldNames.LocationTo));
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
