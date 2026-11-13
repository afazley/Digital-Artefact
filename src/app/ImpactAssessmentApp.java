
package app;

import java.util.Scanner;
import model.ImpactAssessment;
import model.ProjectChange;
import model.Risk;
import model.Stakeholder;
import report.ReportGenerator;
import service.AssessmentService;

public class ImpactAssessmentApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AssessmentService service = new AssessmentService();

        ImpactAssessment assessment = new ImpactAssessment();

        boolean running = true;

        while (running) {

            System.out.println("\n===============================");
            System.out.println("   IMPACT ASSESSMENT GENERATOR");
            System.out.println("===============================");
            System.out.println("1. Create new assessment");
            System.out.println("2. View risks");
            System.out.println("3. Add risk");
            System.out.println("4. Add stakeholder");
            System.out.println("5. Check missing information");
            System.out.println("6. Generate report");
            System.out.println("7. Exit");
            System.out.println("8. View stakeholders");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    System.out.println("\n--- CREATE NEW ASSESSMENT ---");

                    System.out.print("Enter project name: ");
                    String projectName = scanner.nextLine();

                    System.out.print("Describe the proposed change: ");
                    String description = scanner.nextLine();

                    System.out.print("Why is this change needed? ");
                    String reason = scanner.nextLine();

                    System.out.print("Enter proposed implementation date: ");
                    String date = scanner.nextLine();

                    assessment = service.createAssessment(
                        projectName, description, reason, date
                    );

                    System.out.println("\nAssessment details saved!");
                    break;

                case "2":
                    viewRisks(assessment);
                    break;

                case "3":
                    Risk risk = addRisk(scanner);
                    service.addRisk(assessment, risk);
                    break;

                case "4":
                    Stakeholder stakeholder = addStakeholder(scanner);
                    service.addStakeholder(assessment, stakeholder);
                    break;

                case "5":
                    checkMissingInformation(assessment);
                    break;

                case "6":
                    generateReport(assessment);
                    break;

                case "7":
                    System.out.println("Goodbye!");
                    running = false;
                    break;

                case "8":
                    viewStakeholders(assessment);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    public static Risk addRisk(Scanner scanner) {

        System.out.println("\n--- ADD NEW RISK ---");

        System.out.print("Enter risk description: ");
        String description = scanner.nextLine();

        int probability = 0;

        while (probability < 1 || probability > 5) {
            System.out.print("Enter probability (1-5): ");

            try {
                probability = Integer.parseInt(scanner.nextLine());

                if (probability < 1 || probability > 5) {
                    System.out.println("Enter a number between 1 and 5.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a whole number.");
            }
        }

        int impact = 0;

        while (impact < 1 || impact > 5) {
            System.out.print("Enter impact (1-5): ");

            try {
                impact = Integer.parseInt(scanner.nextLine());

                if (impact < 1 || impact > 5) {
                    System.out.println("Enter a number between 1 and 5.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a whole number.");
            }
        }

        System.out.print("Enter mitigation: ");
        String mitigation = scanner.nextLine();

        Risk risk = new Risk(description, probability, impact, mitigation);

        System.out.println("Risk successfully saved!");
        System.out.println("Risk score: " + risk.calculateRiskScore());
        System.out.println("Risk level: " + risk.getRiskLevel());

        return risk;
    }

    public static Stakeholder addStakeholder(Scanner scanner) {

        System.out.println("\n--- ADD STAKEHOLDER ---");

        System.out.print("Enter stakeholder name: ");
        String name = scanner.nextLine();

        System.out.print("Enter their role: ");
        String role = scanner.nextLine();

        System.out.print("Enter their department: ");
        String department = scanner.nextLine();

        System.out.println("Stakeholder successfully saved!");

        return new Stakeholder(name, role, department);
    }

    public static void viewRisks(ImpactAssessment assessment) {

        System.out.println("\n--- SAVED RISKS ---");

        if (assessment.getRisks().isEmpty()) {
            System.out.println("No risks have been added yet.");
            return;
        }

        for (int i = 0; i < assessment.getRisks().size(); i++) {

            Risk risk = assessment.getRisks().get(i);

            System.out.println("\nRisk " + (i + 1));
            System.out.println("Description: " + risk.getDescription());
            System.out.println("Probability: " + risk.getProbability());
            System.out.println("Impact: " + risk.getImpact());
            System.out.println("Score: " + risk.calculateRiskScore());
            System.out.println("Level: " + risk.getRiskLevel());
            System.out.println("Mitigation: " + risk.getMitigation());
        }
    }

    public static void viewStakeholders(ImpactAssessment assessment) {

        System.out.println("\n--- SAVED STAKEHOLDERS ---");

        if (assessment.getStakeholders().isEmpty()) {
            System.out.println("No stakeholders have been added yet.");
            return;
        }

        for (int i = 0; i < assessment.getStakeholders().size(); i++) {

            Stakeholder stakeholder = assessment.getStakeholders().get(i);

            System.out.println("\nStakeholder " + (i + 1));
            stakeholder.displayDetails();
        }
    }

    public static void checkMissingInformation(ImpactAssessment assessment) {

        System.out.println("\n--- MISSING INFORMATION CHECK ---");

        ProjectChange change = assessment.getProjectChange();
        boolean missing = false;

        if (change == null) {
            System.out.println("- Project assessment has not been created.");
            missing = true;
        } else {

            if (!change.isComplete()) {
                System.out.println("- Some project details are missing.");
                missing = true;
            }
        }

        if (assessment.getRisks().isEmpty()) {
            System.out.println("- No risks have been added.");
            missing = true;
        }

        if (assessment.getStakeholders().isEmpty()) {
            System.out.println("- No stakeholders have been added.");
            missing = true;
        }

        if (!missing) {
            System.out.println("All required information has been provided.");
        }
    }

    public static void generateReport(ImpactAssessment assessment) {

        ProjectChange change = assessment.getProjectChange();

        if (change == null) {
            System.out.println("Please create an assessment before generating a report.");
            return;
        }

        ReportGenerator.generateReport(
            change.getProjectName(),
            change.getDescription(),
            change.getReason(),
            change.getImplementationDate(),
            assessment.getRisks(),
            assessment.getStakeholders()
        );
    }
}
