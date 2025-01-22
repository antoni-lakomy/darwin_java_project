package agh.ics.oop.simulation;

import agh.ics.oop.maps.WorldMap;
import agh.ics.oop.observers.SimObserver;
import agh.ics.oop.organisms.Animal;
import agh.ics.oop.organisms.AnimalBuilder;
import agh.ics.oop.planters.Planter;

import java.util.*;


/**
 * Main simulation class, contains most of the logic required for it to function
 * It can only be created through a builder.
 */
public class Simulation implements Runnable {

    protected WorldMap map;

    protected Planter planter;

    protected AnimalBuilder animalBuilder;

    protected List<Animal> aliveAnimals;

    protected List<Animal> deadAnimals;


    protected Map<List<Byte>, Integer> genomeFrequency = new HashMap<>();

    protected int animalFedThreshold;

    protected int plantGrowth;

    protected int simulationDay;

    protected Simulation() {
    }

    private final List<SimObserver> observers = new ArrayList<>();

    private boolean shutDown = false;

    private boolean paused = false;

    private float timePerStep = 0.4f;


    //Precalculated statistics
    private float avgEnergy;
    private float avgLifespan;
    private float avgChildren;
    private Byte[] mostPopularGenome;
    private int plantCount;
    private int emptyTiles;

    public float getAvgEnergy() {
        return avgEnergy;
    }

    public float getAvgLifespan() {
        return avgLifespan;
    }

    public float getAvgChildren() {
        return avgChildren;
    }

    public Byte[] getMostPopularGenome() {
        return mostPopularGenome;
    }

    public int getPlantCount() {
        return plantCount;
    }

    public int getEmptyTiles() {
        return emptyTiles;
    }

    public int getSimulationDay() {
        return simulationDay;
    }

    public void addObserver(SimObserver observer) {
        observers.addLast(observer);
    }

    public boolean removeObserver(SimObserver observer) {
        return observers.remove(observer);
    }

    public void addElemToAliveAnimals(Animal animal) {
        aliveAnimals.add(animal);
    } //for tests purpose

    public void removeElemFromAliveAnimals(Animal animal) {
        aliveAnimals.remove(animal);
    } //for test purpose

    public Animal getAliveAnimal(int index) {
        return aliveAnimals.get(index);
    } //for test purpose

    public List<Animal> getAliveAnimals() {
        return aliveAnimals;
    } //for test purpose

    public int getAliveAnimalsSize() {
        return aliveAnimals.size();
    }

    public int getDeadAnimalsSize() {
        return deadAnimals.size();
    }

    public void addElemToDeadAnimals(Animal animal) {
        deadAnimals.add(animal);
    } //for testing purpose

    public Planter getPlanter() {
        return planter;
    } //for test purpose

    public WorldMap getMap() {
        return map;
    }

    public void updateGenomeFrequency(List<Animal> deadThisStep, List<Animal> newThisStep) {
        // Remove genome frequencies for dead animals
        for (Animal deadAnimal : deadThisStep) {
            List<Byte> genome = Arrays.asList(deadAnimal.getGenome());
            genomeFrequency.put(genome, genomeFrequency.getOrDefault(genome, 0) - 1);

            // Remove genome entry if count reaches zero
            if (genomeFrequency.get(genome) <= 0) {
                genomeFrequency.remove(genome);
            }
        }

        // Add genome frequencies for new animals
        for (Animal newAnimal : newThisStep) {
            List<Byte> genome = Arrays.asList(newAnimal.getGenome());
            genomeFrequency.put(genome, genomeFrequency.getOrDefault(genome, 0) + 1);
        }
    }


    public Byte[] findMostPopularGenome() {
        return genomeFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey) // Returns Optional<Byte[]>
                .map(genome -> genome.toArray(new Byte[0])) // Converts Optional to Array Byte[]
                .orElse(new Byte[0]); // Returns empty array if to genomes present
    }


    public float calcAvgEnergy() {
        int totalEnergy = 0;
        for (Animal animal : aliveAnimals) {
            totalEnergy += animal.getEnergy();
        }
        if (aliveAnimals.isEmpty()) return 0;
        return (float) totalEnergy / aliveAnimals.size();
    }

    public float calcAvgLifespan() {
        int totalLifespan = 0;
        for (Animal animal : deadAnimals) {
            totalLifespan += animal.getAge();
        }
        if (deadAnimals.isEmpty()) return 0;
        return (float) totalLifespan / deadAnimals.size();
    }



    public float calcAvgChildren() {
        int totalChildren = 0;
        for (Animal animal : aliveAnimals) {
            totalChildren += animal.getChildren().size();
        }
        if (aliveAnimals.isEmpty()) return 0;
        return (float)totalChildren / aliveAnimals.size();
    }


    public List<Animal> getAnimalsWithGivenGenome(Byte[] genome) {
        List<Animal> animalsWithGenome = new ArrayList<>();
        for (Animal animal : aliveAnimals) {
            if (Arrays.equals(genome, animal.getGenome())) {
                animalsWithGenome.add(animal);
            }
        }
        return animalsWithGenome;
    }

    /**
     * Precalculates the simulation statistics so that the UI portion
     * can get them in a swift manner thus avoiding exceptions on the UI thread.
     */
    public void updateStats() {
        plantCount = map.getPlantCount();
        emptyTiles = map.calculateEmptyTiles();
        mostPopularGenome = findMostPopularGenome();
        avgEnergy = calcAvgEnergy();
        avgLifespan = calcAvgLifespan();
        avgChildren = calcAvgChildren();
    }

    private void simUpdated(){
        for (SimObserver observer : observers){
            observer.update(this);
        }
    }


    /**
     * Performs a single step of the simulation.
     */
    public void simulationStep(){
        //1
        List<Animal> deadThisStep = map.removeDead();
        aliveAnimals.removeAll(deadThisStep);
        deadAnimals.addAll(deadThisStep);

        //2
        for (Animal animal : aliveAnimals){
            map.moveAnimal(animal);
        }

        //3
        planter.consume();

        //4
        List<Animal> newThisStep = map.reproduceAnimals(animalBuilder,animalFedThreshold);
        aliveAnimals.addAll(newThisStep);

        //5
        planter.plant(plantGrowth);

        // Update genome frequency
        updateGenomeFrequency(deadThisStep, newThisStep);
        //advance day
        simulationDay += 1;

        updateStats();
        simUpdated();
    }



    public synchronized void shutDown(){
        System.out.println("Shutting down a simulation");
        this.shutDown = true;
    }

    public synchronized void changeStep(float step){
        this.timePerStep = step;
    }

    public synchronized void changePause(){
        this.paused = !this.paused;
    }

    @Override
    public void run() {
        while (true){
            long time1 = System.nanoTime();
            if(!paused) {
                simulationStep();
            }
            long time2 = System.nanoTime();

            if (shutDown){
                return;
            }
            try{
                Thread.sleep(Math.round(Math.max(0,timePerStep* 1000 - (float) (time2 - time1) /1000000)));
            } catch (InterruptedException e){
                System.err.println(e.getMessage());
            }
        }
    }

}

