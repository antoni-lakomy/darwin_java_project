package agh.ics.oop.model;

import agh.ics.oop.organisms.Animal;

import java.util.Comparator;

/**
 * A singleton class used for sorting the {@link Animal} objects in the correct order,
 * from the strongest to the weakest.
 * Its single instance can be used as a {@link Comparator} where needed.
 */
public class AnimalComparator implements Comparator<Animal> {

    private AnimalComparator(){}

    private static volatile AnimalComparator instance;

    @Override
    public int compare(Animal animal1, Animal animal2) {
        if (animal1.getEnergy() != animal2.getEnergy()) return animal2.getEnergy() - animal1.getEnergy();
        if (animal1.getAge() != animal2.getAge()) return animal2.getAge() - animal1.getAge();
        return animal2.getChildren().size() - animal1.getChildren().size();
    }

    /**
     * Returns the single instance of this object to be passed to another function.
     * This is the only way to use this {@link Comparator} as its object cannon
     * be created using a constructor.
     * It should be thread and memory safe.
     *
     * @return The single instance of {@link AnimalComparator} object.
     */
    public static AnimalComparator getInstance(){
        if (instance == null){
            synchronized (AnimalComparator.class){
                if (instance == null){
                    instance = new AnimalComparator();
                }
            }
        }
        return instance;
    }
}
