package app;

import java.util.Scanner;

import model.ImpactAssessment;
import model.Risk;
import model.Stakeholder;
import service.AssessmentService;

public class AssessmentInput {

    private final Scanner scanner;
    private final AssessmentService service;

    public AssessmentInput(Scanner scanner, AssessmentService service) {
        this.scanner = scanner;
        this.service = service;
    }

    public ImpactAssessment createAssessment() {
        System.out.println("\n--- CREATE NEW ASSESSMENT ---");

        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine();

        System.out.print("Describe the proposed change: ");
        String description = scanner.nextLine();

        System.out.print("Why is this change needed? ");
        String reason = scanner.nextLine();

        System.out.print("Enter proposed implementation date: ");
        String date = scanner.nextLine();

        return service.createAssessment(
                projectName, description, reason, date
        );
    }

    public Risk addRisk() {
        System.out.println("\n--- ADD NEW RISK ---");

        System.out.print("Enter risk description: ");
        String description = scanner.nextLine();

        int probability = readRiskRating("probability");
        int impact = readRiskRating("impact");

        System.out.print("Enter mitigation: ");
        String mitigation = scanner.nextLine();

        Risk risk = new Risk(
                description, probability, impact, mitigation
        );

        System.out.println("Risk successfully saved!");
        System.out.println("Risk score: " + risk.calculateRiskScore());
        System.out.println("Risk level: " + risk.getRiskLevel());

        return risk;
    }

    public Stakeholder addStakeholder() {
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

    private int readRiskRating(String ratingName) {
        while (true) {
            System.out.print("Enter " + ratingName + " (1-5): ");

            try {
                int rating = Integer.parseInt(
                        scanner.nextLine().trim()
                );

                if (rating >= 1 && rating <= 5) {
                    return rating;
                }

                System.out.println(
                        "Enter a number between 1 and 5."
                );

            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input. Enter a whole number."
                );
            }
        }
    }
}