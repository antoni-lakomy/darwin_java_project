package agh.ics.oop.presenters;

import agh.ics.oop.maps.WorldMap;
import agh.ics.oop.observers.SimObserver;
import agh.ics.oop.organisms.Animal;
import agh.ics.oop.records.Vector2d;
import agh.ics.oop.simulation.Simulation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class SimulationPresenter implements SimObserver {

    private final static double maxGridSize = 600;

    private double gridCellSize;

    private Animal selectedAnimal;

    private DisplayCell selectedCell;

    private boolean paused = false;

    @FXML
    GridPane mapGrid;

    @FXML private Label simulationDay;
    @FXML private Label aliveAnimals;
    @FXML private Label deadAnimals;
    @FXML private Label avgEnergy;
    @FXML private Label avgLifespan;
    @FXML private Label avgChildren;
    @FXML private Label mostPopularGenome;
    @FXML private Label plantCount;
    @FXML private Label emptyTiles;

    @FXML private Label animalGenome;
    @FXML private Label animalActiveGene;
    @FXML private Label animalEnergy;
    @FXML private Label animalPlantsEaten;
    @FXML private Label animalChildren;
    @FXML private Label animalDescendants;
    @FXML private Label animalAge;
    @FXML private Label animalDied;

    @FXML private Button deselectButton;


    Simulation simulation;

    @FXML
    public void closeWindowEvent() {
        simulation.shutDown();
    }

    public void setSimulation(Simulation simulation){
        this.simulation = simulation;

        //region GridPane setup
        WorldMap map = simulation.getMap();
        mapGrid.setPadding(new Insets(0));
        double cellWidth = maxGridSize / (double)(map.getWidth());
        double cellHeight = maxGridSize / (double)(map.getHeight());
        gridCellSize = Math.min(cellWidth,cellHeight);

        mapGrid.setPrefSize(gridCellSize * map.getWidth(),gridCellSize * map.getHeight());
        mapGrid.setMaxSize(gridCellSize * map.getWidth(),gridCellSize * map.getHeight());


        for (int x = 0; x < map.getWidth(); x++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(gridCellSize));
        }

        for (int y = 0; y < map.getHeight(); y++){
            mapGrid.getRowConstraints().add(new RowConstraints(gridCellSize));
        }
        //endregion

    }

    @Override
    public void update(Simulation simulation) {
        Platform.runLater(() -> {
            updateStatistics(simulation);
            drawMap(simulation);
            if (selectedAnimal != null){
                updateSpecificAnimalStatistics(selectedAnimal);
            }
        });
    }

    public synchronized void pauseUnpauseSimulation(){
        simulation.changePause();
        paused = !paused;
        if (paused) drawMapPaused(simulation);
    }

    private synchronized void updateStatistics(Simulation simulation){
        simulationDay.setText("Day: " + simulation.getSimulationDay());
        aliveAnimals.setText("Alive Animals: " + simulation.getAliveAnimalsSize());
        deadAnimals.setText("Dead Animals: " + simulation.getDeadAnimalsSize());
        avgEnergy.setText("Avg Energy: " + simulation.getAvgEnergy());
        avgLifespan.setText("Avg Lifespan: " + simulation.getAvgLifespan());
        avgChildren.setText("Avg Children: " + simulation.getAvgChildren());
        mostPopularGenome.setText("Most Popular Genome:\n" + Arrays.toString(this.simulation.getMostPopularGenome()));
        plantCount.setText("Plant Count: " + simulation.getPlantCount());
        emptyTiles.setText("Empty Tiles: " + simulation.getEmptyTiles());
    }


    private synchronized void updateSpecificAnimalStatistics(Animal animal){
        animalGenome.setText("Genome:\n" + Arrays.toString(animal.getGenome()));
        animalActiveGene.setText("Active Part: " + animal.getCurrentGene());
        animalEnergy.setText("Energy: " + animal.getEnergy());
        animalPlantsEaten.setText("Plants Eaten: " + animal.getPlantsEaten());
        animalChildren.setText("Children: " + animal.getChildren().size());
        animalDescendants.setText("Descendants: " + animal.countDescendants());
        animalAge.setText("Age: " + animal.getAge());
        if (animal.getEnergy() < 1){
            animalDied.setText("Died: " + animal.getFinalDay());
        } else{
            animalDied.setText("Died: N/A");
        }
    }

    public synchronized void deselectAnimal(){
        selectedAnimal = null;
        selectedCell.removeAnimalHighlight();
        selectedCell = null;
        animalGenome.setText("Genome: N/A");
        animalActiveGene.setText("Active Part: N/A");
        animalEnergy.setText("Energy: N/A");
        animalPlantsEaten.setText("Plants Eaten: N/A");
        animalChildren.setText("Children: N/A");
        animalDescendants.setText("Descendants: N/A");
        animalAge.setText("Age: N/A");
        animalDied.setText("Died: N/A");
        deselectButton.setDisable(true);
    }

    public synchronized void selectAnimal(Vector2d poz,DisplayCell cell){
        Animal animal = simulation.getMap().getAnimalAt(poz);
        if (animal != null){
            selectedAnimal = animal;
            updateSpecificAnimalStatistics(animal);
            deselectButton.setDisable(false);
            if (selectedCell != null) selectedCell.removeAnimalHighlight();
            cell.addAnimalHighlight(Color.RED);
            selectedCell = cell;
        }
    }

    public synchronized void drawMap(Simulation simulation){
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        WorldMap map = simulation.getMap();

        //region elements
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Vector2d poz = new Vector2d(x, y);
                if (!map.isFieldEmpty(poz)){
                    DisplayCell cell = new DisplayCell(map.getAnimalAt(poz),map.getPlantAt(poz),gridCellSize);
                    if (selectedAnimal != null && selectedAnimal.getEnergy() > 0 &&
                            poz.equals(selectedAnimal.getPosition())){
                        cell.addAnimalHighlight(Color.RED);
                        selectedCell = cell;
                    }



                    mapGrid.add(cell, x, map.getHeight() - (1 + y));
                    GridPane.setHalignment(cell, HPos.CENTER);
                }

            }
        }

        //endregion
    }

    public synchronized void drawMapPaused(Simulation simulation){
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        WorldMap map = simulation.getMap();
        List<Animal> mPGA = simulation.getAnimalsWithGivenGenome(simulation.getMostPopularGenome());
        Set<Vector2d> highlightedAnimalPositions
                = mPGA.stream().map(Animal::getPosition).collect(Collectors.toCollection(HashSet::new));
        Set<Vector2d> highlightedMapPositions = new HashSet<>(simulation.getPlanter().getPreferredTiles());

        //region elements
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Vector2d poz = new Vector2d(x, y);
                DisplayCell cell = new DisplayCell(map.getAnimalAt(poz), map.getPlantAt(poz),
                        gridCellSize, poz,this);

                if (selectedAnimal != null && selectedAnimal.getEnergy() > 0 &&
                        poz.equals(selectedAnimal.getPosition())){
                    cell.addAnimalHighlight(Color.RED);
                    selectedCell = cell;
                }

                if (highlightedAnimalPositions.contains(poz)){
                    cell.addCellHighlight(Color.RED,gridCellSize);
                }
                if (highlightedMapPositions.contains(poz)){
                    cell.addCellHighlight(Color.YELLOW,gridCellSize);
                }


                mapGrid.add(cell, x, map.getHeight() - (1 + y));
                GridPane.setHalignment(cell, HPos.CENTER);
                }


        }
        //endregion
    }

}
