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

        System.out.println("\n======================================");
        System.out.println("       IMPACT ASSESSMENT SYSTEM");
        System.out.println("======================================");

        System.out.println("Current role: " + controller.getCurrentRole());

        ImpactAssessment assessment = controller.getSelectedAssessment();

        if (assessment != null) {
            System.out.println("Selected project: "
                    + assessment.getProjectChange().getProjectName());
            System.out.println("Status: " + assessment.getStatus());
        } else {
            System.out.println("Selected project: None");
        }

        System.out.println("--------------------------------------");
        System.out.println("       ASSESSMENT MANAGEMENT");
        System.out.println("--------------------------------------");
        System.out.println("[1]  Create new assessment");
        System.out.println("[2]  View all assessments");
        System.out.println("[3]  Select assessment");

        System.out.println("\n       RISKS & STAKEHOLDERS");
        System.out.println("--------------------------------------");
        System.out.println("[4]  Add risk");
        System.out.println("[5]  View prioritised risks");
        System.out.println("[6]  Add stakeholder");
        System.out.println("[7]  View stakeholders");

        System.out.println("\n       REVIEW & APPROVAL");
        System.out.println("--------------------------------------");
        System.out.println("[8]  Check missing information");
        System.out.println("[9]  Start / Resume assessment");
        System.out.println("[10] Submit for approval");
        System.out.println("[11] Manager review");

        System.out.println("\n       SYSTEM");
        System.out.println("--------------------------------------");
        System.out.println("[12] Generate report");
        System.out.println("[13] Switch user role");
        System.out.println("[0]  Exit application");

        System.out.println("======================================");
    }

    private void handleChoice(String choice) {

        switch (choice) {

            case "1":
                controller.createAssessment();
                break;

            case "2":
                controller.viewAllAssessments();
                break;

            case "3":
                controller.selectAssessment();
                break;

            case "4":
                controller.addRisk();
                break;

            case "5":
                controller.viewRisks();
                break;

            case "6":
                controller.addStakeholder();
                break;

            case "7":
                controller.viewStakeholders();
                break;

            case "8":
                controller.checkMissingInformation();
                break;

            case "9":
                controller.startAssessment();
                break;

            case "10":
                controller.submitAssessment();
                break;

            case "11":
                controller.reviewAssessment();
                break;

            case "12":
                controller.generateReport();
                break;

            case "13":
                controller.setCurrentRole(selectRole());
                break;

            case "0":
                System.out.println("\nThank you for using the system!");
                running = false;
                break;

            default:
                System.out.println("Invalid option. Please try again.");
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