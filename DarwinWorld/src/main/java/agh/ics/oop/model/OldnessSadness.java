package agh.ics.oop.model;

import agh.ics.oop.maps.WorldMap;
import agh.ics.oop.organisms.Animal;
import agh.ics.oop.maps.Poles;
import agh.ics.oop.maps.Globe;
import java.util.Random;

/** Always returns the next gene after the current one, but also each time draws if the animal is going to move this turn
 * judging on its age. If the move is blocked, animal.SkippingTurn attribute is being set to {@true}, which is then validated
 * in the {@link WorldMap}. Also inside it is being changed back to the {@false} at the end of the turn.
 * Eventually animal finishes turn with pointer at the next gene and energy reduced by potential moving cost even if it didn't actually move.
 */
public class OldnessSadness implements  GeneInterpreter {

    @Override
    public int getNextGene(Animal animal) {
        execPotentialSkip(animal);
        return (animal.getCurrentGene() + 1 ) % animal.getGenome().length;
    }

    public void execPotentialSkip(Animal animal) {
        Random rand = new Random();
        int animalAge = animal.getAge();
        int odds = 80;
        // calculates % part of animalAge of boundAge and decreases it by 20% to make it cap at 80%
        // age at which changes of skipping a move hit 80% (They stop to grow)
        int boundAge = 100;
        if (animalAge < boundAge) {
            odds = (int) (((float)animalAge / boundAge) * 100 * 0.8f);
        }
        int result = rand.nextInt(100);
        if (result < odds) {animal.setSkippingMove(true);}
    }
}
