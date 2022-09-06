package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Report {
    private final String target;
    private final Account reporter;
    private final String subject;
    private final String detail;
    private final LocalDateTime time;

    public Report(Reportable target, Account account, String subject, String detail, LocalDateTime time) {
        this.target = target.toReport();
        this.reporter = account;
        this.subject = subject;
        this.detail = detail;
        this.time = time;
    }

    public String getTarget() {
        return target;
    }

    public Account getReporter() {
        return reporter;
    }

    public String getSubject() {
        return subject;
    }

    public String getDetail() {
        return detail;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String toCsv() {
        return target + ", \"" + reporter.getUsername() + "\", \"" + subject + "\", \"" + detail + "\", \"" + time + "\"";
    }

    @Override
    public String toString() {
        String[] data = target.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        return data[0].trim().replaceAll("^\"|\"$", "") +
                " [" + this.time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "]";
    }
}
