package model;

import agh.ics.oop.enums.MapDirection;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.Globe;
import agh.ics.oop.model.GeneInterpreter;
import agh.ics.oop.model.OldnessSadness;
import agh.ics.oop.organisms.Animal;
import agh.ics.oop.organisms.AnimalBuilder;
import agh.ics.oop.records.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OldnessSadnessTest {

    GeneInterpreter geneInterpreter = new OldnessSadness();

    @Test
    void olderAnimalIs_MoreMovesItSkipsTest() {
        // Given animals with the same starting positions, both moving constantly North
        AnimalBuilder builder = new AnimalBuilder();
        Byte[] genome = new Byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        Animal animal1 = builder.buildBase(new Vector2d(30, 30), 100, genome);
        Animal animal2 = builder.buildBase(new Vector2d(30, 30), 100, genome);

        animal1.setOrientation(MapDirection.NORTH);
        animal2.setOrientation(MapDirection.NORTH);

        animal1.setCurrentGene(0);
        animal2.setCurrentGene(0);

        animal1.setAge(99);
        animal2.setAge(5);

        AbstractWorldMap map = new Globe(100, 100, geneInterpreter);

        map.addAnimal(animal1);
        map.addAnimal(animal2);

        // When trying to move for 30 turns
        for (int i = 0; i < 30; i++) {
            map.moveAnimal(animal1);
            map.moveAnimal(animal2);
        }

        // Then animal1 travelled shorter distance (is currently farther from the North Pole) compared to animal2 (it skipped more moves due to its age)
        assertTrue(animal1.getPosition().y() < animal2.getPosition().y(), "Expected for animal1 to move less to North, because it is older, hence it is more likely to skip its move");

    }

    @Test
    void doesSkippingGeneComeWithSkippingMoveTest() {
        // Given animal that should be infinitely walking on rectangle [(30,29), (30, 32), (32,29), (32,32) ] edges if it didn't skip any gen
        AnimalBuilder builder = new AnimalBuilder();
        Byte[] genome = new Byte[]{0, 0, 2, 0, 2, 0, 0, 2, 0, 2};

        Animal animal = builder.buildBase(new Vector2d(30, 30), 100, genome);

        animal.setOrientation(MapDirection.NORTH);

        animal.setCurrentGene(0);

        animal.setAge(99);

        AbstractWorldMap map = new Globe(100, 100, geneInterpreter);

        map.addAnimal(animal);

        // When trying to move for 300 turns
        for (int i = 0; i < 300; i++) {
            map.moveAnimal(animal);
        }


        Vector2d[] rectangle = new Vector2d[]{new Vector2d(30, 29), new Vector2d(30, 30), new Vector2d(30, 31),
                new Vector2d(30, 32), new Vector2d(32, 29), new Vector2d(32, 30), new Vector2d(32, 31),
                new Vector2d(32, 32), new Vector2d(31,32), new Vector2d(31,29)};

        // Then animal doesn't stand on the original rectangle edge, which proves that it had to skip some of its genes in order to get out of its mother route.
        assertFalse(Arrays.asList(rectangle).contains(animal.getPosition()), "Expected for animal to not stand on given rectangle edge");

    }
}