package kps.gui.windows.form.dialogs;

import kps.gui.windows.form.FormBuilder;
import kps.gui.windows.form.FormDialog;
import kps.xml.objects.Simulation;

import java.awt.*;

public class LocationDialog extends FormDialog {
    public static final String NAME = "Name";

    public LocationDialog(Frame owner, String title, boolean modal, Simulation simulation) {
        super(owner, title, modal, simulation);
        buildDialog();
        setVisible(true);
    }

    @Override
    protected void initializeForm(FormBuilder builder) {
        builder.addStringField(NAME, "Location name", null);
    }

    @Override
    protected void save() {
        cancel();
    }

    @Override
    protected boolean cancel() {
        dispose();
        return true;
    }
}
