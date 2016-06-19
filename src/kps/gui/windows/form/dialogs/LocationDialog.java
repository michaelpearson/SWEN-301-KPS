package kps.gui.windows.form.dialogs;

import kps.gui.windows.form.FormBuilder;
import kps.gui.windows.form.FormDialog;
import kps.xml.objects.Simulation;

import java.awt.*;

/**
 * Dialog for updating and creating new locations
 */
public class LocationDialog extends FormDialog {
    public static final String NAME = "Name";

    /**
     * Main constructor for the locations dialog
     * @param owner the frames owner
     * @param title the frame title
     * @param modal whether the frame should disable background windows
     * @param simulation the simulation which the location should be updated or added t
     */
    public LocationDialog(Frame owner, String title, boolean modal, Simulation simulation) {
        super(owner, title, modal, simulation);
        buildDialog();
        setVisible(true);
    }

    @Override protected void initializeForm(FormBuilder builder) {
        builder.addStringField(NAME, "Location name", null);
    }

    @Override protected void save() {
        cancel();
    }

    @Override protected boolean cancel() {
        dispose();
        return true;
    }
}
