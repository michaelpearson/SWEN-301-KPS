package kps.business.interfaces;

import kps.xml.objects.CalculatedRoute;

/**
 * Interface which defines the route calculator
 */
public interface IRouteCalculator {
    /**
     * Call this method to calculate the route.
     * @return The calculated route
     */
    CalculatedRoute buildCalculatedRoute();
}