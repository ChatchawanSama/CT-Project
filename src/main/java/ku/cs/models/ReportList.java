package ku.cs.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReportList {
    private final List<Report> reports;

    public ReportList() {
        reports = new ArrayList<>();
    }

    public void addReport(Report report) {
        reports.add(report);
    }

    public List<Report> getReports() {
        return reports;
    }

    public List<Report> getSortedByTime() {
        List<Report> temp = reports;
        temp.sort(new Comparator<Report>() {
            @Override
            public int compare(Report o1, Report o2) {
                if (o1.getTime().isBefore(o2.getTime())) {
                    return 1;
                }
                if (o1.getTime().isAfter(o2.getTime())) {
                    return -1;
                }
                return 0;
            }
        });
        return temp;
    }

    public String toCsv() {
        String result = "";
        for (Report report : this.reports) {
            result += report.toCsv() + "\n";
        }
        return result;
    }
}
