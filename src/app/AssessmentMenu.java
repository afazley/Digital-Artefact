package app;

import java.util.Scanner;

import model.ImpactAssessment;
import model.UserRole;

public class AssessmentMenu {

    private final Scanner scanner;
    private final AssessmentController controller;
    private boolean running;

    public AssessmentMenu() {
        scanner = new Scanner(System.in);
        controller = new AssessmentController(scanner);
        running = true;
    }

    public void start() {
        controller.setCurrentRole(selectRole());

        while (running) {
            showMenu();

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            handleChoice(choice);
        }

        scanner.close();
    }

    private void showMenu() {
        System.out.println("\n===============================");
        System.out.println(" IMPACT ASSESSMENT MANAGEMENT");
        System.out.println("===============================");
        System.out.println("Current role: " + controller.getCurrentRole());

        ImpactAssessment assessment =
                controller.getSelectedAssessment();

        if (assessment != null) {
            System.out.println(
                    "Selected project: "
                    + assessment.getProjectChange().getProjectName()
            );
            System.out.println("Status: " + assessment.getStatus());
        } else {
            System.out.println("Selected project: None");
        }

        System.out.println("\n1. Create new assessment");
        System.out.println("2. View risks");
        System.out.println("3. Add risk");
        System.out.println("4. Add stakeholder");
        System.out.println("5. Check missing information");
        System.out.println("6. Generate report");
        System.out.println("7. Exit");
        System.out.println("8. View stakeholders");
        System.out.println("9. View all assessments");
        System.out.println("10. Select assessment");
        System.out.println("11. Switch user role");
        System.out.println("12. Start / Resume assessment");
        System.out.println("13. Submit assessment for approval");
        System.out.println("14. Review assessment (Manager only)");
    }

    private void handleChoice(String choice) {
        switch (choice) {
            case "1":
                controller.createAssessment();
                break;
            case "2":
                controller.viewRisks();
                break;
            case "3":
                controller.addRisk();
                break;
            case "4":
                controller.addStakeholder();
                break;
            case "5":
                controller.checkMissingInformation();
                break;
            case "6":
                controller.generateReport();
                break;
            case "7":
                System.out.println("Goodbye!");
                running = false;
                break;
            case "8":
                controller.viewStakeholders();
                break;
            case "9":
                controller.viewAllAssessments();
                break;
            case "10":
                controller.selectAssessment();
                break;
            case "11":
                controller.setCurrentRole(selectRole());
                break;
            case "12":
                controller.startAssessment();
                break;
            case "13":
                controller.submitAssessment();
                break;
            case "14":
                controller.reviewAssessment();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private UserRole selectRole() {
        while (true) {
            System.out.println("\n--- SELECT USER ROLE ---");
            System.out.println("1. Employee");
            System.out.println("2. Senior Manager");
            System.out.print("Select your role: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("Employee mode activated.");
                    return UserRole.EMPLOYEE;
                case "2":
                    System.out.println("Senior Manager mode activated.");
                    return UserRole.MANAGER;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}