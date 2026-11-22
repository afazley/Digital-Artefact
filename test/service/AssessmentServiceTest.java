package service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import model.ImpactAssessment;
import model.Risk;

public class AssessmentServiceTest {

    private final AssessmentService service = new AssessmentService();

    @Test
    void risksAreSortedHighestFirst() {
        ImpactAssessment assessment = new ImpactAssessment();

        assessment.addRisk(new Risk("Low", 2, 2, "Monitor"));
        assessment.addRisk(new Risk("High", 5, 5, "Rollback"));
        assessment.addRisk(new Risk("Medium", 3, 3, "Training"));

        List<Risk> sorted = service.getSortedRisks(assessment);

        assertEquals(25, sorted.get(0).calculateRiskScore());
        assertEquals(9, sorted.get(1).calculateRiskScore());
        assertEquals(4, sorted.get(2).calculateRiskScore());
    }

    @Test
    void emptyRiskListReturnsEmptyList() {
        ImpactAssessment assessment = new ImpactAssessment();

        List<Risk> sorted = service.getSortedRisks(assessment);

        assertTrue(sorted.isEmpty());
    }

    @Test
    void singleRiskRemainsUnchanged() {
        ImpactAssessment assessment = new ImpactAssessment();

        assessment.addRisk(new Risk("Downtime", 4, 5, "Backup"));

        List<Risk> sorted = service.getSortedRisks(assessment);

        assertEquals(1, sorted.size());
        assertEquals(20, sorted.get(0).calculateRiskScore());
    }

    @Test
    void equalRiskScoresPreserveOriginalOrder() {
        ImpactAssessment assessment = new ImpactAssessment();

        Risk first = new Risk("First risk", 2, 3, "Monitor");
        Risk second = new Risk("Second risk", 3, 2, "Training");

        assessment.addRisk(first);
        assessment.addRisk(second);

        List<Risk> sorted = service.getSortedRisks(assessment);

        assertSame(first, sorted.get(0));
        assertSame(second, sorted.get(1));
    }

    @Test
    void sortingDoesNotModifyOriginalList() {
        ImpactAssessment assessment = new ImpactAssessment();

        Risk low = new Risk("Low risk", 1, 2, "Monitor");
        Risk high = new Risk("High risk", 5, 5, "Rollback");

        assessment.addRisk(low);
        assessment.addRisk(high);

        service.getSortedRisks(assessment);

        assertSame(low, assessment.getRisks().get(0));
        assertSame(high, assessment.getRisks().get(1));
    }
}