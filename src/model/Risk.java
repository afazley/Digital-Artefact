package model;

public class Risk {

    private String description;
    private int probability;
    private int impact;
    private String mitigation;

    public Risk(String description, int probability,
            int impact, String mitigation) {

        this.description = description;
        this.probability = probability;
        this.impact = impact;
        this.mitigation = mitigation;
    }

    public int calculateRiskScore() {
        return probability * impact;
    }

    public String getRiskLevel() {

        int score = calculateRiskScore();

        if (score <= 4) {
            return "Low";
        } else if (score <= 9) {
            return "Medium";
        } else {
            return "High";
        }
    }

    public boolean isComplete() {
        return description != null && !description.isBlank()
                && mitigation != null && !mitigation.isBlank()
                && probability >= 1 && probability <= 5
                && impact >= 1 && impact <= 5;
    }

    public String getDescription() {
        return description;
    }

    public String getMitigation() {
        return mitigation;
    }

    public int getProbability() {
        return probability;
    }

    public int getImpact() {
        return impact;
    }

}