package model;

import java.util.ArrayList;
import java.util.List;

public class ImpactAssessment {
    
    private AssessmentStatus status;
    private ProjectChange projectChange;
    private List<Risk> risks;
    private List<Stakeholder> stakeholders;

    public ImpactAssessment() {
        risks = new ArrayList<>();
        stakeholders = new ArrayList<>();
        status = AssessmentStatus.DRAFT;
    }

    public void setProjectChange(ProjectChange projectChange) {
        this.projectChange = projectChange;
    }

    public ProjectChange getProjectChange() {
        return projectChange;
    }

    public List<Risk> getRisks() {
        return risks;
    }

    public List<Stakeholder> getStakeholders() {
        return stakeholders;
    }

    public void addRisk(Risk risk) {
        risks.add(risk);
    }

    public void addStakeholder(Stakeholder stakeholder) {
        stakeholders.add(stakeholder);
    }

    public boolean isComplete() {
        return projectChange != null
                && projectChange.isComplete()
                && !risks.isEmpty()
                && !stakeholders.isEmpty();
    }

    public AssessmentStatus getStatus() {
    return status;
    }

    public boolean startProgress() {
        if (status == AssessmentStatus.DRAFT
                || status == AssessmentStatus.CHANGES_REQUIRED) {

            status = AssessmentStatus.IN_PROGRESS;
            return true;
        }

        return false;
    }

    public boolean submitForApproval() {
        if (status == AssessmentStatus.IN_PROGRESS && isComplete()) {
            status = AssessmentStatus.PENDING_APPROVAL;
            return true;
        }

        return false;
    }

    public boolean reviewAssessment(AssessmentStatus decision) {

        if (status != AssessmentStatus.PENDING_APPROVAL) {
            return false;
        }

        if (decision == AssessmentStatus.APPROVED
                || decision == AssessmentStatus.REJECTED
                || decision == AssessmentStatus.CHANGES_REQUIRED) {

            status = decision;
            return true;
        }

        return false;
    }
}