package agh.ics.oop.simulation;

import agh.ics.oop.maps.Globe;
import agh.ics.oop.maps.Poles;
import agh.ics.oop.maps.WorldMap;
import agh.ics.oop.model.FullPredestination;
import agh.ics.oop.model.GeneInterpreter;
import agh.ics.oop.model.OldnessSadness;
import agh.ics.oop.organisms.Animal;
import agh.ics.oop.organisms.AnimalBuilder;
import agh.ics.oop.planters.ForestedEquators;
import agh.ics.oop.planters.Planter;
import agh.ics.oop.records.SimParams;
import agh.ics.oop.records.Vector2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public final class SimulationBuilder {


    /**
     * Builds a new {@link  Simulation} based on provided parameters.
     *
     *
     * @param params The {@link SimParams} object containing the parameters
     *               of the desired simulation.
     * @return A new {@link Simulation} object.
     */
    public static Simulation build(SimParams params){
        Simulation sim = new Simulation();

        GeneInterpreter interpreter = createGeneInterpreter(params);
        WorldMap map = createMap(params,interpreter);
        sim.map = map;

        AnimalBuilder builder = createAnimalBuilder(params);
        sim.animalBuilder = builder;

        sim.planter = createPlanter(params,map);

        sim.aliveAnimals = createAnimals(params,map,builder);
        sim.updateGenomeFrequency(Collections.emptyList(), sim.aliveAnimals);

        sim.animalFedThreshold = params.animalFedThreshold();

        sim.plantGrowth = params.plantGrowth();

        sim.deadAnimals = new ArrayList<>();

        sim.simulationDay = 0;

        return sim;
    }

    private static GeneInterpreter createGeneInterpreter(SimParams params){
        if (params.animalBehaviourType() == 0) return new FullPredestination();
        if (params.animalBehaviourType() == 1) return new OldnessSadness();
        throw new IllegalArgumentException("The specified animal behaviour type is incorrect");
    }

    private static WorldMap createMap(SimParams params, GeneInterpreter interpreter){
        WorldMap map;
        if (params.mapType() == 0) return new Globe(params.mapWidth(),params.mapHeight(),interpreter);
        if (params.mapType() == 1) return new Poles(params.mapWidth(),params.mapHeight(),interpreter);
        throw new IllegalArgumentException("The specified map type is incorrect");
    }

    private static AnimalBuilder createAnimalBuilder(SimParams params){
        return new AnimalBuilder(params);
    }

    private static Planter createPlanter(SimParams params,WorldMap map){
        Planter planter;
        if (params.plantType() == 0)
            planter = new ForestedEquators(map,params.plantEnergy(),params.seed());
        else {
            throw new IllegalArgumentException("The specified plant type is incorrect");
        }
        planter.plant(params.plantStartNumber());
        return planter;
    }

    private static ArrayList<Animal> createAnimals(SimParams params, WorldMap map, AnimalBuilder builder){
        Random rng = new Random(params.seed());
        ArrayList<Animal> animals = new ArrayList<>();

        for (int i = 0; i < params.animalStartNumber(); i++){
            Vector2d position = new Vector2d(rng.nextInt(params.mapWidth()),rng.nextInt(params.mapHeight()));
            Animal animal = builder.buildFresh(position);
            map.addAnimal(animal);
            animals.addLast(animal);

        }
        return animals;
    }

}
