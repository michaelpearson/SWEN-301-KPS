package kps.gui.windows;

import kps.business.BusinessFiguresCalculator;
import kps.gui.models.BusinessEventsTableModel;
import kps.gui.models.MailEventsTableModel;
import kps.xml.objects.Simulation;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

class DecisionSupport extends JFrame {
    private final Simulation simulation;


    private JLabel totalRevenue;
    private JLabel totalExpenditure;
    private JLabel totalNumberOfEvents;
    private JLabel totalAmountOfMail;
    private JLabel averageDeliveryTime;
    private JButton criticalRoutes;

    DecisionSupport(Simulation simulation) {
        this.simulation = simulation;
        buildGui();
        setTitle("Decision Support");
        setResizable(true);
        setSize(1300, 500);

        setLocationRelativeTo(null);
        buildBusinessFigures(null, new Date());
        setVisible(true);
    }

    private void buildBusinessFigures(@Nullable Date dateFrom, @Nullable Date dateTo) {
        new JDialog(this, true) {
            {
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                GridLayout layout = new GridLayout(2, 1);
                layout.setVgap(10);
                panel.setLayout(layout);

                JProgressBar progress = new JProgressBar(0, 100);
                new BusinessFiguresCalculator(simulation, dateFrom, dateTo, data -> dataReady(data), p -> {
                    progress.setValue((int)(p * 100));
                    if(p == 1) {
                        this.dispose();
                    }
                });

                JLabel label = new JLabel("Calculating figures...");
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.CENTER);


                setTitle("Progress");
                setResizable(false);
                setLocationRelativeTo(null);
                setSize(500, 50);

                panel.add(label);
                panel.add(progress);

                add(panel);
                setVisible(true);
            }
        };
    }

    private void buildGui() {


        JPanel figuresPanel = new JPanel();
        GridLayout layout = new GridLayout(0, 2);
        layout.setVgap(10);
        layout.setHgap(10);
        figuresPanel.setLayout(layout);

        criticalRoutes = new JButton("View Critical Routes");
        criticalRoutes.addActionListener(e -> new CriticalRoutesWindow(simulation) );

        figuresPanel.add(new JLabel("Total revenue:"));
        figuresPanel.add(totalRevenue = new JLabel());
        figuresPanel.add(new JLabel("Total expenditure:"));
        figuresPanel.add(totalExpenditure = new JLabel());
        figuresPanel.add(new JLabel("Total number of events:"));
        figuresPanel.add(totalNumberOfEvents = new JLabel());
        figuresPanel.add(new JLabel("Total amount of mail:"));
        figuresPanel.add(totalAmountOfMail = new JLabel());
        figuresPanel.add(new JLabel("average delivery time:"));
        figuresPanel.add(averageDeliveryTime = new JLabel());
        figuresPanel.add(new JLabel("Critical routes:"));
        figuresPanel.add(criticalRoutes);

        figuresPanel.setPreferredSize(new Dimension(300, 200));



        JPanel figuresPanelOuter = new JPanel(new BorderLayout());
        figuresPanelOuter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        figuresPanelOuter.add(figuresPanel, BorderLayout.NORTH);




        BusinessEventsTableModel tableModel;
        JXTable businessEventsTable = new JXTable(tableModel = new MailEventsTableModel(simulation));

        businessEventsTable.getSelectionModel().addListSelectionListener(e -> {
            if(e.getValueIsAdjusting()) {
                return;
            }
            int rows[] = businessEventsTable.getSelectedRows();
            if(rows.length == 1) {
                buildBusinessFigures(null, tableModel.getBusinessEvent(rows[0]).getDate());
            } else if( rows.length > 1) {
                Date to = tableModel.getBusinessEvent(rows[0]).getDate();
                Date from = tableModel.getBusinessEvent(rows[rows.length - 1]).getDate();
                buildBusinessFigures(from, to);
            } else {
                buildBusinessFigures(null, null);
            }
        });

        businessEventsTable.setSize(500, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(businessEventsTable), BorderLayout.CENTER);
        panel.add(figuresPanelOuter, BorderLayout.EAST);
        add(panel);
    }

    private void dataReady(BusinessFiguresCalculator data) {
        totalAmountOfMail.setText(String.format("%d", data.getAmountOfMail()));
        totalRevenue.setText(String.format("%2.2f", data.getTotalRevenue()));
        totalExpenditure.setText(String.format("%2.2f", data.getTotalExpenditure()));
        totalNumberOfEvents.setText(String.format("%d", data.getTotalNumberOfEvents()));
        averageDeliveryTime.setText(String.format("%2.2f", data.getAverageDeliveryTime()));
        //criticalRoutes.setText(String.format("%d", data.getCriticalRoutes()));
    }
}
