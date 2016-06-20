package kps.gui.models;

import kps.xml.objects.Mail;
import kps.xml.objects.Simulation;
import kps.xml.objects.abstracts.BusinessEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is the model for the decision support table. It adapts to the simulation object that is passed in.
 */
public class MailEventsTableModel extends BusinessEventsTableModel {

    /**
     * Main constructor which sets up the columns
     * @param simulation the simulation object which the table will display.
     */
    public MailEventsTableModel(Simulation simulation) {
        super(simulation);
    }

    @Override
    protected List<BusinessEvent> getBusinessEvents() {
        List<BusinessEvent> businessEvents = super.getBusinessEvents();
        return businessEvents.stream().filter(be -> be.getClass().equals(Mail.class)).collect(Collectors.toList());
    }
}
