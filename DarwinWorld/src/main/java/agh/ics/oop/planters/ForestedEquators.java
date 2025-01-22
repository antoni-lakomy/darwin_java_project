package agh.ics.oop.planters;

import agh.ics.oop.maps.WorldMap;
import agh.ics.oop.organisms.Plant;
import agh.ics.oop.records.Vector2d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * The default Planter where the preferred plant positions
 * are located between equators.
 */
public class ForestedEquators implements Planter{

    private final WorldMap map;

    private final Random rng;

    private final int plantEnergy;

    private List<Vector2d> preferredTiles;

    private List<Vector2d> normalTiles;

    private final float minEquator;

    private final float maxEquator;


    @Override
    public List<Vector2d> getPreferredTiles() {return this.preferredTiles;}

    public ForestedEquators(WorldMap map, int plantEnergy, long seed){
        this.map = map;
        this.rng = new Random(seed);
        this.plantEnergy = plantEnergy;
        this.minEquator = 0.4f * map.getHeight();
        this.maxEquator = 0.6f * map.getHeight();

        this.preferredTiles = new ArrayList<>();
        this.normalTiles = new ArrayList<>();

        for (int y = 0; y < map.getHeight(); y++){
            List<Vector2d> current = normalTiles;
            if (y >= minEquator && y < maxEquator) current = preferredTiles;

            for (int x = 0; x < map.getWidth(); x++){
                current.addLast(new Vector2d(x,y));
            }
        }
    }


    private List<Vector2d> plantFromList(int numberToPlant, List<Vector2d> tiles){
        HashSet<Vector2d> vectorsToPlant = new HashSet<>();

        //Floyd's random subset algorithm.
        for (int i = tiles.size() - numberToPlant; i < tiles.size(); i++) {
            Vector2d poz = tiles.get(rng.nextInt(i + 1));
            if (vectorsToPlant.contains(poz)) {
                 vectorsToPlant.add(tiles.get(i));
            } else {
                vectorsToPlant.add(poz);
            }
        }

        for (Vector2d spot : vectorsToPlant.stream().toList()){
            map.addPlant(new Plant(spot,plantEnergy));
        }
        return tiles.stream().filter(vector -> !vectorsToPlant.contains(vector)).toList();
    }




    @Override
    public void plant(int numberToPlant) {
        int preferred = Math.min(Math.round(0.8f*numberToPlant),preferredTiles.size());

        preferredTiles = new ArrayList<>(plantFromList(preferred,preferredTiles));

        int normal = Math.min(normalTiles.size(),numberToPlant - preferred);

        normalTiles = new ArrayList<>(plantFromList(normal,normalTiles));
    }

    @Override
    public void consume() {
        List<Vector2d> positionsFreed = map.consumePlants();

        for (Vector2d position : positionsFreed){
            if (minEquator <= position.y() && position.y() < maxEquator){
                preferredTiles.addLast(position);
            } else {
                normalTiles.addLast(position);
            }
        }
    }
}
