package agh.ics.oop.maps;

import agh.ics.oop.organisms.Animal;
import agh.ics.oop.organisms.AnimalBuilder;
import agh.ics.oop.organisms.Organism;
import agh.ics.oop.organisms.Plant;
import agh.ics.oop.records.Vector2d;

import java.util.List;

public interface WorldMap {

    int getWidth();

    int getHeight();

    /**
     * Checks if the tile on the given position is empty.
     *
     * @param position The position to check.
     * @return true if the tile is empty, false otherwise.
     */
    boolean isFieldEmpty(Vector2d position);

    /**
     * Gets the first organism from the tile at a given position.
     *
     * @param position The position to check.
     * @return The organism at the given position
     */
    Organism getOrganismAt(Vector2d position);

    /**
     * Gets the strongest animal from the tile at a given position.
     *
     * @param position The position to check.
     * @return The animal at the given position
     */
    Animal getAnimalAt(Vector2d position);

    /**
     * Gets the plant from the tile at a given position.
     *
     * @param position The position to check.
     * @return The animal at the given position
     */
    Plant getPlantAt(Vector2d position);

    /**
     * Adds an animal to the map.
     * @param animal {@link Animal} to be added.
     */
    void addAnimal(Animal animal);

    /**
     * Removes an animal from the map.
     * @param animal {@link Animal} to be removed.
     */
    void removeAnimal(Animal animal);

    /**
     * Adds a plant to the map.
     * Throws an exception if there already is a plant on this spot on the map.
     *
     * @param plant {@link Plant} to be added.
     *
     * @throws IllegalStateException if there already is a plant
     *                               on the same position on the map.
     */
    void addPlant(Plant plant);

    /**
     * Binds the provided position to the map borders including
     * special map features.
     * If animal tries to cross the Pole, it stays on current field and rotates 180 deg.
     * If it passes side boundaries it appears on the other side.
     *
     * @param newPosition The position to bind.
     * @return A {@link Vector2d} containing the bound position.
     */
    Vector2d boundPosition(Vector2d newPosition, Animal animal);


    /**
     * Moves an animal on the map according to its genome
     * and this maps specifications.
     *
     * @param animal {@link Animal} to move.
     *
     * @throws NullPointerException if the animal was not present on the map.
     */
    void moveAnimal(Animal animal);


    /**
     * Initiates the consumption faze of the simulation.
     * Tries to consume plants on every tile of the map.
     * The spots where the plant was successfully consumed
     * are returned as a list of positions.
     *
     * @return A list of positions on the map where a plant was successfully consumed.
     */
    List<Vector2d> consumePlants();

    /**
     * Initiates the reproduction faze of the simulation.
     * Tries to reproduce animals on every tile of the map.
     * The newly created children are returned as a list.
     *
     * @param animalBuilder The {@link AnimalBuilder} used for reproduction of the animals.
     * @param animalFedThreshold The amount of energy animal has to have to be considered fed.
     *
     * @return A list of children created in this faze.
     */
    List<Animal> reproduceAnimals(AnimalBuilder animalBuilder,int animalFedThreshold);

    /**
     * Removes the dead animals from the map.
     * An animal is considered dead if its energy has dropped to zero.
     *
     * @return A list of the removed animals
     */
    List<Animal> removeDead();

    int calculateEnergy(int PositionY);

    int calculateEmptyTiles();

    //Getter of plantCount attribute.
    int getPlantCount();
}
