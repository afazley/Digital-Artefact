
package app;

import java.util.Scanner;

import model.ImpactAssessment;
import model.UserRole;

public class AssessmentMenu {

    private final Scanner scanner;
    private final AssessmentController controller;
    private final AssessmentWizard wizard;
    private boolean running = true;

    public AssessmentMenu() {
        scanner = new Scanner(System.in);
        controller = new AssessmentController(scanner);
        wizard = new AssessmentWizard(scanner, controller);
    }

    public void start() {
        controller.setCurrentRole(selectRole());

        while (running) {
            showMenu();
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            handleChoice(choice);
        }

        scanner.close();
    }

    private void showMenu() {
        System.out.println("\n======================================");
        System.out.println("       IMPACT ASSESSMENT SYSTEM");
        System.out.println("======================================");
        System.out.println("Current role: "
                + controller.getCurrentRole());

        ImpactAssessment selected =
                controller.getSelectedAssessment();

        if (selected != null) {
            System.out.println("Selected project: "
                    + selected.getProjectChange().getProjectName());
            System.out.println("Status: " + selected.getStatus());
        }

        if (controller.getCurrentRole() == UserRole.EMPLOYEE) {
            System.out.println("\n1. Create a new assessment (guided)");
            System.out.println("2. Continue an existing assessment");
            System.out.println("3. View all assessments");
            System.out.println("4. Generate a report");
            System.out.println("5. Switch role");
        } else {
            System.out.println("\n1. Review assessments awaiting approval");
            System.out.println("2. View all assessments");
            System.out.println("3. Generate a report");
            System.out.println("4. Switch role");
        }

        System.out.println("0. Exit");
        System.out.println("======================================");
    }

    private void handleChoice(String choice) {
        if (choice.equals("0")) {
            System.out.println("Goodbye!");
            running = false;
            return;
        }

        if (controller.getCurrentRole() == UserRole.EMPLOYEE) {
            switch (choice) {
                case "1":
                    wizard.createGuidedAssessment();
                    break;
                case "2":
                    wizard.continueAssessment();
                    break;
                case "3":
                    controller.viewAllAssessments();
                    break;
                case "4":
                    wizard.chooseAndGenerateReport();
                    break;
                case "5":
                    controller.setCurrentRole(selectRole());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } else {
            switch (choice) {
                case "1":
                    wizard.reviewPendingAssessment();
                    break;
                case "2":
                    controller.viewAllAssessments();
                    break;
                case "3":
                    wizard.chooseAndGenerateReport();
                    break;
                case "4":
                    controller.setCurrentRole(selectRole());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private UserRole selectRole() {
        while (true) {
            System.out.println("\n--- SELECT ROLE ---");
            System.out.println("1. Employee");
            System.out.println("2. Manager");
            System.out.print("Choose a role: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                return UserRole.EMPLOYEE;
            }

            if (choice.equals("2")) {
                return UserRole.MANAGER;
            }

            System.out.println("Enter 1 or 2.");
        }
    }
}
