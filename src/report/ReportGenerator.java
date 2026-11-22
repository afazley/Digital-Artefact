package report;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.ImpactAssessment;
import model.ProjectChange;
import model.Risk;
import model.Stakeholder;

public class ReportGenerator {

    // Retained for existing tests that use the original report method.
    public static void generateReport(
            String projectName,
            String changeDescription,
            String reason,
            String implementationDate,
            List<Risk> risks,
            List<Stakeholder> stakeholders) {

        writeReport(Path.of("ImpactAssessmentReport.txt"), projectName,
                changeDescription, reason, implementationDate,
                null, risks, stakeholders);
    }

    // New reports use a separate filename, so earlier reports are kept.
    public static Path generateReport(ImpactAssessment assessment) {
        if (assessment == null || assessment.getProjectChange() == null) {
            System.out.println("Please create an assessment first.");
            return null;
        }

        ProjectChange change = assessment.getProjectChange();

        try {
            Path directory = Path.of("reports");
            Files.createDirectories(directory);

            // Remove characters that are unsuitable for filenames.
            String name = change.getProjectName().trim()
                    .replaceAll("[^A-Za-z0-9_-]", "_")
                    .replaceAll("_+", "_");
            if (name.isEmpty()) {
                name = "Project";
            }
            if (name.length() > 50) {
                name = name.substring(0, 50);
            }

            String baseName = "ImpactAssessment_" + name;
            Path file = directory.resolve(baseName + ".txt");
            int number = 2;

            while (Files.exists(file)) {
                file = directory.resolve(baseName + "_" + number + ".txt");
                number++;
            }

            boolean saved = writeReport(file, change.getProjectName(),
                    change.getDescription(), change.getReason(),
                    change.getImplementationDate(),
                    assessment.getStatus().toString(),
                    assessment.getRisks(), assessment.getStakeholders());

            return saved ? file : null;
        } catch (IOException e) {
            System.out.println("Error creating report folder: " + e.getMessage());
            return null;
        }
    }

    private static boolean writeReport(
            Path file,
            String projectName,
            String changeDescription,
            String reason,
            String implementationDate,
            String status,
            List<Risk> risks,
            List<Stakeholder> stakeholders) {

        try (PrintWriter writer = new PrintWriter(
                Files.newBufferedWriter(file, StandardCharsets.UTF_8))) {

            writer.println("==================================");
            writer.println("       IMPACT ASSESSMENT REPORT");
            writer.println("==================================");
            writer.println("\nPROJECT DETAILS");
            writer.println("Project name: " + projectName);
            writer.println("Change description: " + changeDescription);
            writer.println("Reason for change: " + reason);
            writer.println("Implementation date: " + implementationDate);
            if (status != null) {
                writer.println("Assessment status: " + status);
            }

            // Sort a copy, so the assessment's original list is unaffected.
            List<Risk> sortedRisks = new ArrayList<>(risks);
            sortedRisks.sort(Comparator.comparingInt(
                    Risk::calculateRiskScore).reversed());

            writer.println("\nRISKS");
            if (sortedRisks.isEmpty()) {
                writer.println("No risks recorded.");
            } else {
                for (int i = 0; i < sortedRisks.size(); i++) {
                    Risk risk = sortedRisks.get(i);
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
            writer.flush();

            if (writer.checkError()) {
                System.out.println("Error writing report: " + file);
                return false;
            }

            System.out.println("Report generated successfully!");
            System.out.println("Saved as " + file);
            return true;
        } catch (IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
            return false;
        }
    }
}
