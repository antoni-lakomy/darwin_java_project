package agh.ics.oop.maps;

import agh.ics.oop.model.GeneInterpreter;
import agh.ics.oop.model.WorldTile;
import agh.ics.oop.organisms.Animal;
import agh.ics.oop.organisms.AnimalBuilder;
import agh.ics.oop.organisms.Organism;
import agh.ics.oop.organisms.Plant;
import agh.ics.oop.records.Vector2d;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWorldMap implements WorldMap {
    protected int width;
    protected int height;
    protected WorldTile[][] map;
    protected final GeneInterpreter geneInterpreter;
    protected int plantCount;


    public AbstractWorldMap(int width, int height, GeneInterpreter geneInterpreter) {
        this.width = width;
        this.height = height;
        this.plantCount = 0;
        this.geneInterpreter = geneInterpreter;
        this.map = new WorldTile[width][height];
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                map[x][y] = new WorldTile();
            }
        }
    }

    @Override
    public int getPlantCount(){ return plantCount; }

    @Override
    public boolean isFieldEmpty(Vector2d position) {
        return map[position.x()][position.y()].isEmpty();
    }

    @Override
    public Organism getOrganismAt(Vector2d position) {
        return map[position.x()][position.y()].getOrganism();
    }

    @Override
    public Animal getAnimalAt(Vector2d position) {
        return map[position.x()][position.y()].getAnimal();
    }

    @Override
    public Plant getPlantAt(Vector2d position) {
        return map[position.x()][position.y()].getPlant();
    }

    @Override
    public void addAnimal(Animal animal) {
        Vector2d poz = animal.getPosition();
        map[poz.x()][poz.y()].addAnimal(animal);
    }

    @Override
    public void removeAnimal(Animal animal) {
        Vector2d poz = animal.getPosition();
        map[poz.x()][poz.y()].removeAnimal(animal);
    }

    @Override
    public void addPlant(Plant plant) {
        Vector2d poz = plant.getPosition();
        map[poz.x()][poz.y()].addPlant(plant);
        plantCount++;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public int calculateEmptyTiles() {
        int cnt = 0;
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                if (map[x][y].isEmpty()) {cnt++;}
            }
        }
        return cnt;
    }

    @Override
    public Vector2d boundPosition(Vector2d newPosition, Animal animal) {
        if (newPosition.y() >= height) {
            newPosition = animal.getPosition();
            animal.rotate(4);
        }
        if (newPosition.y() < 0) {
            newPosition = animal.getPosition();
            animal.rotate(4);
        }
        if (newPosition.x() >= width) newPosition = new Vector2d(0,newPosition.y());
        if (newPosition.x() < 0) newPosition = new Vector2d(width-1, newPosition.y());
        return newPosition;
    }



    @Override
    public List<Vector2d> consumePlants() {
        List<Vector2d> positions = new LinkedList<>();

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){

                if (map[x][y].tryToConsumePlant()) {
                    positions.addFirst(new Vector2d(x, y));
                    plantCount--;
                }
            }
        }

        return positions;
    }

    @Override
    public List<Animal> reproduceAnimals(AnimalBuilder animalBuilder, int animalFedThreshold) {
        List<Animal> newAnimals = new LinkedList<>();

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                newAnimals.addAll(map[x][y].tryToMultiply(animalBuilder,animalFedThreshold));
            }
        }

        return newAnimals;
    }

    @Override
    public List<Animal> removeDead() {
        List<Animal> deadAnimals = new LinkedList<>();

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                deadAnimals.addAll(map[x][y].removeDeadAnimals());
            }
        }

        return deadAnimals;
    }





    @Override
    public void moveAnimal(Animal animal) {
        int energyRequired = calculateEnergy(animal.getPosition().y());
        Vector2d moveVector = animal.activateGene(geneInterpreter,energyRequired);
        Vector2d oldPosition = animal.getPosition();
        Vector2d newPosition;
        if (!animal.isSkippingMove()) {
            newPosition = animal.getPosition().add(moveVector);
            newPosition = boundPosition(newPosition, animal);
        } else {
            newPosition = oldPosition;
            animal.setSkippingMove(false);
        }

        if (!map[oldPosition.x()][oldPosition.y()].removeAnimal(animal))
            throw new NullPointerException("Animal was not present on the map");

        map[newPosition.x()][newPosition.y()].addAnimal(animal);
        animal.setPosition(newPosition);
    }

    //  Divides between children classes as basic map (Globe - kula ziemska) and Poles modifier (Bieguny)
    @Override
    public abstract int calculateEnergy(int positionY);





}

