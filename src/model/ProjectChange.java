package model;

public class ProjectChange {

    private String projectName;
    private String description;
    private String reason;
    private String implementationDate;

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
                && implementationDate != null && !implementationDate.isBlank();
    }
}