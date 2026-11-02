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

    public String getDescription() {
    return description;
    }

    public String getMitigation() {
        return mitigation;
    }
}