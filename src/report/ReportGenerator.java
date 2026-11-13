package report;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import model.Risk;
import model.Stakeholder;

public class ReportGenerator {

    public static void generateReport(
            String projectName,
            String changeDescription,
            String reason,
            String implementationDate,
            List<Risk> risks,
            List<Stakeholder> stakeholders) {

        try (PrintWriter writer = new PrintWriter(
                new FileWriter("ImpactAssessmentReport.txt"))) {

            writer.println("==================================");
            writer.println("       IMPACT ASSESSMENT REPORT");
            writer.println("==================================");

            writer.println("\nPROJECT DETAILS");
            writer.println("Project name: " + projectName);
            writer.println("Change description: " + changeDescription);
            writer.println("Reason for change: " + reason);
            writer.println("Implementation date: " + implementationDate);

            writer.println("\nRISKS");

            if (risks.isEmpty()) {
                writer.println("No risks recorded.");
            } else {
                for (int i = 0; i < risks.size(); i++) {
                    Risk risk = risks.get(i);

                    writer.println("\nRisk " + (i + 1));
                    writer.println("Description: " + risk.getDescription());
                    writer.println("Probability: " + risk.getProbability());
                    writer.println("Impact: " + risk.getImpact());
                    writer.println("Score: " + risk.calculateRiskScore());
                    writer.println("Level: " + risk.getRiskLevel());
                    writer.println("Mitigation: " + risk.getMitigation());
                }
            }

            writer.println("\nSTAKEHOLDERS");

            if (stakeholders.isEmpty()) {
                writer.println("No stakeholders recorded.");
            } else {
                for (int i = 0; i < stakeholders.size(); i++) {
                    Stakeholder stakeholder = stakeholders.get(i);

                    writer.println("\nStakeholder " + (i + 1));
                    writer.println("Name: " + stakeholder.getName());
                    writer.println("Role: " + stakeholder.getRole());
                    writer.println("Department: " + stakeholder.getDepartment());
                }
            }

            writer.println("\n==================================");
            writer.println("          END OF REPORT");
            writer.println("==================================");

            System.out.println("Report generated successfully!");
            System.out.println("Saved as ImpactAssessmentReport.txt");

        } catch (IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }
}