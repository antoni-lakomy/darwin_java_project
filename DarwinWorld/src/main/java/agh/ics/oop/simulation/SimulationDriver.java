package agh.ics.oop.simulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationDriver {

    ExecutorService threadPool = Executors.newFixedThreadPool(4);

    public SimulationDriver(){}


    public void addToThePool(Simulation simulation){
        threadPool.submit(simulation);
    }

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
