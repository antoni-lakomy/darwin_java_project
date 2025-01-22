package simulation;

import agh.ics.oop.records.SimParams;
import agh.ics.oop.simulation.Simulation;
import agh.ics.oop.simulation.SimulationBuilder;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulationTest {
    SimParams params = new SimParams(11, 10, 10,
            0, 10, 10,
            3, 0, 5,
            30, 20, 10,
            10, 0, 0, 4, 0);


    Simulation simulation = SimulationBuilder.build(params);


    @Test
    void GeneralSimulationTest() {
        //Given 1000 days of simulation
        for (int i = 0; i < 1000; i++) {
            simulation.simulationStep();
        }

        //When getting all simulation statistics
        int aliveAnimalsCount = simulation.getAliveAnimalsSize();
        int plantCount = simulation.getMap().getPlantCount();
        int emptyTiles = simulation.getMap().calculateEmptyTiles();
        Byte[] mostPopularGenome = simulation.findMostPopularGenome();
        float avgEnergy = simulation.calcAvgEnergy();
        float avgLifespan = simulation.calcAvgLifespan();
        float avgChildren = simulation.calcAvgChildren();





        //Then all statistics should be equal to specific values. Thanks to seed we are giving as one of the SimulationBuilder parameter,
        //we can be 100% sure that those data will be the same each time we complete simulation with the same seed given.
        Byte[] genome1 = new Byte[]{5, 3, 2, 3, 5, 3, 2, 0, 4, 1};

        assertEquals(28, aliveAnimalsCount, "Expected count of alive animals to be 6.");

        assertEquals(16, plantCount, "Expected initial plant count to be 13.");

        assertEquals(63, emptyTiles, "Expected number of empty tiles to be 83 (17 occupied).");

        assertArrayEquals(genome1, mostPopularGenome, "Expected the most popular genome to be genome1.");

        assertEquals(22.428571701049805, avgEnergy, "Expected average energy to be 106.25");

        assertEquals(52.82149887084961, avgLifespan, "Expected average lifespan to be 133 days.");

        assertEquals(3.7142856121063232, avgChildren, "Expected average number of children to be 0.5");
    }
}
