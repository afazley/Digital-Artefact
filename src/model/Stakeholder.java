package model;

public class Stakeholder extends AssessmentParticipant {

    private String department;

    public Stakeholder(String name, String role, String department) {
        super(name, role);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String getParticipantType() {
        return "Project Stakeholder";
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Department: " + department);
    }
}