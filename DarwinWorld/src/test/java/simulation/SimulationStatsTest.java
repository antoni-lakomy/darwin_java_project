package simulation;

import agh.ics.oop.organisms.Animal;
import agh.ics.oop.organisms.AnimalBuilder;
import agh.ics.oop.records.SimParams;
import agh.ics.oop.records.Vector2d;
import agh.ics.oop.simulation.Simulation;
import agh.ics.oop.simulation.SimulationBuilder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class SimulationStatsTest {
    // Builds a simulation with given parameters
    SimParams params = new SimParams(11,10,10,
            0,10,10,
            3,0,5,
            30,20,10,
            10, 0,0,4,0);



    @Test
    void givenSimulation_withAnimalsLifeCycle_CorrectAnimalsCount() {
        // Given simulation where some new animals were born and some died
        Simulation simulation = SimulationBuilder.build(params);
        simulation.addElemToAliveAnimals(new Animal(new Vector2d(2,2)));
        simulation.addElemToAliveAnimals(new Animal(new Vector2d(4,8)));
        Animal animalToRemove = simulation.getAliveAnimal(0);
        simulation.removeElemFromAliveAnimals(animalToRemove);
        // When
        int aliveAnimalsCount = simulation.getAliveAnimalsSize();

        // Then simulation started with 5 animals, 2 were born and 1 died, so the total amount of alive animals should be 6.
        assertEquals(6, aliveAnimalsCount, "Expected count of alive animals to be 6.");
    }

    @Test
    void givenSimulation_whenGetPlantCountCalled_thenReturnsCorrectCount() {
        // Given simulation with initial params and planted additional 3 plants
        Simulation simulation = SimulationBuilder.build(params);
        simulation.getPlanter().plant(3);

        // When
        int plantCount = simulation.getMap().getPlantCount();

        // Then initializing our simulation we place 10 plants on it, then we add another 3 of them, so the plant count should equal 13
        assertEquals(13, plantCount, "Expected initial plant count to be 13.");
    }

    @Test
    void givenSimulation_whenCalculateEmptyTilesCalled_thenReturnsCorrectCount() {
        // Given simulation with initial 14 empty tiles, then we add 1 animal on empty tile, and 3 plants,
        // one of them is being placed on tile already occupied by an animal (it doesn't decrease the amount of empty tiles)
        Simulation simulation = SimulationBuilder.build(params);
        simulation.getMap().addAnimal(new Animal(new Vector2d(2,2)));
        simulation.getPlanter().plant(3);

        // When
        int emptyTiles = simulation.getMap().calculateEmptyTiles();

        // Then 100 - 14 - 1 - 2 (instead 3) = 83
        assertEquals(83, emptyTiles, "Expected number of empty tiles to be 83 (17 occupied).");
    }
//
    @Test
    void givenSimulation_whenFindMostPopularGenomeCalled_thenReturnsCorrectGenome() {
        // Given a simulation with initial parameters, delete all animals, adds 2 new animals with similar genome and 1 with different.
        AnimalBuilder builder = new AnimalBuilder();
        Simulation simulation = SimulationBuilder.build(params);
        Byte[] genome1 = new Byte[]{0, 1, 2, 3, 4, 5, 6, 7, 0, 7};
        Byte[] genome2 = new Byte[]{0, 1, 2, 3, 4, 5, 6, 7, 0, 7};
        Byte[] genome3 = new Byte[]{0, 2, 2, 2, 4, 5, 6, 7, 0, 7};


        Animal animal1 = builder.buildBase(new Vector2d(2, 3), 100, genome1);
        Animal animal2 = builder.buildBase(new Vector2d(2, 4), 100, genome2);
        Animal animal3 = builder.buildBase(new Vector2d(2, 5), 100, genome3);
        List<Animal> newAnimals = List.of(animal1, animal2, animal3);

        simulation.updateGenomeFrequency(simulation.getAliveAnimals(), newAnimals);

        // When
        Byte[] mostPopularGenome = simulation.findMostPopularGenome();


        // Then
        assertArrayEquals(genome1, mostPopularGenome, "Expected the most popular genome to be genome1.");
    }


    @Test
    void givenSimulation_whenFindMostPopularGenomeCalledWhileNoAnimals_thenReturnsEmptyArray() {
        // Given a simulation with initial parameters (no plants at all, 1 animal), runs 100 turns (animal will be dead)
        // Checks if most popular genome returns []
        SimParams params2 = new SimParams(11,10,10,
                0,0,10,
                0,0,1,
                30,20,10,
                10, 0,0,4,0);

        Simulation simulation = SimulationBuilder.build(params2);

        for (int i = 0; i < 100; i++) {
            simulation.simulationStep();
        }

        // When
        Byte[] mostPopularGenome = simulation.findMostPopularGenome();

        // Then
        assertArrayEquals(new Byte[0], mostPopularGenome, "Expected the most popular genome to be genome1.");
    }

    @Test
    void givenSimulationWithNoAnimals_whenFindMostPopularGenomeCalled_thenReturnsEmptyArray() {
        // Given a simulation with initial parameters (no plants at all, no animals),
        // Checks if most popular genome returns []
        SimParams params2 = new SimParams(11,10,10,
                0,0,10,
                0,0,0,
                30,20,10,
                10, 0,0,4,0);

        Simulation simulation = SimulationBuilder.build(params2);

        // When
        Byte[] mostPopularGenome = simulation.findMostPopularGenome();

        // Then as the result of the most popular genome we get a genome that appears the most times (2) among 3 animals.
        assertArrayEquals(new Byte[0], mostPopularGenome, "Expected the most popular genome to be genome1.");
    }


    @Test
    void givenSimulation_whenCalcAvgEnergyCalled_thenReturnsCorrectAverage() {
        // Given a simulation with initial parameters, 5 animals with energy 30 each already exist.
        // We add 3 animals with energy as follows 100, 200 and 400.

        AnimalBuilder builder = new AnimalBuilder();
        Simulation simulation = SimulationBuilder.build(params);

        Byte[] genome = new Byte[]{0, 1, 2, 3, 4, 5, 6, 7, 0, 7};

        Animal animal1 = builder.buildBase(new Vector2d(2, 3), 100, genome);
        Animal animal2 = builder.buildBase(new Vector2d(2, 4), 200, genome);
        Animal animal3 = builder.buildBase(new Vector2d(2, 5), 400, genome);
        simulation.addElemToAliveAnimals(animal1);
        simulation.addElemToAliveAnimals(animal2);
        simulation.addElemToAliveAnimals(animal3);

        // When
        float avgEnergy = simulation.calcAvgEnergy();

        // Then average energy of all 8 animals should equal 106
        assertEquals(106.25, avgEnergy, "Expected average energy to be 106.25");
    }

    @Test
    void givenSimulation_whenCalcAvgLifespanCalled_thenReturnsCorrectAverage() {
        // Given a simulation with initial parameters, 0 animals dead yet.
        // We add 3 dead animals with age as follows 50, 100 and 250.

        AnimalBuilder builder = new AnimalBuilder();
        Simulation simulation = SimulationBuilder.build(params);

        Byte[] genome = new Byte[]{0, 1, 2, 3, 4, 5, 6, 7, 0, 7};

        Animal animal1 = builder.buildBase(new Vector2d(2, 3), 100, genome);
        Animal animal2 = builder.buildBase(new Vector2d(2, 4), 200, genome);
        Animal animal3 = builder.buildBase(new Vector2d(2, 5), 400, genome);
        simulation.addElemToDeadAnimals(animal1);
        simulation.addElemToDeadAnimals(animal2);
        simulation.addElemToDeadAnimals(animal3);
        animal1.setAge(50);
        animal2.setAge(100);
        animal3.setAge(252);

        // When
        float avgLifespan = simulation.calcAvgLifespan();

        // Then the average age of all dead animals should equal 133
        assertEquals(134.0, avgLifespan, "Expected average lifespan to be 134 days.");
    }

    @Test
    void givenSimulation_whenCalcAvgChildrenCalled_thenReturnsCorrectAverage() {
        // Given a simulation with initial parameters, 5 animals each has no kids.
        // We add 5 animals two of them have 2 kids, one of them has 1 kid, rest has no kids.
        AnimalBuilder builder = new AnimalBuilder();
        Simulation simulation = SimulationBuilder.build(params);

        Byte[] genome = new Byte[]{0, 1, 2, 3, 4, 5, 6, 7, 0, 7};

        Animal animal1 = builder.buildBase(new Vector2d(2, 3), 100, genome);
        Animal animal2 = builder.buildBase(new Vector2d(2, 4), 200, genome);
        Animal animal3 = builder.buildBase(new Vector2d(2, 4), 200, genome);
        Animal animal4 = builder.buildBase(new Vector2d(2, 5), 400, genome);
        Animal animal5 = builder.buildBase(new Vector2d(2, 5), 400, genome);

        simulation.addElemToAliveAnimals(animal1);
        simulation.addElemToAliveAnimals(animal2);
        simulation.addElemToAliveAnimals(animal3);
        simulation.addElemToAliveAnimals(animal4);
        simulation.addElemToAliveAnimals(animal5);

        animal1.addChild(animal2);
        animal1.addChild(animal3);
        animal2.addChild(animal3);
        animal2.addChild(animal4);
        animal4.addChild(animal5);

        // When
        float avgChildren = simulation.calcAvgChildren();

        // Then the average kids per animal should equal 0.5 (5/10)
        assertEquals(0.5, avgChildren, "Expected average number of children to be 0.5");
    }
}