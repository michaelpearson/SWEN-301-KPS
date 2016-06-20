package kps.xml.objects.abstracts;

/**
 * Used to define a business event as a pricable event for the {@link kps.business.BusinessFiguresCalculator}
 */
public interface Priceable {
    double getExpenditure();
    double getRevenue();
}
