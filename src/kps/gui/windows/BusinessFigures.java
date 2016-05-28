package kps.gui.windows;

import kps.business.BusinessFiguresCalculator;
import kps.xml.objects.Simulation;

import javax.swing.*;
import java.awt.*;

public class BusinessFigures extends JFrame {
    private final Simulation simulation;


    private JLabel totalRevenue;
    private JLabel totalExpensiture;
    private JLabel totalNumberOfEvents;
    private JLabel totalAmountOfMail;
    private JLabel averageDeliveryTime;
    private JLabel criticalRoutes;

    public BusinessFigures(Simulation simulation) {
        this.simulation = simulation;
        buildGui();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        new JDialog(this, true) {
            {
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                GridLayout layout = new GridLayout(2, 1);
                layout.setVgap(10);
                panel.setLayout(layout);

                JProgressBar progress = new JProgressBar(0, 100);
                new BusinessFiguresCalculator(simulation, null, (s) -> dataReady(s), p -> {
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
        setLayout(new BorderLayout());
        JPanel figuresPanel = new JPanel();
        figuresPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridLayout layout = new GridLayout(0, 2);
        layout.setVgap(10);
        layout.setHgap(10);
        figuresPanel.setLayout(layout);
        setResizable(false);

        figuresPanel.add(new JLabel("Total revenue:"));
        figuresPanel.add(totalRevenue = new JLabel());
        figuresPanel.add(new JLabel("Total expenditure:"));
        figuresPanel.add(totalExpensiture = new JLabel());
        figuresPanel.add(new JLabel("Total number of events:"));
        figuresPanel.add(totalNumberOfEvents = new JLabel());
        figuresPanel.add(new JLabel("Total amount of mail:"));
        figuresPanel.add(totalAmountOfMail = new JLabel());
        figuresPanel.add(new JLabel("average delivery time:"));
        figuresPanel.add(averageDeliveryTime = new JLabel());
        figuresPanel.add(new JLabel("Critical routes:"));
        figuresPanel.add(criticalRoutes = new JLabel());
        add(figuresPanel, BorderLayout.CENTER);
    }

    private void dataReady(BusinessFiguresCalculator data) {
        totalAmountOfMail.setText(String.format("%d", data.getAmountOfMail()));
        totalRevenue.setText(String.format("%2.2f", data.getTotalRevenue()));
        totalExpensiture.setText(String.format("%2.2f", data.getTotalExpenditure()));
        totalNumberOfEvents.setText(String.format("%d", data.getTotalNumberOfEvents()));
        averageDeliveryTime.setText(String.format("%2.2f", data.getAverageDeliveryTime()));
        //criticalRoutes.setText(String.format("%d", data.getCriticalRoutes()));
    }
}
