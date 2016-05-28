package kps.gui.windows;

import kps.gui.FormDialog;
import kps.xml.objects.Simulation;

import javax.swing.*;
import java.awt.*;

public class LocationDialog extends FormDialog {
    public static final String NAME = "Name";

    public LocationDialog(Frame owner, String title, boolean modal, Simulation simulation) {
        super(owner, title, modal, simulation);
        buildDialog();
        setVisible(true);
    }

    public LocationDialog(String title, Simulation simulation) {
        super(title, simulation);
    }

    @Override
    protected JComponent[][] getAllFields() {
        return new JComponent[][] {
            getField(NAME, "Location Name", "")
        };
    }

    @Override
    protected void save() {
        cancel();
    }

    @Override
    protected void cancel() {
        dispose();
    }
}
