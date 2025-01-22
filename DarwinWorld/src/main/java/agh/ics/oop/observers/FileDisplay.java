package agh.ics.oop.observers;

import agh.ics.oop.simulation.Simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


/**
 * Writes 
 */
public class FileDisplay implements SimObserver {

    private String fileName;

    public FileDisplay() throws IOException {
        this.fileName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        this.fileName = this.fileName + ".csv";
        File file = new File(fileName);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Day,Alive Animals,Dead Animals," +
                    "Average Energy,Average Lifespan,Average Children," +
                    "Most Popular Genome,Plant Count,Empty Tiles");
        }

    }


    @Override
    public synchronized void update(Simulation simulation) {
        File file = new File(fileName);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file,true))) {
            writer.println(simulation.getSimulationDay() + "," +
                    simulation.getAliveAnimalsSize() + "," +
                    simulation.getDeadAnimalsSize() + "," +
                    simulation.getAvgEnergy() + "," +
                    simulation.getAvgLifespan() + "," +
                    simulation.getAvgChildren() + ",\"" +
                    Arrays.toString(simulation.getMostPopularGenome()) + "\"," +
                    simulation.getPlantCount() + "," +
                    simulation.getEmptyTiles());
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}
