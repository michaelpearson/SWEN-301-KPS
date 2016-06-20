package kps.gui.windows;

import kps.gui.models.BusinessEventsTableModel;
import kps.gui.windows.form.dialogs.CustomerPriceDialog;
import kps.gui.windows.form.dialogs.MailDialog;
import kps.gui.windows.form.dialogs.RouteDialog;
import kps.xml.SimulationXML;
import kps.xml.exceptions.XMLException;
import kps.xml.objects.Simulation;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.PrintStream;

@SuppressWarnings("WeakerAccess")
public class Home extends JFrame {
    private final Simulation simulation;
    private String fileName;
    private Login.UserName user;


    public static final String APPLICATION_NAME = "Swen 301 - KPS";

    @SuppressWarnings("UnusedParameters") //todo: restrict access based on user.
    public Home(Simulation simulation, String fileName, Login.UserName user) {
        this.simulation = simulation;
        this.fileName = fileName;
        this.user = user;

        Dimension size = new Dimension(1300, 700);
        setMinimumSize(size);
        setSize(size);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(APPLICATION_NAME);
        setLayout(new GridLayout(0, 2));

        buildGui();

        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void buildGui() {
        setLayout(new BorderLayout());

        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        tablePanel.setLayout(new BorderLayout());

        BusinessEventsTableModel tableModel = new BusinessEventsTableModel(simulation);
        JXTable table = new JXTable(tableModel);
        table.setFillsViewportHeight(true);
        table.packColumn(0, 6);
        table.setBorder(BorderFactory.createEmptyBorder());
        table.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JXTable table = (JXTable) e.getSource();
                    int row = table.convertRowIndexToModel(table.getSelectedRow());
                    tableModel.edit(row, Home.this);
                    tableModel.fireTableDataChanged();
                }
            }
        });
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        GridLayout buttonLayout = new GridLayout(10, 1);
        buttonLayout.setVgap(10);
        buttonPanel.setLayout(buttonLayout);


        JButton addRouteButton = new JButton("Add route");
        addRouteButton.addActionListener(e -> {new RouteDialog(Home.this, simulation); tableModel.updateTable();});
        buttonPanel.add(addRouteButton);

        JButton addMailDeliveryButton = new JButton("Add mail delivery");
        addMailDeliveryButton.addActionListener(e -> {new MailDialog(Home.this, simulation); tableModel.updateTable();});
        buttonPanel.add(addMailDeliveryButton);

        JButton updateTransportCostButton = new JButton("Update transport cost");
        updateTransportCostButton.addActionListener(e -> {new TransportRouteUpdateTable(Home.this, simulation); tableModel.updateTable();});
        buttonPanel.add(updateTransportCostButton);


        if(user == Login.UserName.Manager) {
            JButton viewDecisionSupportButton = new JButton("Open decision support");
            viewDecisionSupportButton.addActionListener(e -> new DecisionSupport(simulation));
            buttonPanel.add(viewDecisionSupportButton);
        }


        JButton addCustomerPriceButton = new JButton("Add customer price");
        addCustomerPriceButton.addActionListener(e -> {new CustomerPriceDialog(Home.this, simulation); tableModel.updateTable();});
        buttonPanel.add(addCustomerPriceButton);

        JButton viewLocationsButton = new JButton("View locations");
        viewLocationsButton.addActionListener(e -> {new LocationWindow(simulation); tableModel.updateTable();});
        buttonPanel.add(viewLocationsButton);

        JButton exitButton = new JButton("Save and exit");
        exitButton.addActionListener(e -> save(true));
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.EAST);
    }

    private void save(boolean exit) {
        JDialog saveDialog = new JDialog(this, true);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridLayout layout = new GridLayout(2, 1);
        layout.setVgap(10);
        panel.setLayout(layout);
        JProgressBar progress = new JProgressBar(0, 100);
        progress.setValue(50); //Hack hack hack...
        JLabel label = new JLabel("Saving data...");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        new Thread() {
            @Override
            public void run() {
                try {
                    SimulationXML.writeSimulation(simulation, new PrintStream(fileName));
                    if(exit) {
                        System.exit(0);
                    }
                } catch (XMLException | FileNotFoundException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(saveDialog, "Could not open the log file for writing.", "Error saving", JOptionPane.ERROR_MESSAGE);
                }

            }
        }.start();

        saveDialog.setTitle("Progress");
        saveDialog.setResizable(false);
        saveDialog.setLocationRelativeTo(null);
        saveDialog.setSize(400, 100);

        panel.add(label);
        panel.add(progress);

        saveDialog.add(panel);
        saveDialog.setVisible(true);

    }
}
