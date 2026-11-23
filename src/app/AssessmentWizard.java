
package app;

import java.util.Scanner;

import model.AssessmentStatus;
import model.ImpactAssessment;
import model.ProjectChange;

public class AssessmentWizard {

    private final Scanner scanner;
    private final AssessmentController controller;

    public AssessmentWizard(Scanner scanner,
                            AssessmentController controller) {
        this.scanner = scanner;
        this.controller = controller;
    }

    public void createGuidedAssessment() {
        System.out.println("\nSTEP 1 OF 4: PROJECT DETAILS");
        controller.createAssessment();
        controller.startAssessment();

        System.out.println("\nSTEP 2 OF 4: RISKS");
        addRisks();

        System.out.println("\nSTEP 3 OF 4: STAKEHOLDERS");
        addStakeholders();

        System.out.println("\nSTEP 4 OF 4: REVIEW");
        finishAssessment();
    }

    public void continueAssessment() {
        if (!controller.selectAssessment()) {
            return;
        }

        ImpactAssessment assessment =
                controller.getSelectedAssessment();

        AssessmentStatus status = assessment.getStatus();

        if (status == AssessmentStatus.DRAFT
                || status == AssessmentStatus.CHANGES_REQUIRED) {
            controller.startAssessment();
        }

        if (!assessment.isEditable()) {
            System.out.println("This assessment is "
                    + assessment.getStatus()
                    + " and cannot be edited.");

            showAssessmentSummary();
            return;
        }

        finishAssessment();
    }

    private void addRisks() {
        do {
            controller.addRisk();
        } while (askYesNo(
                "Would you like to add another risk? (Y/N): "
        ));
    }

    private void addStakeholders() {
        do {
            controller.addStakeholder();
        } while (askYesNo(
                "Would you like to add another stakeholder? (Y/N): "
        ));
    }

    private boolean askYesNo(String question) {
        while (true) {
            System.out.print(question);
            String answer = scanner.nextLine().trim();

            if (answer.equalsIgnoreCase("Y")) {
                return true;
            }

            if (answer.equalsIgnoreCase("N")) {
                return false;
            }

            System.out.println("Please enter Y or N.");
        }
    }

    private void showAssessmentSummary() {
        ImpactAssessment assessment =
                controller.getSelectedAssessment();

        ProjectChange change = assessment.getProjectChange();

        System.out.println("\n--- ASSESSMENT SUMMARY ---");
        System.out.println("Project: " + change.getProjectName());
        System.out.println("Proposed change: "
                + change.getDescription());
        System.out.println("Reason: " + change.getReason());
        System.out.println("Implementation date: "
                + change.getImplementationDate());
        System.out.println("Status: " + assessment.getStatus());

        controller.viewRisks();
        controller.viewStakeholders();
        controller.checkMissingInformation();
    }

    private void finishAssessment() {
        while (true) {
            showAssessmentSummary();

            System.out.println("\nWhat would you like to do next?");
            System.out.println("1. Submit for manager approval");
            System.out.println("2. Add another risk");
            System.out.println("3. Add another stakeholder");
            System.out.println("0. Save and return to the main menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    controller.submitAssessment();

                    if (controller.getSelectedAssessment().getStatus()
                            == AssessmentStatus.PENDING_APPROVAL) {
                        return;
                    }
                    break;

                case "2":
                    addRisks();
                    break;

                case "3":
                    addStakeholders();
                    break;

                case "0":
                    System.out.println(
                            "Assessment kept in this session. "
                            + "You can continue it from the main menu."
                    );
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }

    public void reviewPendingAssessment() {
        if (!controller.selectPendingAssessment()) {
            return;
        }

        showAssessmentSummary();

        if (askYesNo("Make a review decision now? (Y/N): ")) {
            controller.reviewAssessment();
        }
    }

    public void chooseAndGenerateReport() {
        if (controller.selectAssessment()) {
            controller.generateReport();
        }
    }
}
