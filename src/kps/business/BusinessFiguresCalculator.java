package kps.business;

import kps.xml.objects.Mail;
import kps.xml.objects.Route;
import kps.xml.objects.Simulation;
import kps.xml.objects.abstracts.BusinessEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class will calculate business figures as per the project spec.
 */
public class BusinessFiguresCalculator {
    @NotNull private final Simulation simulation;
    @NotNull private final Thread calculator;
    private Date dateFrom;
    @Nullable private final DataReady callback;
    @Nullable private final ProgressCallback progressCallback;
    @Nullable private final Date dateTo;
    private double totalRevenue = 0;
    private double totalExpenditure = 0;
    private int totalNumberOfEvents = 0;
    private int amountOfMail = 0;
    private int volumeOfMail = 0;
    private int weightOfMail = 0;
    private int sumDeliveryTime = 0;
    @NotNull private List<Route> criticalRoutes = new ArrayList<>();

    /**
     * Interface for the completion callback
     */
    public interface DataReady {
        void ready(BusinessFiguresCalculator source);
    }

    /**
     * Interface for letting the caller know that progress is happening
     */
    public interface ProgressCallback {
        void progress(double percent);
    }

    /**
     * Main constructor that creates a single use route calculator
     * @param simulation the main simulation object to base the route calculation on
     * @param dateTo the date that the calculator should treat as "now", all newer events will be filtered from the working set
     * @param dateFrom the start date of the calculations
     * @param callback the callback which will be called when the data is ready
     * @param progressCallback callback for when progress has been made in the calculation.
     */
    public BusinessFiguresCalculator(@NotNull Simulation simulation, @Nullable Date dateFrom , @Nullable Date dateTo, @Nullable DataReady callback, @Nullable ProgressCallback progressCallback) {
        this.simulation = simulation;
        this.dateFrom = dateFrom;
        this.callback = callback;
        this.dateTo = dateTo;
        this.progressCallback = progressCallback;

        calculator = new Thread() {
            @Override
            public void run() {
                calculateTotals();
                if(progressCallback != null) {
                    progressCallback.progress(1);
                }
            }
        };
        calculator.start();
    }

    /**
     * Wait on the calculation thread until the calculation completes.
     * @return the result of the calculation or null if it was interrupted
     * @throws InterruptedException
     */
    @Nullable public BusinessFiguresCalculator waitForResult() throws InterruptedException {
        calculator.wait();
        return this;
    }

    /**
     * Private method that does the calculation
     */
    private void calculateTotals() {
        List<BusinessEvent> businessEvents = simulation.getAllBusinessEvents();
        if(dateFrom != null) {
            businessEvents = businessEvents.stream().filter(be -> be.getDate().compareTo(dateFrom) >= 0).collect(Collectors.toList());
        }
        if(dateTo != null) {
            businessEvents = businessEvents.stream().filter(be -> be.getDate().compareTo(dateTo) <= 0).collect(Collectors.toList());
        }

        businessEvents.sort((l, r) -> l.getDate().compareTo(r.getDate()));
        int a = 0;
        for(BusinessEvent businessEvent : businessEvents) {
            if(Thread.interrupted()) {
                return;
            }
            if(dateTo != null && businessEvent.getDate().after(dateTo)) {
                fireCallback();
                return;
            }
            a++;
            if(progressCallback != null) {
                progressCallback.progress((double)a / businessEvents.size());
            }
            totalNumberOfEvents++;
            if(Mail.class.isAssignableFrom(businessEvent.getClass())) {
                amountOfMail++;
                volumeOfMail += ((Mail)businessEvent).getVolume();
                weightOfMail += ((Mail)businessEvent).getWeight();
                sumDeliveryTime += ((Mail)businessEvent).getDeliveryTime();
            }
            totalRevenue += businessEvent.getRevenue();
            totalExpenditure += businessEvent.getExpenditure();
        }
        fireCallback();
    }

    /**
     * Fires the callback in "callback"
     */
    private void fireCallback() {
        if(callback != null) {
            callback.ready(this);
        }
    }

    @NotNull public Thread getCalculator() {
        return calculator;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getTotalExpenditure() {
        return totalExpenditure;
    }

    public int getTotalNumberOfEvents() {
        return totalNumberOfEvents;
    }

    public int getAmountOfMail() {
        return amountOfMail;
    }

    public int getVolumeOfMail() {
        return volumeOfMail;
    }

    public int getWeightOfMail() {
        return weightOfMail;
    }

    public double getAverageDeliveryTime() {
        return (double) sumDeliveryTime / (double)amountOfMail;
    }

    @NotNull public List<Route> getCriticalRoutes() {
        return criticalRoutes;
    }
}
