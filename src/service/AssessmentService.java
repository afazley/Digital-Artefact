package service;

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
}