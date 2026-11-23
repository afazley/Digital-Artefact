
package app;

import java.util.Scanner;
import model.ImpactAssessment;
import model.ProjectChange;
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

        hint("Tell us what is changing and why it is needed.");

        String projectName = readRequiredText(
                "Enter project name: "
        );

        hint("Briefly describe the change being proposed.");
        String description = readRequiredText(
                "Describe the proposed change: "
        );

        hint("Explain what the project is aiming to improve.");
        String reason = readRequiredText(
                "Why is this change needed? "
        );

        hint("Use the format dd/MM/yyyy, for example 15/12/2026.");
        String date = readValidDate();

        return service.createAssessment(
                projectName, description, reason, date
        );
    }



    public Risk addRisk() {
        System.out.println("\n--- ADD NEW RISK ---");

        hint("What could go wrong, and how could you reduce the impact?");
        String description = readRequiredText(
                "Enter risk description: "
        );

        hint("Probability: 1 = very unlikely, 5 = very likely.");
        int probability = readRiskRating("probability");

        hint("Impact: 1 = minor, 5 = severe.");
        int impact = readRiskRating("impact");

        hint("Describe the action you would take to manage this risk.");
        String mitigation = readRequiredText(
                "Enter mitigation: "
        );

        return new Risk(
                description, probability, impact, mitigation
        );
    }



    public Stakeholder addStakeholder() {
        System.out.println("\n--- ADD STAKEHOLDER ---");

        hint("Who might be affected by this change or involved in it?");

        String name = readRequiredText(
                "Enter stakeholder name: "
        );

        hint("What is their responsibility within the project?");
        String role = readRequiredText(
                "Enter their role: "
        );

        hint("Which team or department do they belong to?");
        String department = readRequiredText(
                "Enter their department: "
        );

        return new Stakeholder(name, role, department);
    }


    // Repeats the question if the user enters nothing or just spaces.
    private String readRequiredText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be empty. Please try again.");
        }
    }

    private String readValidDate() {
        while (true) {
            System.out.print("Enter proposed implementation date (dd/MM/yyyy): ");
            String value = scanner.nextLine().trim();

            if (ProjectChange.isValidImplementationDate(value)) {
                return value;
            }

            System.out.println("Enter a real date in dd/MM/yyyy format.");
        }
    }

    private int readRiskRating(String ratingName) {
        while (true) {
            System.out.print("Enter " + ratingName + " (1-5): ");

            try {
                int rating = Integer.parseInt(scanner.nextLine().trim());

                if (rating >= 1 && rating <= 5) {
                    return rating;
                }

                System.out.println("Enter a number between 1 and 5.");

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a whole number.");
            }
        }
    }


    private void hint(String message) {
        // ANSI colour codes display guidance in grey in the terminal.
        System.out.println("\033[90m  " + message + "\033[0m");
    }

}
