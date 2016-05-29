package kps.gui.windows;

import kps.business.BusinessFiguresCalculator;
import kps.xml.objects.Simulation;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class BusinessFigures extends JFrame {
    private final Simulation simulation;


    private JLabel totalRevenue;
    private JLabel totalExpenditure;
    private JLabel totalNumberOfEvents;
    private JLabel totalAmountOfMail;
    private JLabel averageDeliveryTime;
    private JLabel criticalRoutes;

    public BusinessFigures(Simulation simulation) {
        this.simulation = simulation;
        buildGui();
        pack();
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
        setResizable(false);

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

        outerPanel.add(figuresPanel, BorderLayout.CENTER);

        JPanel datePanel = new JPanel();
        GridLayout dateLayout = new GridLayout(2, 1);
        dateLayout.setVgap(10);
        datePanel.setLayout(dateLayout);


        JXDatePicker datePicker = new JXDatePicker(new Date());
        datePicker.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        datePicker.addActionListener(e -> buildBusinessFigures(datePicker.getDate()));
        datePanel.add(new JLabel("Select the date to calculate the business figures up to:"));
        datePanel.add(datePicker);
        outerPanel.add(datePanel, BorderLayout.SOUTH);
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
