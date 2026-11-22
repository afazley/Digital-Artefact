
package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ImpactAssessmentTest {

        private ImpactAssessment createCompleteAssessment() {
                ImpactAssessment assessment = new ImpactAssessment();

                ProjectChange change = new ProjectChange(
                                "Website Migration",
                                "Move website to new servers",
                                "Improve reliability",
                                "15/12/2026");

                assessment.setProjectChange(change);

                assessment.addRisk(
                                new Risk("System downtime", 4, 5, "Rollback plan"));

                assessment.addStakeholder(
                                new Stakeholder("Sarah", "Manager", "IT"));

                return assessment;
        }

        @Test
        void newAssessmentStartsAsDraft() {
                ImpactAssessment assessment = new ImpactAssessment();

                assertEquals(AssessmentStatus.DRAFT, assessment.getStatus());
        }

        @Test
        void newAssessmentIsIncomplete() {
                ImpactAssessment assessment = new ImpactAssessment();

                assertFalse(assessment.isComplete());
        }

        @Test
        void assessmentWithAllDetailsIsComplete() {
                ImpactAssessment assessment = createCompleteAssessment();

                assertTrue(assessment.isComplete());
        }

        @Test
        void assessmentWithoutRisksIsIncomplete() {
                ImpactAssessment assessment = new ImpactAssessment();

                assessment.setProjectChange(new ProjectChange(
                                "Website Migration",
                                "Move website to new servers",
                                "Improve reliability",
                                "15/12/2026"));

                assessment.addStakeholder(
                                new Stakeholder("Sarah", "Manager", "IT"));

                assertFalse(assessment.isComplete());
        }

        @Test
        void assessmentWithoutStakeholdersIsIncomplete() {
                ImpactAssessment assessment = new ImpactAssessment();

                assessment.setProjectChange(new ProjectChange(
                                "Website Migration",
                                "Move website to new servers",
                                "Improve reliability",
                                "15/12/2026"));

                assessment.addRisk(
                                new Risk("Downtime", 4, 5, "Rollback plan"));

                assertFalse(assessment.isComplete());
        }

        @Test
        void startingAssessmentChangesStatus() {
                ImpactAssessment assessment = new ImpactAssessment();

                assertTrue(assessment.startProgress());
                assertEquals(
                                AssessmentStatus.IN_PROGRESS,
                                assessment.getStatus());
        }

        @Test
        void incompleteAssessmentCannotBeSubmitted() {
                ImpactAssessment assessment = new ImpactAssessment();

                assessment.startProgress();

                assertFalse(assessment.submitForApproval());
                assertEquals(
                                AssessmentStatus.IN_PROGRESS,
                                assessment.getStatus());
        }

        @Test
        void completeAssessmentCanBeSubmitted() {
                ImpactAssessment assessment = createCompleteAssessment();

                assessment.startProgress();

                assertTrue(assessment.submitForApproval());
                assertEquals(
                                AssessmentStatus.PENDING_APPROVAL,
                                assessment.getStatus());
        }

        @Test
        void managerDecisionCanApprovePendingAssessment() {
                ImpactAssessment assessment = createCompleteAssessment();

                assessment.startProgress();
                assessment.submitForApproval();

                assertTrue(
                                assessment.reviewAssessment(AssessmentStatus.APPROVED));

                assertEquals(
                                AssessmentStatus.APPROVED,
                                assessment.getStatus());
        }

        @Test
        void draftAssessmentCannotBeApproved() {
                ImpactAssessment assessment = createCompleteAssessment();

                assertFalse(
                                assessment.reviewAssessment(AssessmentStatus.APPROVED));

                assertEquals(
                                AssessmentStatus.DRAFT,
                                assessment.getStatus());
        }

        @Test
        void approvedAssessmentCannotBeApprovedAgain() {
                ImpactAssessment assessment = createCompleteAssessment();

                assessment.startProgress();
                assessment.submitForApproval();
                assessment.reviewAssessment(AssessmentStatus.APPROVED);

                assertFalse(
                                assessment.reviewAssessment(AssessmentStatus.APPROVED));
        }

        @Test
        void changesRequiredAssessmentCanResume() {
                ImpactAssessment assessment = createCompleteAssessment();

                assessment.startProgress();
                assessment.submitForApproval();

                assessment.reviewAssessment(
                                AssessmentStatus.CHANGES_REQUIRED);

                assertTrue(assessment.startProgress());

                assertEquals(
                                AssessmentStatus.IN_PROGRESS,
                                assessment.getStatus());
        }

        @Test
        void approvedAssessmentCannotAddRisks() {

                // Arrange: create a complete assessment.
                ImpactAssessment assessment = createCompleteAssessment();

                // Move it through the approval workflow.
                assessment.startProgress();
                assessment.submitForApproval();
                assessment.reviewAssessment(AssessmentStatus.APPROVED);

                // Record the number of risks before attempting a change.
                int originalRiskCount = assessment.getRisks().size();

                // Act: attempt to add a risk after approval.
                assessment.addRisk(
                                new Risk("New security risk", 5, 5, "Security testing"));

                // Assert: the approved assessment should remain unchanged.
                assertEquals(originalRiskCount, assessment.getRisks().size());
        }

        @Test
        void approvedAssessmentCannotAddStakeholders() {

                ImpactAssessment assessment = createCompleteAssessment();

                assessment.startProgress();
                assessment.submitForApproval();
                assessment.reviewAssessment(AssessmentStatus.APPROVED);

                int originalStakeholderCount = assessment.getStakeholders().size();

                assessment.addStakeholder(
                                new Stakeholder("James", "Developer", "IT"));

                assertEquals(
                                originalStakeholderCount,
                                assessment.getStakeholders().size());
        }

        @Test
        void approvedAssessmentCannotBeModifiedThroughRiskList() {
                ImpactAssessment assessment = createCompleteAssessment();

                assessment.startProgress();
                assessment.submitForApproval();
                assessment.reviewAssessment(AssessmentStatus.APPROVED);

                assertThrows(UnsupportedOperationException.class, () -> {
                        assessment.getRisks().clear();
                });
        }

}
