package agh.ics.oop.maps;

import agh.ics.oop.model.GeneInterpreter;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Poles extends AbstractWorldMap {

    public Poles(int width, int height, GeneInterpreter geneInterpreter) {
        super(width, height, geneInterpreter);
    }

    // Closer it gets to Pole, higher cost of move gets, except equators, where energy required is always 1.
    // Since an animal stepped out of the equator - its move energy rises gradually starting at 2, up to maxMoveCost.
    @Override
    public int calculateEnergy(int positionY) {
        int maxValidDistance = (int) (0.4f * this.height);
        int distance = min(positionY, this.height - positionY - 1);
        if (distance >= maxValidDistance) {return 1;}

        int maxMoveCost = 10;
        return max(2, (int)((float) maxMoveCost - ((float)distance / maxValidDistance * maxMoveCost)));
    }

}