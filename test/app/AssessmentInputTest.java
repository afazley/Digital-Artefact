
package app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import model.Risk;
import service.AssessmentService;

public class AssessmentInputTest {

    // Simulates the user typing into the console.
    private AssessmentInput createInput(String userInput) {

        ByteArrayInputStream stream = new ByteArrayInputStream(
                userInput.getBytes(StandardCharsets.UTF_8));

        Scanner scanner = new Scanner(stream);

        return new AssessmentInput(
                scanner, new AssessmentService());
    }

    @Test
    void acceptsMinimumAndMaximumRatings() {

        AssessmentInput input = createInput(
                "System downtime\n1\n5\nRollback plan\n");

        Risk risk = input.addRisk();

        assertEquals(1, risk.getProbability());
        assertEquals(5, risk.getImpact());
        assertEquals(5, risk.calculateRiskScore());
    }

    @Test
    void invalidTextInputPromptsForAnotherNumber() {

        AssessmentInput input = createInput(
                "Data loss\nabc\n4\n3\nBackup plan\n");

        Risk risk = input.addRisk();

        assertEquals(4, risk.getProbability());
        assertEquals(3, risk.getImpact());
        assertEquals(12, risk.calculateRiskScore());
    }

    @Test
    void outOfRangeRatingsAreRejected() {

        AssessmentInput input = createInput(
                "Security risk\n0\n6\n5\n-1\n3\nSecurity review\n");

        Risk risk = input.addRisk();

        assertEquals(5, risk.getProbability());
        assertEquals(3, risk.getImpact());
        assertEquals(15, risk.calculateRiskScore());
    }
}
