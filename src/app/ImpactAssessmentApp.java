
package app;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.AssessmentStatus;
import model.ImpactAssessment;
import model.ProjectChange;
import model.Risk;
import model.Stakeholder;
import model.UserRole;
import report.ReportGenerator;
import service.AssessmentService;

public class ImpactAssessmentApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        UserRole currentRole = selectRole(scanner);
        System.out.println("\nLogged in as: " + currentRole);
        AssessmentService service = new AssessmentService();

        List<ImpactAssessment> assessments = new ArrayList<>();
        ImpactAssessment assessment = null;

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
            System.out.println("9. View all assessments");
            System.out.println("10. Select assessment");
            System.out.println("11. Switch user role");
            System.out.println("12. Start / Resume assessment");
            System.out.println("13. Submit assessment for approval");
            System.out.println("14. Review assessment (Manager only)");

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

                    service.addAssessment(assessments, assessment);

                    System.out.println("\nAssessment created successfully!");
                    System.out.println("Assessment ID: " + assessments.size());
                    System.out.println("Currently selected: " + projectName);

                case "2":
                    if (assessment == null) {
                        System.out.println(
                                "Please create or select an assessment first."
                        );
                        break;
                    }
                    
                    viewRisks(assessment);
                    break;

                case "3":

                    if (assessment == null) {
                        System.out.println(
                                "Please create or select an assessment first."
                        );
                        break;
                    }

                    Risk risk = addRisk(scanner);
                    service.addRisk(assessment, risk);
                    break;

                case "4":

                    if (assessment == null) {
                        System.out.println(
                                "Please create or select an assessment first."
                        );
                        break;
                    }

                    Stakeholder stakeholder = addStakeholder(scanner);
                    service.addStakeholder(assessment, stakeholder);
                    break;

                case "5":

                    if (assessment == null) {
                        System.out.println(
                                "Please create or select an assessment first."
                        );
                        break;
                    }

                    checkMissingInformation(assessment);
                    break;

                case "6":

                    if (assessment == null) {
                        System.out.println(
                                "Please create or select an assessment first."
                        );
                        break;
                    }

                    generateReport(assessment);
                    break;

                case "7":
                    System.out.println("Goodbye!");
                    running = false;
                    break;

                case "8":
                    if (assessment == null) {
                        System.out.println(
                                "Please create or select an assessment first."
                        );
                        break;
                    }
                    viewStakeholders(assessment);
                    break;

                case "9":
                    service.viewAllAssessments(assessments);
                    break;

                case "10":

                    if (assessments.isEmpty()) {
                        System.out.println("No assessments available.");
                        break;
                    }

                    service.viewAllAssessments(assessments);

                    System.out.print("\nEnter assessment number to select: ");

                    try {
                        int selectedNumber = Integer.parseInt(
                                scanner.nextLine().trim()
                        );

                        if (selectedNumber >= 1
                                && selectedNumber <= assessments.size()) {

                            assessment = assessments.get(selectedNumber - 1);

                            System.out.println(
                                    "Selected assessment: "
                                    + assessment.getProjectChange().getProjectName()
                            );

                        } else {
                            System.out.println("Invalid assessment number.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid whole number.");
                    }

                    break;
                
                case "11":
                    currentRole = selectRole(scanner);
                    break;

                case "12":

                    if (assessment == null) {
                        System.out.println("Please select an assessment first.");
                        break;
                    }

                    if (service.startAssessment(assessment)) {
                        System.out.println("Assessment is now IN PROGRESS.");
                    } else {
                        System.out.println(
                            "Cannot start assessment from status: "
                            + assessment.getStatus()
                        );
                    }

                    break;

                case "13":

                    if (assessment == null) {
                        System.out.println("Please select an assessment first.");
                        break;
                    }

                    if (service.submitAssessment(assessment)) {
                        System.out.println("Assessment submitted for approval!");
                    } else {
                        System.out.println(
                            "Submission failed. Assessment must be IN PROGRESS "
                            + "and contain all required information."
                        );
                    }

                    break;
                    
                case "14":

                    if (currentRole != UserRole.MANAGER) {
                        System.out.println(
                            "Access denied. Only managers can review assessments."
                        );
                        break;
                    }

                    if (assessment == null) {
                        System.out.println("Please select an assessment first.");
                        break;
                    }

                    if (assessment.getStatus() != AssessmentStatus.PENDING_APPROVAL) {
                        System.out.println(
                            "Assessment must be PENDING APPROVAL before review."
                        );
                        break;
                    }

                    System.out.println("\n--- MANAGER REVIEW ---");
                    System.out.println("1. Approve");
                    System.out.println("2. Reject");
                    System.out.println("3. Request changes");

                    System.out.print("Enter decision: ");
                    String decisionChoice = scanner.nextLine().trim();

                    AssessmentStatus decision = null;

                    switch (decisionChoice) {
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
                            break;
                    }

                    if (!decisionChoice.equals("1")
                            && !decisionChoice.equals("2")
                            && !decisionChoice.equals("3")) {
                        break;
                    }

                    if (service.reviewAssessment(assessment, decision)) {
                        System.out.println(
                            "Assessment status updated to: "
                            + assessment.getStatus()
                        );
                    }

                    break;
                
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    public static UserRole selectRole(Scanner scanner) {

    while (true) {
        System.out.println("\n===============================");
        System.out.println("       SELECT USER ROLE");
        System.out.println("===============================");
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