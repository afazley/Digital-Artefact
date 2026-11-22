
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

        String projectName = readRequiredText("Enter project name: ");
        String description = readRequiredText("Describe the proposed change: ");
        String reason = readRequiredText("Why is this change needed? ");
        String date = readValidDate();

        return service.createAssessment(
                projectName, description, reason, date
        );
    }

    public Risk addRisk() {
        System.out.println("\n--- ADD NEW RISK ---");

        String description = readRequiredText("Enter risk description: ");
        int probability = readRiskRating("probability");
        int impact = readRiskRating("impact");
        String mitigation = readRequiredText("Enter mitigation: ");

        return new Risk(description, probability, impact, mitigation);
    }

    public Stakeholder addStakeholder() {
        System.out.println("\n--- ADD STAKEHOLDER ---");

        String name = readRequiredText("Enter stakeholder name: ");
        String role = readRequiredText("Enter their role: ");
        String department = readRequiredText("Enter their department: ");

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
}
