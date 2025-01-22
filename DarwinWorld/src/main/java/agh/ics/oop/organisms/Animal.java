package agh.ics.oop.organisms;

import agh.ics.oop.enums.MapDirection;
import agh.ics.oop.model.GeneInterpreter;
import agh.ics.oop.records.Vector2d;

import java.util.*;

public class Animal extends Organism{

    protected MapDirection orientation;

    protected Byte[] genome;

    protected int currentGene;

    protected int energy;

    protected int age;

    protected int dayBorn;

    protected int plantsEaten;

    protected boolean skippingMove;

    protected List<Animal> parents;

    protected List<Animal> children;

    public Byte[] getGenome() { return genome.clone(); }

    public MapDirection getOrientation() { return  orientation; }

    public void setOrientation(MapDirection orientation) { this.orientation = orientation; } //for testing purpose

    public int getCurrentGene() { return  currentGene; }

    public void setCurrentGene(int currentGene) { this.currentGene = currentGene; } //for testing purpose

    public int getEnergy() { return energy; }

    public int getAge() {return age; }

//    public int getDayBorn() {return dayBorn;}

    public int getFinalDay() {return dayBorn + age;}

    public void setAge(int age) { this.age = age; } //for test purpose

//    public List<Animal> getParents() { return parents; }

    public List<Animal> getChildren() { return children;}

    public void addChild(Animal child) { children.add(child); } //for test purpose

    public int getPlantsEaten() {return plantsEaten;}

    public void addPlantsEaten() {plantsEaten++;}

    public void setSkippingMove(boolean isSkippingMove) { this.skippingMove = isSkippingMove; }

    public boolean isSkippingMove() { return this.skippingMove; }

    public Animal(Vector2d position){
        super(position);
        skippingMove = false;
        plantsEaten = 0;
    }

    /**
     * Counts the total number of descendants of this animal.
     * Descendants include all children, grandchildren, etc., but avoids double-counting.
     * Classic BFS algorithm.
     * @return Total number of unique descendants.
     */
    public int countDescendants() {

        // Add direct children to the queue
        Queue<Animal> queue = new LinkedList<>(this.children);
        Set<Animal> visited = new HashSet<>(this.children);

        while (!queue.isEmpty()) {
            Animal current = queue.poll();

            // Checks children of the current animal
            for (Animal child : current.children) {
                if (!visited.contains(child)) {
                    visited.add(child);
                    queue.add(child);
                }
            }
        }

        return visited.size(); // Total unique descendants
    }


    public void addEnergy(int energy){
        this.energy += energy;
    }

    @Override
    public String toString(){
        return orientation.toString();
    }

    public void rotate(int rotation){ this.orientation = this.orientation.rotate(rotation); }

    /**
     * The first part of animals movement.
     * a) Activates the current gene changing the orientation,
     * b) Changes the current gene to the next one according to the provided {@link GeneInterpreter},
     * c) Drains the specified amount of energy from the animal,
     * d) Makes the animal older by 1,
     * e) Returns the current orientation as a vector on a 2D grid,
     *
     * @param interpreter The interpreter used to set the next active gene.
     * @param energyRequired The energy this move drains from the animal.
     * @return A {@link Vector2d} representing the orientation of the animal.
     */
    public Vector2d activateGene(GeneInterpreter interpreter, int energyRequired){
        orientation = orientation.rotate(genome[currentGene]);
        currentGene = interpreter.getNextGene(this);
        energy = Math.max(energy - energyRequired,0);
        age += 1;
        return orientation.toUnitVector();
    }

    public void setPosition(Vector2d position){
        this.position = position;
    }

}
