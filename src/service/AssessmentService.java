package service;
import java.util.List;
import model.AssessmentStatus;
import model.ImpactAssessment;
import model.ProjectChange;
import model.Risk;
import model.Stakeholder;
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
            AssessmentStatus decision) {

        return assessment.reviewAssessment(decision);
    }
}