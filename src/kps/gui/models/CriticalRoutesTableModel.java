package kps.gui.models;

import kps.xml.objects.*;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CriticalRoutesTableModel extends AbstractTableModel {
    private static final String FROM = "From";
    private static final String TO = "To";
    private static final String PRIORITY = "Priority";
    private static final String AVERAGE_REVENUE = "Average Revenue";
    private static final String AVERAGE_EXPENDITURE = "Average Expenditure";
    private final Simulation simulation;
    private List<RouteGroup> mailRouteGroup = new ArrayList<>();
    private Map<String, FieldGetter> tableColumns = new LinkedHashMap<>();

    private static class RouteGroup {
        private @NotNull String from;
        private @NotNull String to;
        private @NotNull Priority priority;
        private @NotNull List<Mail> mail;

        RouteGroup(@NotNull Mail m) {
            this.from = m.getFrom();
            this.to = m.getTo();
            this.priority = m.getPriority();
            this.mail = new ArrayList<>();
            this.mail.add(m);
        }

        boolean addMailEvent(@NotNull Mail m) {
            if(to.equals(m.getTo()) && from.equals(m.getFrom()) && priority.equals(m.getPriority())) {
                mail.add(m);
                return true;
            }
            return false;
        }
    }

    private interface FieldGetter {
        String getField(int row);
    }

    public CriticalRoutesTableModel(@NotNull Simulation simulation) {
        this.simulation = simulation;
        updateTable();

        tableColumns.put(FROM, row -> mailRouteGroup.get(row).from);
        tableColumns.put(TO, row -> mailRouteGroup.get(row).to);
        tableColumns.put(PRIORITY, row -> mailRouteGroup.get(row).priority.name());
        tableColumns.put(AVERAGE_EXPENDITURE, row -> String.format("%.2f", calculateAverageExpenditure(mailRouteGroup.get(row))));
        tableColumns.put(AVERAGE_REVENUE, row -> String.format("%.2f", calculateAverageRevenue(mailRouteGroup.get(row))));
    }

    private double calculateAverageExpenditure(@NotNull RouteGroup rg){
        double expenditure = 0;
        int total = 0;

        for (Mail m : rg.mail) {
            expenditure += m.getExpenditure();
            total++;
        }
        return expenditure / Math.max(1, total);
    }

    private double calculateAverageRevenue(@NotNull RouteGroup rg){
        double revenue = 0;
        int total = 0;

        for (Mail m : rg.mail) {
            revenue += m.getRevenue();
            total++;
        }
        return revenue / Math.max(1, total);
    }

    private int criticalFitness(@NotNull RouteGroup l, @NotNull RouteGroup r){
        double lAvg = calculateAverageRevenue(l) - calculateAverageExpenditure(l);
        double rAvg = calculateAverageRevenue(r) - calculateAverageExpenditure(r);
        return (int)((lAvg - rAvg) * 100);
    }
    
    private void updateTable() {
        List<Mail> mail = simulation.getMail();
        mailRouteGroup.clear();

        mail.stream().forEach(m -> {
            if(!(mailRouteGroup.stream().anyMatch(mrg -> mrg.addMailEvent(m)))) {
                mailRouteGroup.add(new RouteGroup(m));
            }
        });
        mailRouteGroup = mailRouteGroup.stream().filter(m -> calculateAverageExpenditure(m) > calculateAverageRevenue(m)).collect(Collectors.toList());
        mailRouteGroup.sort(this::criticalFitness);
    }

    @Override
    public int getRowCount() {
        return mailRouteGroup.size();
    }

    @Override
    public int getColumnCount() {
        return tableColumns.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return new LinkedList<>(tableColumns.entrySet()).get(columnIndex).getKey();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return tableColumns.get(getColumnName(columnIndex)).getField(rowIndex);
    }
}
