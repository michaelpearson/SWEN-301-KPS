package kps.gui.windows;

import kps.gui.FormDialog;
import kps.gui.models.CostUpdateTableModel;
import kps.gui.models.HomepageTableModel;
import kps.xml.objects.Cost;
import kps.xml.objects.Mail;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.Priority;
import kps.xml.objects.enums.TransportType;
import org.jdesktop.swingx.JXTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class CostUpdateDialog extends FormDialog {
    private @NotNull Cost route;
    private boolean isInDocument;
    private final Frame owner;


    private enum FieldNames {
        CompanyName,
        LocationTo,
        LocationFrom,
        TransportType,
        WeightCost,
        VolumeCost,
        MaxWeight,
        MaxVolume,
        Duration,
        DayOfWeek,
        Frequency
    }

    public CostUpdateDialog(Frame owner, Simulation simulation) {
        this(owner, simulation, null);
    }

    public CostUpdateDialog(Frame owner, Simulation simulation, @Nullable Cost previousRoute) {
        super(owner, "Mail Delivery", true, simulation);
        this.isInDocument = previousRoute != null;
        this.owner = owner;
        buildTable();
        setVisible(true);
    }

    private void buildTable() {
        setSize(900, 300);
        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        tablePanel.setLayout(new BorderLayout());
        JXTable table = new JXTable(new CostUpdateTableModel(simulation));
        table.setFillsViewportHeight(true);
        table.packColumn(0, 6);
        table.setBorder(BorderFactory.createEmptyBorder());
        table.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                JTable table =(JTable) e.getSource();
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                if (e.getClickCount() == 2) {
                    ((HomepageTableModel)table.getModel()).edit(row, owner);
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                System.out.println(simulation.getCosts().get(table.getSelectedRow()).toString());
                route = simulation.getCosts().get(table.getSelectedRow());
                tablePanel.setVisible(false);
                tablePanel.removeAll();
                isInDocument = true;
                buildDialog();
            }
        });
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

    }

    @Override
    protected JComponent[][] getAllFields() {
        return new JComponent[][]{
                getField(FieldNames.CompanyName, "Company name", route.getCompany() == null ? "" : route.getCompany()),
                getField(FieldNames.LocationTo, "Location to", route.getTo() != null ? route.getTo().getName() : ""),
                getField(FieldNames.LocationFrom, "Location from", route.getFrom() != null ? route.getFrom().getName() : ""),
                getField(FieldNames.TransportType, "Transportation type", route.getTransportType() == null ? TransportType.Air : route.getTransportType()),
                getField(FieldNames.WeightCost, "Weight cost", route.getWeightCost()),
                getField(FieldNames.VolumeCost, "Volume cost", route.getVolumeCost()),
                getField(FieldNames.MaxWeight, "Max weight", route.getMaxWeight()),
                getField(FieldNames.MaxVolume, "Max volume", route.getMaxVolume()),
                getField(FieldNames.Duration, "Duration", route.getDuration()),
                getField(FieldNames.DayOfWeek, "Day of the week", route.getDay() == null ? DayOfWeek.Monday : route.getDay()),
                getField(FieldNames.Frequency, "Frequency of delivery", route.getFrequency())
        };
    }

    protected void save() {
        Set<Map.Entry<Object, Object>> entries = getAllValues().entrySet();
        for(Map.Entry<Object, Object> entry : entries) {
            System.out.println(entry.getValue());
            switch((FieldNames)entry.getKey()) {
                case CompanyName:
                    route.setCompany((String)entry.getValue());
                    break;
                case LocationTo:
                    route.setTo((String)entry.getValue());
                    break;
                case LocationFrom:
                    route.setFrom((String)entry.getValue());
                    break;
                case DayOfWeek:
                    route.setDay(Arrays.asList(DayOfWeek.values()).stream().filter(v -> v.equals((DayOfWeek)entry.getValue())).findFirst().get());
                    break;
                case Duration:
                    route.setDuration((Integer)entry.getValue());
                    break;
                case Frequency:
                    route.setFrequency((Integer)entry.getValue());
                    break;
                case MaxVolume:
                    route.setMaxVolume((Integer)entry.getValue());
                    break;
                case MaxWeight:
                    route.setMaxWeight((Integer)entry.getValue());
                    break;
                case TransportType:
                    route.setTransportType(Arrays.asList(TransportType.values()).stream().filter(v -> v.equals((TransportType)entry.getValue())).findFirst().get());
                    break;
                case VolumeCost:
                    route.setVolumeCost((Integer)entry.getValue());
                    break;
                case WeightCost:
                    route.setWeightCost((Integer)entry.getValue());
                    break;
                default:
                    throw new RuntimeException("Unknown field");
            }
        }
        if(!isInDocument) {
            simulation.getCosts().add(route);
        }
        cancel();
    }

    protected void cancel() {
        dispose();
    }

}
