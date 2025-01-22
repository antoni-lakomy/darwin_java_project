package agh.ics.oop.planters;

import agh.ics.oop.maps.WorldMap;
import agh.ics.oop.records.Vector2d;

import java.util.List;

public interface Planter {


    /**
     * Plant a given number of plants on this planters {@link WorldMap}.
     *
     * @param numberToPlant A number of plants to plant.
     */
    void plant(int numberToPlant);


    /**
     * Start the consumption faze of the simulation on this planters {@link WorldMap}
     *
     */
    void consume();

    // Getter of preferredTiles attribute.
    List<Vector2d> getPreferredTiles();
}
