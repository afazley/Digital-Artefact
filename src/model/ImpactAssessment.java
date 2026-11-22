
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImpactAssessment {

    private ProjectChange projectChange;
    private final List<Risk> risks;
    private final List<Stakeholder> stakeholders;
    private AssessmentStatus status;

    public ImpactAssessment() {
        risks = new ArrayList<>();
        stakeholders = new ArrayList<>();
        status = AssessmentStatus.DRAFT;
    }

    // Only draft and in-progress assessments can be edited.
    public boolean isEditable() {
        return status == AssessmentStatus.DRAFT
                || status == AssessmentStatus.IN_PROGRESS;
    }

    public void setProjectChange(ProjectChange projectChange) {
        if (!isEditable()) {
            System.out.println("Assessment cannot be modified: " + status);
            return;
        }

        this.projectChange = projectChange;
    }

    public ProjectChange getProjectChange() {
        return projectChange;
    }

    // Prevent external code from modifying the lists directly.
    public List<Risk> getRisks() {
        return Collections.unmodifiableList(risks);
    }

    public List<Stakeholder> getStakeholders() {
        return Collections.unmodifiableList(stakeholders);
    }

    public void addRisk(Risk risk) {
        if (!isEditable()) {
            System.out.println("Cannot add risk. Assessment is " + status);
            return;
        }

        risks.add(risk);
    }

    public void addStakeholder(Stakeholder stakeholder) {
        if (!isEditable()) {
            System.out.println(
                    "Cannot add stakeholder. Assessment is " + status
            );
            return;
        }

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
