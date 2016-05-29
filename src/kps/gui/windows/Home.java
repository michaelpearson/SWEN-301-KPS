package kps.gui.windows;

import kps.gui.models.HomepageTableModel;
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

public class Home extends JFrame {
    private final Simulation simulation;
    private final Login.UserName user;

    @SuppressWarnings("WeakerAccess")
    public static final String APPLICATION_NAME = "Swen 301 - KPS";

    public Home(Simulation simulation, Login.UserName user) {
        this.simulation = simulation;
        this.user = user;

        Dimension size = new Dimension(900, 500);
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

        JXTable table = new JXTable(new HomepageTableModel(simulation));
        table.setFillsViewportHeight(true);
        table.packColumn(0, 6);
        table.setBorder(BorderFactory.createEmptyBorder());
        table.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JXTable table = (JXTable) e.getSource();
                    int row = table.convertRowIndexToModel(table.getSelectedRow());
                    ((HomepageTableModel)table.getModel()).edit(row, Home.this);
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
        addRouteButton.addActionListener(e -> new RouteDialog(Home.this, simulation));
        buttonPanel.add(addRouteButton);

        JButton addMailDeliveryButton = new JButton("Add mail delivery");
        addMailDeliveryButton.addActionListener(e -> new MailDialog(Home.this, simulation));
        buttonPanel.add(addMailDeliveryButton);

        JButton updateTransportCostButton = new JButton("Update transport cost");
        updateTransportCostButton.addActionListener(e -> new TransportRouteUpdateTable(Home.this, simulation));
        buttonPanel.add(updateTransportCostButton);

        JButton viewBusinessFiguresButton = new JButton("View business figures");
        viewBusinessFiguresButton.addActionListener(e -> new BusinessFigures(simulation));
        buttonPanel.add(viewBusinessFiguresButton);

        JButton exitButton = new JButton("Save and exit");
        exitButton.addActionListener(e -> save(true));
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.EAST);
    }

    private void save(boolean exit) {
        try {
            SimulationXML.writeSimulation(simulation, new PrintStream("log.xml"));
            if(exit) {
                System.exit(0);
            }
        } catch (XMLException | FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not open the log file for writing.", "Error saving", JOptionPane.ERROR_MESSAGE);
        }
    }
}
