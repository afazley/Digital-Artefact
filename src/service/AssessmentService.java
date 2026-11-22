package service;
import java.util.ArrayList;
import java.util.List;
import model.AssessmentStatus;
import model.ImpactAssessment;
import model.ProjectChange;
import model.Risk;
import model.Stakeholder;
import model.UserRole;

public class AssessmentService {

    public ImpactAssessment createAssessment(
            String projectName,
            String description,
            String reason,
            String implementationDate) {

        ImpactAssessment assessment = new ImpactAssessment();

        ProjectChange change = new ProjectChange(
                projectName,
                description,
                reason,
                implementationDate
        );

        assessment.setProjectChange(change);

        return assessment;
    }

    public void addRisk(ImpactAssessment assessment, Risk risk) {
        assessment.addRisk(risk);
    }

    public void addStakeholder(
            ImpactAssessment assessment, Stakeholder stakeholder) {
        assessment.addStakeholder(stakeholder);
    }

    public boolean checkCompleteness(ImpactAssessment assessment) {
        return assessment.isComplete();
    }

    public void addAssessment(
        List<ImpactAssessment> assessments,
        ImpactAssessment assessment) {

    assessments.add(assessment);
}

    public void viewAllAssessments(List<ImpactAssessment> assessments) {

        if (assessments.isEmpty()) {
            System.out.println("No assessments have been created yet.");
            return;
        }

        System.out.println("\n--- ALL IMPACT ASSESSMENTS ---");

        for (int i = 0; i < assessments.size(); i++) {

            ImpactAssessment assessment = assessments.get(i);
            ProjectChange change = assessment.getProjectChange();

            System.out.println("\nAssessment " + (i + 1));

            if (change != null) {
                System.out.println("Project: " + change.getProjectName());
                System.out.println("Implementation date: "
                        + change.getImplementationDate());
            }

            System.out.println("Risks: " + assessment.getRisks().size());
            System.out.println("Stakeholders: "
                    + assessment.getStakeholders().size());

            System.out.println("Status: " + assessment.getStatus());
        }
    }

    public boolean startAssessment(ImpactAssessment assessment) {
    return assessment.startProgress();
    }

    public boolean submitAssessment(ImpactAssessment assessment) {
        return assessment.submitForApproval();
    }

    public boolean reviewAssessment(
            ImpactAssessment assessment,
            AssessmentStatus decision,
            UserRole role) {

        // Only managers can review assessments.
        if (role != UserRole.MANAGER) {
            return false;
        }

        // A selected assessment must exist.
        if (assessment == null) {
            return false;
        }

        // The assessment must be awaiting approval.
        if (assessment.getStatus()
                != AssessmentStatus.PENDING_APPROVAL) {
            return false;
        }

        return assessment.reviewAssessment(decision);
    }

    public List<Risk> getSortedRisks(ImpactAssessment assessment) {

    // Copy the risks so the original list is not modified.
    List<Risk> sortedRisks = new ArrayList<>(assessment.getRisks());

    // Bubble sort: highest risk score first.
    for (int i = 0; i < sortedRisks.size() - 1; i++) {

        for (int j = 0; j < sortedRisks.size() - i - 1; j++) {

            Risk currentRisk = sortedRisks.get(j);
            Risk nextRisk = sortedRisks.get(j + 1);

            if (currentRisk.calculateRiskScore()
                    < nextRisk.calculateRiskScore()) {

                sortedRisks.set(j, nextRisk);
                sortedRisks.set(j + 1, currentRisk);
            }
        }
    }

    return sortedRisks;
    }
}