package maps;

import agh.ics.oop.enums.MapDirection;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.Poles;
import agh.ics.oop.model.FullPredestination;
import agh.ics.oop.model.GeneInterpreter;
import agh.ics.oop.organisms.Animal;
import agh.ics.oop.organisms.AnimalBuilder;
import agh.ics.oop.records.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolesTest {

    GeneInterpreter geneInterpreter = new FullPredestination();

    @Test
    void closerAnimalGetsToNorthPole_MoreEnergyItConsumesMoving() {
        // Given animals with the same starting energy, but one placed closer to the pole than the other
        AnimalBuilder builder = new AnimalBuilder();
        Byte[] genome = new Byte[]{0, 2, 4, 2, 6, 4, 3, 7, 2, 3};

        Animal animal1 = builder.buildBase(new Vector2d(30, 81), 100, genome);
        Animal animal2 = builder.buildBase(new Vector2d(35, 62), 100, genome);

        animal1.setOrientation(MapDirection.NORTH);
        animal2.setOrientation(MapDirection.NORTH);

        animal1.setCurrentGene(0);
        animal2.setCurrentGene(0);

        AbstractWorldMap map = new Poles(100, 100, geneInterpreter);

        map.addAnimal(animal1);
        map.addAnimal(animal2);

        // When
        map.moveAnimal(animal1);
        map.moveAnimal(animal2);

        // Then
        assertTrue(animal1.getEnergy() < animal2.getEnergy(), "Expected for animal1 to consume more energy because its closer to pole");

    }

    @Test
    void closerAnimalGetsToSouthPole_MoreEnergyItConsumesMoving() {
        // Given animals with the same starting energy, but one placed closer to the pole than the other
        AnimalBuilder builder = new AnimalBuilder();
        Byte[] genome = new Byte[]{0, 2, 4, 2, 6, 4, 3, 7, 2, 3};

        Animal animal1 = builder.buildBase(new Vector2d(30, 21), 100, genome);
        Animal animal2 = builder.buildBase(new Vector2d(35, 39), 100, genome);

        animal1.setOrientation(MapDirection.SOUTH);
        animal2.setOrientation(MapDirection.SOUTH);

        animal1.setCurrentGene(0);
        animal2.setCurrentGene(0);

        AbstractWorldMap map = new Poles(100, 100, geneInterpreter);

        map.addAnimal(animal1);
        map.addAnimal(animal2);

        // When
        map.moveAnimal(animal1);
        map.moveAnimal(animal2);

        // Then
        assertTrue(animal1.getEnergy() < animal2.getEnergy(), "Expected for animal1 to consume more energy because its closer to pole");

    }

    @Test
    void moveCostOnEquatorIsAlwaysOne() {
        // Given animals with the same starting energy, both placed on the equator
        AnimalBuilder builder = new AnimalBuilder();
        Byte[] genome = new Byte[]{0, 2, 4, 2, 6, 4, 3, 7, 2, 3};

        Animal animal1 = builder.buildBase(new Vector2d(30, 50), 100, genome);
        Animal animal2 = builder.buildBase(new Vector2d(35, 59), 100, genome);

        animal1.setOrientation(MapDirection.SOUTH);
        animal2.setOrientation(MapDirection.SOUTH);

        animal1.setCurrentGene(0);
        animal2.setCurrentGene(0);

        AbstractWorldMap map = new Poles(100, 100, geneInterpreter);

        map.addAnimal(animal1);
        map.addAnimal(animal2);

        // When
        map.moveAnimal(animal1);
        map.moveAnimal(animal2);

        // Then
        assertEquals(animal1.getEnergy(), animal2.getEnergy(), "Expected for both animals to consume the same amount of energy");

    }

    @Test
    void testMoveCostOnTheEdgeOfEquator() {
        // Given animal standing on the edge (but outside) the equator, but moving into it.
        // It should consume energy as if it was moving outside the equator (>1)
        AnimalBuilder builder = new AnimalBuilder();
        Byte[] genome = new Byte[]{0, 2, 4, 2, 6, 4, 3, 7, 2, 3};

        Animal animal = builder.buildBase(new Vector2d(35, 60), 100, genome);

        animal.setOrientation(MapDirection.SOUTH);

        animal.setCurrentGene(0);

        AbstractWorldMap map = new Poles(100, 100, geneInterpreter);

        map.addAnimal(animal);

        // When
        map.moveAnimal(animal);

        // Then
        assertEquals(animal.getEnergy(), 98, "Expected to consume 2 energy points");

    }

    @Test
    void testCalculationsOnTheEdgeIfTheMap() {
        // Given animal standing on the edge of map, also trying to move out of it.
        // It should consume max amount of energy, don't cross the border and rotate.
        AnimalBuilder builder = new AnimalBuilder();
        Byte[] genome = new Byte[]{0, 2, 4, 2, 6, 4, 3, 7, 2, 3};

        Animal animal = builder.buildBase(new Vector2d(35, 0), 100, genome);

        animal.setOrientation(MapDirection.SOUTH);

        animal.setCurrentGene(0);

        AbstractWorldMap map = new Poles(100, 100, geneInterpreter);

        map.addAnimal(animal);

        // When
        map.moveAnimal(animal);

        // Then
        assertEquals(animal.getEnergy(), 90, "Expected to consume 10 energy points");
        assertEquals(animal.getPosition(), new Vector2d(35,0), "Expected to stay where it was");
        assertEquals(animal.getOrientation(), MapDirection.NORTH, "Expected to rotate");

    }
}