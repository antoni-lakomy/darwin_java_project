package agh.ics.oop.simulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Contains the logic to run multiple {@link Simulation} instances simultaneously.
 */
public class SimulationDriver {

    ExecutorService threadPool = Executors.newFixedThreadPool(4);

    public SimulationDriver(){}


    /**
     * Adds a simulation to its internal pool to be run.
     *
     * @param simulation A simulation to be added.
     */
    public void addToThePool(Simulation simulation){
        threadPool.submit(simulation);
    }

    /**
     * Shuts down the internal pool and waits for it's termination.
     */
    public void shutdownThePool(){
        System.out.println("Shutting down the pool");
        try {
            threadPool.shutdown();
            threadPool.awaitTermination(1, TimeUnit.SECONDS);
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
        System.out.println("Pool shut down");
    }


}
