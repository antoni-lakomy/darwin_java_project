package maps;

import agh.ics.oop.enums.MapDirection;
import agh.ics.oop.model.FullPredestination;
import agh.ics.oop.model.GeneInterpreter;
import agh.ics.oop.organisms.Animal;
import agh.ics.oop.organisms.AnimalBuilder;
import agh.ics.oop.records.Vector2d;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.Globe;

class AbstractWorldMapTest {

    GeneInterpreter geneInterpreter = new FullPredestination();

    @Test
    void givenEmptyMap_whenCheckingIfFieldIsEmpty_thenReturnsTrue() {
        // Given
        AbstractWorldMap map = new Globe(10, 10, geneInterpreter);

        // When
        boolean isEmpty = map.isFieldEmpty(new Vector2d(5, 5));

        // Then
        assertTrue(isEmpty, "Expected field (5, 5) to be empty on initialization.");
    }

    @Test
    void normalAnimalMoveTest() {
        // Given
        AnimalBuilder builder = new AnimalBuilder();
        Byte[] genome = new Byte[]{2, 2, 4, 2, 6, 4, 3, 7, 2, 3};

        Animal animal1 = builder.buildBase(new Vector2d(3, 2), 100, genome);
        Animal animal2 = builder.buildBase(new Vector2d(7, 4), 100, genome);

        animal1.setOrientation(MapDirection.NORTH);
        animal2.setOrientation(MapDirection.SOUTH_WEST);

        animal1.setCurrentGene(0);
        animal2.setCurrentGene(0);

        AbstractWorldMap map = new Globe(10, 10, geneInterpreter);

        map.addAnimal(animal1);
        map.addAnimal(animal2);

        // When
        map.moveAnimal(animal1);
        map.moveAnimal(animal2);


        // Then
        assertEquals(new Vector2d(4, 2), animal1.getPosition(), "Expected to move normally");
        assertEquals(new Vector2d(6, 5), animal2.getPosition(), "Expected to move normally");
        assertEquals(MapDirection.EAST, animal1.getOrientation(), "Expected to rotate towards its moving direction");
        assertEquals(MapDirection.NORTH_WEST, animal2.getOrientation(), "Expected to rotate towards its moving direction");
    }

    @Test
    void givenOccupiedField_whenCheckingIfFieldIsEmpty_thenReturnsFalse() {
        // Given
        Animal animal = new Animal(new Vector2d(5, 5));
        AbstractWorldMap map = new Globe(10, 10, geneInterpreter);
        map.addAnimal(animal);

        // When
        boolean isEmpty = map.isFieldEmpty(new Vector2d(5, 5));

        // Then
        assertFalse(isEmpty, "Expected field (5, 5) to be occupied.");
    }

    @Test
    void givenMapWithBoundaries_whenMovingOutOfHorizontalBounds_thenAnimalRotates() {
        // Given
        AnimalBuilder builder = new AnimalBuilder();
        Byte[] genome = new Byte[]{0, 2, 4, 2, 6, 4, 3, 7, 2, 3};

        Animal animal1 = builder.buildBase(new Vector2d(5, 9), 100, genome);
        Animal animal2 = builder.buildBase(new Vector2d(0, 0), 100, genome);

        animal1.setOrientation(MapDirection.NORTH);
        animal2.setOrientation(MapDirection.SOUTH_WEST);

        animal1.setCurrentGene(0);
        animal2.setCurrentGene(0);

        AbstractWorldMap map = new Globe(10, 10, geneInterpreter);

        map.addAnimal(animal1);
        map.addAnimal(animal2);

        // When
        map.moveAnimal(animal1);
        map.moveAnimal(animal2);


        // Then
        assertEquals(new Vector2d(5, 9), animal1.getPosition(), "Expected to not cross the border");
        assertEquals(new Vector2d(0, 0), animal2.getPosition(), "Expected to not cross the border");
        assertEquals(MapDirection.SOUTH, animal1.getOrientation(), "Expected to rotate towards its moving direction");
        assertEquals(MapDirection.NORTH_EAST, animal2.getOrientation(), "Expected to rotate towards its moving direction");
    }

    @Test
    void givenMapWithBoundaries_whenMovingOutOfVerticalBounds_thenAnimalAppearsOnOtherSide() {
        // Given
        AnimalBuilder builder = new AnimalBuilder();
        Byte[] genome = new Byte[]{7, 2, 4, 2, 6, 4, 3, 7, 2, 3};

        Animal animal1 = builder.buildBase(new Vector2d(9, 4), 100, genome);
        Animal animal2 = builder.buildBase(new Vector2d(0, 0), 100, genome);

        animal1.setOrientation(MapDirection.SOUTH_EAST);
        animal2.setOrientation(MapDirection.NORTH);

        animal1.setCurrentGene(0);
        animal2.setCurrentGene(0);

        AbstractWorldMap map = new Globe(10, 10, geneInterpreter);

        map.addAnimal(animal1);
        map.addAnimal(animal2);

        // When
        map.moveAnimal(animal1);
        map.moveAnimal(animal2);


        // Then
        assertEquals(new Vector2d(0, 4), animal1.getPosition(), "Expected to not cross the border");
        assertEquals(new Vector2d(9, 1), animal2.getPosition(), "Expected to not cross the border");
        assertEquals(MapDirection.EAST, animal1.getOrientation(), "Expected to rotate as the genome says");
        assertEquals(MapDirection.NORTH_WEST, animal2.getOrientation(), "Expected to rotate as the genome says");
    }





    @Test
    void givenAnimalOnMap_whenRemovingAnimal_thenFieldBecomesEmpty() {
        // Given
        Animal animal = new Animal(new Vector2d(5, 5));
        AbstractWorldMap map = new Globe(10, 10, geneInterpreter);
        map.addAnimal(animal);

        // When
        map.removeAnimal(animal);

        // Then
        assertTrue(map.isFieldEmpty(new Vector2d(5,5)), "Expected field (5, 5) to be empty after removing the animal.");
    }
}