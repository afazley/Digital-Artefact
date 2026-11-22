package service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import model.ImpactAssessment;
import model.Risk;
import model.AssessmentStatus;
import model.ProjectChange;
import model.Stakeholder;
import model.UserRole;

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


    private ImpactAssessment createPendingAssessment() {

        ImpactAssessment assessment = new ImpactAssessment();

        assessment.setProjectChange(new ProjectChange(
                "Website Migration",
                "Move website to new servers",
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

        return assessment;
    }

    @Test
    void employeeCannotApproveAssessment() {

        ImpactAssessment assessment = createPendingAssessment();

        boolean result = service.reviewAssessment(
                assessment,
                AssessmentStatus.APPROVED,
                UserRole.EMPLOYEE
        );

        assertFalse(result);
        assertEquals(
                AssessmentStatus.PENDING_APPROVAL,
                assessment.getStatus()
        );
    }

    @Test
    void managerCanApproveAssessment() {

        ImpactAssessment assessment = createPendingAssessment();

        boolean result = service.reviewAssessment(
                assessment,
                AssessmentStatus.APPROVED,
                UserRole.MANAGER
        );

        assertTrue(result);
        assertEquals(
                AssessmentStatus.APPROVED,
                assessment.getStatus()
        );
    }

    @Test
    void managerCannotApproveDraftAssessment() {

        ImpactAssessment assessment = new ImpactAssessment();

        boolean result = service.reviewAssessment(
                assessment,
                AssessmentStatus.APPROVED,
                UserRole.MANAGER
        );

        assertFalse(result);
        assertEquals(
                AssessmentStatus.DRAFT,
                assessment.getStatus()
        );
    }

    @Test
    void missingRoleCannotApproveAssessment() {

        ImpactAssessment assessment = createPendingAssessment();

        boolean result = service.reviewAssessment(
                assessment,
                AssessmentStatus.APPROVED,
                null
        );

        assertFalse(result);
        assertEquals(
                AssessmentStatus.PENDING_APPROVAL,
                assessment.getStatus()
        );
    }

}