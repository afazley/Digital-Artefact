package model;

public abstract class AssessmentParticipant {

    private String name;
    private String role;

    public AssessmentParticipant(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public abstract String getParticipantType();

    public void displayDetails() {
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);
        System.out.println("Type: " + getParticipantType());
    }
}