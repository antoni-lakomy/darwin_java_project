package oganisms;

import agh.ics.oop.organisms.Animal;
import agh.ics.oop.organisms.AnimalBuilder;
import agh.ics.oop.records.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTest {

    @Test
    void animalCountDescendantsTest() {

        //Given created animal1 descendants tree
        AnimalBuilder builder = new AnimalBuilder();

        Byte[] genome = new Byte[]{0, 1, 2, 3, 4, 5, 6, 7, 0, 7};

        Animal animal1 = builder.buildBase(new Vector2d(2, 3), 100, genome);
        Animal animal2 = builder.buildBase(new Vector2d(2, 4), 200, genome);
        Animal animal3 = builder.buildBase(new Vector2d(2, 4), 200, genome);
        Animal animal4 = builder.buildBase(new Vector2d(2, 5), 400, genome);
        Animal animal5 = builder.buildBase(new Vector2d(2, 5), 400, genome);

        animal1.addChild(animal2);
        animal1.addChild(animal3);
        animal1.addChild(animal5);
        animal2.addChild(animal3);
        animal2.addChild(animal4);
        animal4.addChild(animal5);

        //When
        int descendants = animal1.countDescendants();

        //Then checking if our method counted descendants correctly, should be 4
        assertEquals(4, descendants);
    }

    @Test
    void animalWithoutDescendantsCountDescendantsTest() {

        //Given created animal, with no descendants
        AnimalBuilder builder = new AnimalBuilder();

        Byte[] genome = new Byte[]{0, 1, 2, 3, 4, 5, 6, 7, 0, 7};

        Animal animal = builder.buildBase(new Vector2d(2, 3), 100, genome);

        //When
        int descendants = animal.countDescendants();

        //Then checking if our method counted descendants correctly, should be 0
        assertEquals(0, descendants);
    }
}
