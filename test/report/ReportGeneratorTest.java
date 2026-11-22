
package report;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import model.Risk;
import model.Stakeholder;

public class ReportGeneratorTest {

    private final Path reportPath =
            Path.of("ImpactAssessmentReport.txt");

    @Test
    void reportContainsProjectDetails() throws Exception {

        ReportGenerator.generateReport(
                "Website Migration",
                "Move website",
                "Improve reliability",
                "15/12/2026",
                new ArrayList<>(),
                new ArrayList<>()
        );

        String report = Files.readString(reportPath);

        assertTrue(report.contains("Website Migration"));
        assertTrue(report.contains("Move website"));
        assertTrue(report.contains("Improve reliability"));
        assertTrue(report.contains("15/12/2026"));
    }

    @Test
    void reportIncludesRisksAndStakeholders() throws Exception {

        List<Risk> risks = new ArrayList<>();
        risks.add(new Risk(
                "System downtime", 5, 4, "Rollback plan"
        ));

        List<Stakeholder> stakeholders = new ArrayList<>();
        stakeholders.add(new Stakeholder(
                "Sarah", "Manager", "IT"
        ));

        ReportGenerator.generateReport(
                "Database Upgrade",
                "Upgrade database",
                "Improve performance",
                "20/12/2026",
                risks,
                stakeholders
        );

        String report = Files.readString(reportPath);

        assertTrue(report.contains("System downtime"));
        assertTrue(report.contains("Risk score: 20")
                || report.contains("Score: 20"));
        assertTrue(report.contains("Rollback plan"));
        assertTrue(report.contains("Sarah"));
        assertTrue(report.contains("IT"));
    }

    @Test
    void reportOrdersRisksByPriority() throws Exception {

        List<Risk> risks = new ArrayList<>();

        risks.add(new Risk("Low risk", 1, 2, "Monitor"));
        risks.add(new Risk("High risk", 5, 5, "Rollback"));
        risks.add(new Risk("Medium risk", 3, 3, "Training"));

        ReportGenerator.generateReport(
                "Test Project",
                "Test change",
                "Testing",
                "15/12/2026",
                risks,
                new ArrayList<>()
        );

        String report = Files.readString(reportPath);

        int highPosition = report.indexOf("Description: High risk");
        int mediumPosition = report.indexOf("Description: Medium risk");
        int lowPosition = report.indexOf("Description: Low risk");

        assertTrue(highPosition >= 0);
        assertTrue(mediumPosition >= 0);
        assertTrue(lowPosition >= 0);

        assertTrue(highPosition < mediumPosition);
        assertTrue(mediumPosition < lowPosition);
    }
}
