package kps.gui.windows;

import kps.business.BusinessFiguresCalculator;
import kps.gui.models.BusinessEventsTableModel;
import kps.xml.objects.Simulation;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Date;

class DecisionSupport extends JFrame {
    private final Simulation simulation;


    private JLabel totalRevenue;
    private JLabel totalExpenditure;
    private JLabel totalNumberOfEvents;
    private JLabel totalAmountOfMail;
    private JLabel averageDeliveryTime;
    private JLabel criticalRoutes;

    DecisionSupport(Simulation simulation) {
        this.simulation = simulation;
        buildGui();
        setTitle("Decision Support");
        setResizable(true);
        setSize(1300, 500);

        setLocationRelativeTo(null);
        buildBusinessFigures(new Date());
        setVisible(true);
    }

    private void buildBusinessFigures(Date date) {
        new JDialog(this, true) {
            {
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                GridLayout layout = new GridLayout(2, 1);
                layout.setVgap(10);
                panel.setLayout(layout);

                JProgressBar progress = new JProgressBar(0, 100);
                new BusinessFiguresCalculator(simulation, date, (s) -> dataReady(s), p -> {
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
                setSize(400, 50);

                panel.add(label);
                panel.add(progress);

                add(panel);
                setVisible(true);
            }
        };
    }

    private void buildGui() {
        JPanel outerPanel = new JPanel();
        outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outerPanel.setLayout(new BorderLayout());

        JPanel figuresPanel = new JPanel();
        GridLayout layout = new GridLayout(0, 2);
        layout.setVgap(10);
        layout.setHgap(10);
        figuresPanel.setLayout(layout);

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
        figuresPanel.add(criticalRoutes = new JLabel());

        figuresPanel.setPreferredSize(new Dimension(300, 200));



        JPanel figuresPanelOuter = new JPanel(new BorderLayout());
        figuresPanelOuter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        figuresPanelOuter.add(figuresPanel, BorderLayout.NORTH);

        outerPanel.add(figuresPanelOuter, BorderLayout.EAST);


        JXTable businessEventsTable = new JXTable(new BusinessEventsTableModel(simulation));

        outerPanel.add(new JScrollPane(businessEventsTable), BorderLayout.CENTER);


        add(outerPanel);
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
