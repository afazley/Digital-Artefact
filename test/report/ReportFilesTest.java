
package report;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import model.AssessmentStatus;
import model.ImpactAssessment;
import model.ProjectChange;
import model.Risk;
import model.Stakeholder;

public class ReportFilesTest {

    // Creates an approved assessment for testing.
    private ImpactAssessment createApprovedAssessment() {

        ImpactAssessment assessment = new ImpactAssessment();

        assessment.setProjectChange(new ProjectChange(
                "Website Migration",
                "Move website",
                "Improve reliability",
                "15/12/2026"
        ));

        assessment.addRisk(
                new Risk("Downtime", 4, 5, "Rollback")
        );

        assessment.addStakeholder(
                new Stakeholder("Sarah", "Manager", "IT")
        );

        assessment.startProgress();
        assessment.submitForApproval();
        assessment.reviewAssessment(AssessmentStatus.APPROVED);

        return assessment;
    }

    @Test
    void generatedReportIncludesApprovalStatus() throws Exception {

        Path reportFile = ReportGenerator.generateReport(
                createApprovedAssessment()
        );

        assertNotNull(reportFile);

        try {
            assertTrue(Files.exists(reportFile));

            String content = Files.readString(reportFile);

            assertTrue(
                    content.contains("Assessment status: APPROVED")
            );

        } finally {
            Files.deleteIfExists(reportFile);
        }
    }

    @Test
    void generatingTwiceDoesNotOverwriteFirstReport() throws Exception {

        ImpactAssessment assessment = createApprovedAssessment();

        Path first = ReportGenerator.generateReport(assessment);
        Path second = ReportGenerator.generateReport(assessment);

        assertNotNull(first);
        assertNotNull(second);

        try {
            assertNotEquals(first, second);

            assertTrue(Files.exists(first));
            assertTrue(Files.exists(second));

            assertEquals(
                    Files.readString(first),
                    Files.readString(second)
            );

        } finally {
            Files.deleteIfExists(first);
            Files.deleteIfExists(second);
        }
    }
}
