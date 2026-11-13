package model;

import java.util.ArrayList;
import java.util.List;

public class ImpactAssessment {

    private ProjectChange projectChange;
    private List<Risk> risks;
    private List<Stakeholder> stakeholders;

    public ImpactAssessment() {
        risks = new ArrayList<>();
        stakeholders = new ArrayList<>();
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
}