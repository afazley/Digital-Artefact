package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Risk;
import model.Stakeholder;

import model.AssessmentStatus;
import model.ImpactAssessment;
import model.ProjectChange;
import model.UserRole;

import report.ReportGenerator;
import service.AssessmentService;

public class AssessmentController {

    private final Scanner scanner;
    private final AssessmentService service;
    private final AssessmentInput input;
    private final AssessmentDisplay display;

    private final List<ImpactAssessment> assessments;

    private ImpactAssessment selectedAssessment;
    private UserRole currentRole;

    public AssessmentController(Scanner scanner) {
        this.scanner = scanner;
        service = new AssessmentService();
        input = new AssessmentInput(scanner, service);
        display = new AssessmentDisplay(service);

        assessments = new ArrayList<>();
        selectedAssessment = null;
    }

    public UserRole getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(UserRole role) {
        currentRole = role;
    }

    public ImpactAssessment getSelectedAssessment() {
        return selectedAssessment;
    }

    private boolean hasSelectedAssessment() {
        if (selectedAssessment == null) {
            System.out.println(
                    "Please create or select an assessment first."
            );
            return false;
        }
        return true;
    }

    private boolean hasEditableAssessment() {
    if (!hasSelectedAssessment()) {
        return false;
    }

    if (!selectedAssessment.isEditable()) {
        System.out.println(
                "Assessment cannot be edited while its status is "
                + selectedAssessment.getStatus() + "."
        );
        return false;
    }

    return true;
    }

    public void createAssessment() {
        selectedAssessment = input.createAssessment();
        service.addAssessment(assessments, selectedAssessment);

        System.out.println("\nAssessment created successfully!");
        System.out.println("Assessment ID: " + assessments.size());
    }

    public void viewRisks() {
        if (hasSelectedAssessment()) {
            display.viewRisks(selectedAssessment);
        }
    }

    public void addRisk() {
        if (!hasEditableAssessment()) {
            return;
        }

        Risk risk = input.addRisk();
        service.addRisk(selectedAssessment, risk);

        System.out.println("Risk successfully saved!");
        System.out.println("Risk score: " + risk.calculateRiskScore());
        System.out.println("Risk level: " + risk.getRiskLevel());
    }

    public void addStakeholder() {
        if (!hasEditableAssessment()) {
            return;
        }

        Stakeholder stakeholder = input.addStakeholder();
        service.addStakeholder(selectedAssessment, stakeholder);

        System.out.println("Stakeholder successfully saved!");
    }

    public void checkMissingInformation() {
        if (hasSelectedAssessment()) {
            display.checkMissingInformation(selectedAssessment);
        }
    }

    public void generateReport() {
        if (!hasSelectedAssessment()) {
            return;
        }

        ProjectChange change = selectedAssessment.getProjectChange();

        if (change == null) {
            System.out.println("No project details available.");
            return;
        }

        ReportGenerator.generateReport(
                change.getProjectName(),
                change.getDescription(),
                change.getReason(),
                change.getImplementationDate(),
                selectedAssessment.getRisks(),
                selectedAssessment.getStakeholders()
        );
    }

    public void viewStakeholders() {
        if (hasSelectedAssessment()) {
            display.viewStakeholders(selectedAssessment);
        }
    }

    public void viewAllAssessments() {
        display.viewAllAssessments(assessments);
    }

    public void selectAssessment() {
        if (assessments.isEmpty()) {
            System.out.println("No assessments available.");
            return;
        }

        viewAllAssessments();

        System.out.print("\nEnter assessment number to select: ");

        try {
            int number = Integer.parseInt(
                    scanner.nextLine().trim()
            );

            if (number >= 1 && number <= assessments.size()) {
                selectedAssessment = assessments.get(number - 1);

                System.out.println(
                        "Selected assessment: "
                        + selectedAssessment.getProjectChange()
                                .getProjectName()
                );
            } else {
                System.out.println("Invalid assessment number.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid whole number.");
        }
    }

    public void startAssessment() {
        if (!hasSelectedAssessment()) {
            return;
        }

        if (service.startAssessment(selectedAssessment)) {
            System.out.println("Assessment is now IN PROGRESS.");
        } else {
            System.out.println(
                    "Cannot start assessment from status: "
                    + selectedAssessment.getStatus()
            );
        }
    }

    public void submitAssessment() {
        if (!hasSelectedAssessment()) {
            return;
        }

        if (service.submitAssessment(selectedAssessment)) {
            System.out.println("Assessment submitted for approval!");
        } else {
            System.out.println(
                    "Submission failed. Assessment must be IN PROGRESS "
                    + "and contain all required information."
            );
        }
    }

    public void reviewAssessment() {
        if (currentRole != UserRole.MANAGER) {
            System.out.println(
                    "Access denied. Only managers can review assessments."
            );
            return;
        }

        if (!hasSelectedAssessment()) {
            return;
        }

        if (selectedAssessment.getStatus()
                != AssessmentStatus.PENDING_APPROVAL) {

            System.out.println(
                    "Assessment must be PENDING APPROVAL before review."
            );
            return;
        }

        System.out.println("\n--- MANAGER REVIEW ---");
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        System.out.println("3. Request changes");
        System.out.print("Enter decision: ");

        String choice = scanner.nextLine().trim();
        AssessmentStatus decision;

        switch (choice) {
            case "1":
                decision = AssessmentStatus.APPROVED;
                break;
            case "2":
                decision = AssessmentStatus.REJECTED;
                break;
            case "3":
                decision = AssessmentStatus.CHANGES_REQUIRED;
                break;
            default:
                System.out.println("Invalid review decision.");
                return;
        }

        if (service.reviewAssessment(
                selectedAssessment, decision, currentRole)) {            System.out.println(
                        "Assessment status updated to: "
                        + selectedAssessment.getStatus()
                );
            }
    }
}