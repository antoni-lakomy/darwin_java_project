package agh.ics.oop.maps;

import agh.ics.oop.model.GeneInterpreter;

public class Globe extends AbstractWorldMap {

    public Globe(int width, int height, GeneInterpreter geneInterpreter) {
        super(width, height, geneInterpreter);
    }

    // Default map, every move require 1 energy point.
    @Override
    public int calculateEnergy(int positionY) {
        return 1;
    }


}

