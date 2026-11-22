package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import model.AssessmentStatus;
import model.ImpactAssessment;
import model.ProjectChange;
import model.Risk;
import model.Stakeholder;
import service.AssessmentService;

public class AssessmentValidationTest {
    private Scanner scannerFor(String text) {
        return new Scanner(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void blankProjectFieldsAndBadDatesRequireNewInput() {
        AssessmentInput input = new AssessmentInput(scannerFor(
                " \nWebsite Migration\n\nMove website\n   \nImprove reliability\nnot-a-date\n31/02/2027\n01/03/2027\n"),
                new AssessmentService());
        ImpactAssessment a = input.createAssessment();
        assertEquals("Website Migration", a.getProjectChange().getProjectName());
        assertEquals("01/03/2027", a.getProjectChange().getImplementationDate());
    }

    @Test
    void blankRiskDescriptionAndMitigationRequireNewInput() {
        AssessmentInput input = new AssessmentInput(scannerFor(
                "   \nSystem downtime\n2\n5\n\nRollback plan\n"), new AssessmentService());
        Risk risk = input.addRisk();
        assertEquals("System downtime", risk.getDescription());
        assertEquals("Rollback plan", risk.getMitigation());
    }

    @Test
    void blankStakeholderDetailsRequireNewInput() {
        AssessmentInput input = new AssessmentInput(scannerFor(
                "\nSarah\n \nManager\n\nIT\n"), new AssessmentService());
        Stakeholder stakeholder = input.addStakeholder();
        assertEquals("Sarah", stakeholder.getName());
        assertEquals("Manager", stakeholder.getRole());
        assertEquals("IT", stakeholder.getDepartment());
    }

    @Test
    void blankModelRiskBlocksApproval() {
        ImpactAssessment a = completeAssessment();
        a.addRisk(new Risk(" ", 3, 3, " "));
        assertTrue(a.startProgress());
        assertFalse(a.submitForApproval());
        assertEquals(AssessmentStatus.IN_PROGRESS, a.getStatus());
    }

    @Test
    void blankModelStakeholderBlocksApproval() {
        ImpactAssessment a = completeAssessment();
        a.addStakeholder(new Stakeholder(" ", " ", " "));
        assertTrue(a.startProgress());
        assertFalse(a.submitForApproval());
    }

    @Test
    void impossibleModelDateBlocksApproval() {
        ImpactAssessment a = completeAssessment();
        a.setProjectChange(new ProjectChange("Project", "Change", "Reason", "31/02/2027"));
        assertTrue(a.startProgress());
        assertFalse(a.submitForApproval());
    }

    @Test
    void approvedAssessmentRejectsControllerRiskInput() {
        AssessmentController controller = approvedController();
        controller.addRisk(); // Deliberately no more console input available.
        assertEquals(1, controller.getSelectedAssessment().getRisks().size());
    }

    @Test
    void approvedAssessmentRejectsControllerStakeholderInput() {
        AssessmentController controller = approvedController();
        controller.addStakeholder(); // Deliberately no more console input available.
        assertEquals(1, controller.getSelectedAssessment().getStakeholders().size());
    }

    private ImpactAssessment completeAssessment() {
        ImpactAssessment a = new ImpactAssessment();
        a.setProjectChange(new ProjectChange("Project", "Change", "Reason", "01/03/2027"));
        a.addRisk(new Risk("Downtime", 4, 5, "Rollback"));
        a.addStakeholder(new Stakeholder("Sarah", "Manager", "IT"));
        return a;
    }

    private AssessmentController approvedController() {
        AssessmentController controller = new AssessmentController(
                scannerFor("Project\nChange\nReason\n01/03/2027\n"));
        controller.createAssessment();
        ImpactAssessment a = controller.getSelectedAssessment();
        a.addRisk(new Risk("Downtime", 4, 5, "Rollback"));
        a.addStakeholder(new Stakeholder("Sarah", "Manager", "IT"));
        assertTrue(a.startProgress());
        assertTrue(a.submitForApproval());
        assertTrue(a.reviewAssessment(AssessmentStatus.APPROVED));
        return controller;
    }
}
