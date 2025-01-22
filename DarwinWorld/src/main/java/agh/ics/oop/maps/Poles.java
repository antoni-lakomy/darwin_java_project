package agh.ics.oop.maps;

import agh.ics.oop.model.GeneInterpreter;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * A variant of the {@link WorldMap} with cold Poles.
 * Animals need varying levels of energy to move depending on their y position.
 * The closer they get to the poles, the higher the cost of their moves get, except for the Tropics,
 * where the energy required is always 1.
 * When animals step out of the Tropics energy required rises gradually starting at 2, up to maxMoveCost.
 */
public class Poles extends AbstractWorldMap {

    public Poles(int width, int height, GeneInterpreter geneInterpreter) {
        super(width, height, geneInterpreter);
    }

    @Override
    public int calculateEnergy(int positionY) {
        int maxValidDistance = (int) (0.4f * this.height);
        int distance = min(positionY, this.height - positionY - 1);
        if (distance >= maxValidDistance) {return 1;}

        int maxMoveCost = 10;
        return max(2, (int)((float) maxMoveCost - ((float)distance / maxValidDistance * maxMoveCost)));
    }

}