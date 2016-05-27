package kps.gui.windows;

import com.sun.istack.internal.NotNull;
import kps.xml.objects.Cost;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.TransportType;

import javax.swing.*;
import java.awt.*;

public class AddRoute extends JDialog {
    private Simulation simulation;
    @NotNull private Cost route;
    private boolean isInDocument;

    public AddRoute(Frame owner, Simulation simulation) {
        this(owner, simulation, null);
    }

    public AddRoute(Frame owner, Simulation simulation, Cost previousRoute) {
        super(owner, "Add route", true);
        this.simulation = simulation;
        this.isInDocument = previousRoute != null;
        this.route = previousRoute == null ? new Cost(simulation) : previousRoute;

        buildDialog();
        setVisible(true);
    }

    private void buildDialog() {
        JComponent components[][] = {
                getField("Company name", route.getCompany() == null ? "" : route.getCompany()),
                getField("Location to", route.getTo() != null ? route.getTo().getName() : ""),
                getField("Location from", route.getFrom() != null ? route.getFrom().getName() : ""),
                getField("Transportation type", route.getTransportType() == null ? TransportType.Air : route.getTransportType()),
                getField("Weight cost", route.getWeightCost()),
                getField("Volume cost", route.getVolumeCost()),
                getField("Max weight", route.getMaxWeight()),
                getField("Max volume", route.getMaxVolume()),
                getField("Duration", route.getDuration()),
                getField("Day of the week", route.getDay() == null ? DayOfWeek.Monday : route.getDay()),
                getField("Frequency of delivery", route.getFrequency())
        };
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridLayout layout = new GridLayout(0, 2);
        layout.setHgap(5);
        layout.setVgap(5);
        formPanel.setLayout(layout);
        for(JComponent[] c : components) {
            for(int a = 0;a < 2;a++) {
                formPanel.add(c[a]);
            }
        }

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> save());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancel());

        formPanel.add(cancelButton);
        formPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
        pack();
    }

    private static JComponent[] getField(String fieldName, Object fieldValue) {
        JLabel label = new JLabel(fieldName);
        JComponent field;
        if(fieldValue.getClass().equals(Integer.class)) {
            field = new JSpinner(new SpinnerNumberModel((Integer)fieldValue, null, null, 1));
        } else if(fieldValue.getClass().equals(String.class)) {
            field = new JTextField(String.valueOf(fieldValue));
        } else if(fieldValue.getClass().equals(TransportType.class)) {
            field = new JComboBox<>(TransportType.values());
            ((JComboBox)field).setSelectedItem(fieldValue);
        } else if(fieldValue.getClass().equals(DayOfWeek.class)) {
            field = new JComboBox<>(DayOfWeek.values());
            ((JComboBox)field).setSelectedItem(fieldValue);
        } else {
            throw new RuntimeException("Unsupported field type");
        }
        return new JComponent[] {label, field};
    }



    private void save() {


        if(!isInDocument) {
            simulation.getCosts().add(route);
        }
        cancel();
    }

    private void cancel() {
        dispose();
    }

}
