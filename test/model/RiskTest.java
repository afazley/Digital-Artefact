
package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RiskTest {

    @Test
    void minimumRiskScore() {
        Risk risk = new Risk("Minor delay", 1, 1, "Monitor");

        assertEquals(1, risk.calculateRiskScore());
        assertEquals("Low", risk.getRiskLevel());
    }

    @Test
    void lowRiskUpperBoundary() {
        Risk risk = new Risk("Minor issue", 2, 2, "Monitor");

        assertEquals(4, risk.calculateRiskScore());
        assertEquals("Low", risk.getRiskLevel());
    }

    @Test
    void mediumRiskLowerBoundary() {
        Risk risk = new Risk("Training delay", 1, 5, "Training");

        assertEquals(5, risk.calculateRiskScore());
        assertEquals("Medium", risk.getRiskLevel());
    }

    @Test
    void mediumRiskUpperBoundary() {
        Risk risk = new Risk("Staff disruption", 3, 3, "Communication");

        assertEquals(9, risk.calculateRiskScore());
        assertEquals("Medium", risk.getRiskLevel());
    }

    @Test
    void highRiskLowerBoundary() {
        Risk risk = new Risk("Security breach", 2, 5, "Audit");

        assertEquals(10, risk.calculateRiskScore());
        assertEquals("High", risk.getRiskLevel());
    }

    @Test
    void maximumRiskScore() {
        Risk risk = new Risk("System outage", 5, 5, "Rollback");

        assertEquals(25, risk.calculateRiskScore());
        assertEquals("High", risk.getRiskLevel());
    }
}
