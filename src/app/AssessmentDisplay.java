package app;

import java.util.List;

import model.ImpactAssessment;
import model.ProjectChange;
import model.Risk;
import model.Stakeholder;
import service.AssessmentService;

public class AssessmentDisplay {

    private final AssessmentService service;

    public AssessmentDisplay(AssessmentService service) {
        this.service = service;
    }

    public void viewRisks(ImpactAssessment assessment) {
        System.out.println("\n--- RISKS ORDERED BY PRIORITY ---");

        List<Risk> sortedRisks = service.getSortedRisks(assessment);

        if (sortedRisks.isEmpty()) {
            System.out.println("No risks have been added yet.");
            return;
        }

        for (int i = 0; i < sortedRisks.size(); i++) {
            Risk risk = sortedRisks.get(i);

            System.out.println("\nPriority " + (i + 1));
            System.out.println("Description: " + risk.getDescription());
            System.out.println("Probability: " + risk.getProbability());
            System.out.println("Impact: " + risk.getImpact());
            System.out.println("Risk score: "
                    + risk.calculateRiskScore());
            System.out.println("Risk level: " + risk.getRiskLevel());
            System.out.println("Mitigation: " + risk.getMitigation());
        }
    }

    public void viewStakeholders(ImpactAssessment assessment) {
        System.out.println("\n--- SAVED STAKEHOLDERS ---");

        if (assessment.getStakeholders().isEmpty()) {
            System.out.println("No stakeholders have been added yet.");
            return;
        }

        for (int i = 0; i < assessment.getStakeholders().size(); i++) {
            Stakeholder stakeholder =
                    assessment.getStakeholders().get(i);

            System.out.println("\nStakeholder " + (i + 1));
            stakeholder.displayDetails();
        }
    }

    public void checkMissingInformation(
            ImpactAssessment assessment) {

        System.out.println("\n--- MISSING INFORMATION CHECK ---");

        ProjectChange change = assessment.getProjectChange();
        boolean missing = false;

        if (change == null) {
            System.out.println(
                    "- Project assessment has not been created."
            );
            missing = true;

        } else if (!change.isComplete()) {
            System.out.println(
                    "- Some project details are missing."
            );
            missing = true;
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
            System.out.println(
                    "All required information has been provided."
            );
        }
    }

    public void viewAllAssessments(
            List<ImpactAssessment> assessments) {

        service.viewAllAssessments(assessments);
    }
}