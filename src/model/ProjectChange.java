package model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class ProjectChange {

    private String projectName;
    private String description;
    private String reason;
    private String implementationDate;

    private static final DateTimeFormatter DATE_FORMAT =
        DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

    public static boolean isValidImplementationDate(String date) {
        if (date == null || date.isBlank()) {
            return false;
        }

        try {
            LocalDate.parse(date.trim(), DATE_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public ProjectChange(String projectName, String description,
                         String reason, String implementationDate) {
        this.projectName = projectName;
        this.description = description;
        this.reason = reason;
        this.implementationDate = implementationDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getDescription() {
        return description;
    }

    public String getReason() {
        return reason;
    }

    public String getImplementationDate() {
        return implementationDate;
    }

    public boolean isComplete() {
        return projectName != null && !projectName.isBlank()
                && description != null && !description.isBlank()
                && reason != null && !reason.isBlank()
                && isValidImplementationDate(implementationDate);
    }
}