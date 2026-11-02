package app;

import java.util.Scanner;
import model.Risk;

public class ImpactAssessmentApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {

            System.out.println("\n===============================");
            System.out.println("   IMPACT ASSESSMENT GENERATOR");
            System.out.println("===============================");
            System.out.println("1. Create new assessment");
            System.out.println("2. View assessment");
            System.out.println("3. Add risk");
            System.out.println("4. Add stakeholder");
            System.out.println("5. Check missing information");
            System.out.println("6. Generate report");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Create assessment selected.");
                    break;

                case "2":
                    System.out.println("View assessment selected.");
                    break;

                case "3":
                    addRisk(scanner);
                    break;

                case "4":
                    System.out.println("Add stakeholder selected.");
                    break;

                case "5":
                    System.out.println("Check missing information selected.");
                    break;

                case "6":
                    System.out.println("Generate report selected.");
                    break;

                case "7":
                    System.out.println("Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    public static void addRisk(Scanner scanner) {

    System.out.println("\n--- ADD NEW RISK ---");

    System.out.print("Enter risk description: ");
    String description = scanner.nextLine();

    int probability = 0;

    while (probability < 1 || probability > 5) {
        System.out.print("Enter probability (1-5): ");

        try {
            probability = Integer.parseInt(scanner.nextLine());

            if (probability < 1 || probability > 5) {
                System.out.println("Please enter a number between 1 and 5.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a whole number.");
        }
    }

    int impact = 0;

    while (impact < 1 || impact > 5) {
        System.out.print("Enter impact (1-5): ");

        try {
            impact = Integer.parseInt(scanner.nextLine());

            if (impact < 1 || impact > 5) {
                System.out.println("Please enter a number between 1 and 5.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a whole number.");
        }
    }

    System.out.print("Enter mitigation: ");
    String mitigation = scanner.nextLine();

    Risk risk = new Risk(description, probability, impact, mitigation);

    System.out.println("\n--- RISK ASSESSMENT ---");
    System.out.println("Description: " + risk.getDescription());
    System.out.println("Risk score: " + risk.calculateRiskScore());
    System.out.println("Risk level: " + risk.getRiskLevel());
    System.out.println("Mitigation: " + risk.getMitigation());
}
}