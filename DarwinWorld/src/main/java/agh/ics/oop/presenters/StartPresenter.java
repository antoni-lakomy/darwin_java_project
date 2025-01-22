package agh.ics.oop.presenters;

import agh.ics.oop.observers.FileDisplay;
import agh.ics.oop.records.SimParams;
import agh.ics.oop.simulation.Simulation;
import agh.ics.oop.simulation.SimulationBuilder;
import agh.ics.oop.simulation.SimulationDriver;
import agh.ics.oop.util.SimulationApp;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.*;


public class StartPresenter {

    private SimulationApp app;

    @FXML
    private TextField seedField;


    @FXML
    private TextField mapHeightField;

    @FXML
    private TextField mapWidthField;

    @FXML
    private ChoiceBox<String>  mapTypeField;


    @FXML
    private TextField plantStartNumberField;

    @FXML
    private TextField plantEnergyField;

    @FXML
    private TextField plantGrowthField;


    @FXML
    private TextField animalStartNumberField;

    @FXML
    private TextField animalStartEnergyField;

    @FXML
    private TextField animalFedThresholdField;

    @FXML
    private TextField animalReproductionCostField;

    @FXML
    private TextField animalGenomeLengthField;

    @FXML
    private ChoiceBox<String>  animalBehaviourTypeField;

    @FXML
    private TextField animalMinMutationField;

    @FXML
    private TextField animalMaxMutationField;

    @FXML
    private TextField saveFileName;

    @FXML
    private CheckBox saveToCSV;

    long seed;

    int mapHeight;
    int mapWidth;
    int mapType;

    int plantStartNumber;
    int plantEnergy;
    int plantGrowth;

    int animalStartNumber;
    int animalStartEnergy;
    int animalFedThreshold;
    int animalReproductionCost;
    int animalGenomeLength;
    int animalBehaviourType;
    int animalMinMutation;
    int animalMaxMutation;

    private Popup popup;

    private Label popupLabel;

    private Stage stage;

    SimulationDriver driver = new SimulationDriver();

    Gson gson = new Gson();

    public void setUp(SimulationApp app, Stage stage) {
        this.app = app;
        this.stage = stage;

        popup = new Popup();
        popup.setAutoHide(true);
        popupLabel = new Label();
        popup.getContent().add(popupLabel);
    }

    public void displayPopup(String message, Paint color){
        popupLabel.setTextFill(color);
        popupLabel.setText(message);
        popup.show(stage);
    }

    private Long verifyLong(TextField textField, String name){
        if (textField.getText().isEmpty()) throw new IllegalArgumentException(name + " is empty.");
        try{
            return Long.parseLong(textField.getText());
        } catch (Exception e){
            throw new IllegalArgumentException(name + " is not numeric");
        }
    }

    private int verifyInt(TextField textField, String name, int minValue){
        int result;
        if (textField.getText().isEmpty()) throw new IllegalArgumentException(name + " is empty.");
        try{
            result = Integer.parseInt(textField.getText());
        } catch (Exception e){
            throw new IllegalArgumentException(name + " is not numeric");
        }
        if (result < minValue) throw new IllegalArgumentException(name + " is less then the min value of " + minValue);
        return result;
    }



    private void verifyData(){
        seed = verifyLong(seedField,"Seed");

        mapHeight = verifyInt(mapHeightField,"Map Height",1);
        mapWidth = verifyInt(mapWidthField,"Map Width",1);

        plantStartNumber = verifyInt(plantStartNumberField,"Plant Start Number",0);
        plantEnergy = verifyInt(plantEnergyField,"Plant Energy",0);
        plantGrowth = verifyInt(plantGrowthField,"Plant Growth",0);

        animalStartNumber = verifyInt(animalStartNumberField,"Animal Start Number",0);
        animalStartEnergy = verifyInt(animalStartEnergyField,"Animal Start Energy",0);
        animalFedThreshold = verifyInt(animalFedThresholdField,"Animal Fed Threshold",0);
        animalReproductionCost = verifyInt(animalReproductionCostField,"Animal Reproduction Cost",0);
        animalGenomeLength = verifyInt(animalGenomeLengthField,"Animal Genome Length",1);
        animalMinMutation = verifyInt(animalMinMutationField,"Animal Min Mutation",0);
        animalMaxMutation = verifyInt(animalMaxMutationField,"Animal Max Mutation",0);

        if (animalFedThreshold < animalReproductionCost)
            throw new IllegalArgumentException("Fed Threshold can't be lower than Reproduction Cost");

        if (animalMinMutation > animalMaxMutation)
            throw new IllegalArgumentException("Min Mutation can't be higher then Max Mutation");

        if (animalMaxMutation > animalGenomeLength)
            throw new IllegalArgumentException("Max Mutation can't be higher then Genome Length");

        mapType = mapTypeField.getItems().indexOf(mapTypeField.getValue());
        animalBehaviourType = animalBehaviourTypeField.getItems().indexOf(animalBehaviourTypeField.getValue());
    }

    private void writeData(SimParams params){
        seedField.setText(Long.toString(params.seed()));

        mapHeightField.setText(Integer.toString(params.mapHeight()));
        mapWidthField.setText(Integer.toString(params.mapWidth()));
        mapTypeField.setValue(mapTypeField.getItems().get(params.mapType()));

        plantStartNumberField.setText(Integer.toString(params.plantStartNumber()));
        plantEnergyField.setText(Integer.toString(params.plantEnergy()));
        plantGrowthField.setText(Integer.toString(params.plantGrowth()));

        animalStartNumberField.setText(Integer.toString(params.animalStartNumber()));
        animalStartEnergyField.setText(Integer.toString(params.animalStartEnergy()));
        animalFedThresholdField.setText(Integer.toString(params.animalFedThreshold()));
        animalReproductionCostField.setText(Integer.toString(params.animalReproductionCost()));
        animalGenomeLengthField.setText(Integer.toString(params.animalGenomeLength()));
        animalMinMutationField.setText(Integer.toString(params.animalMinMutation()));
        animalMaxMutationField.setText(Integer.toString(params.animalMaxMutation()));
        animalBehaviourTypeField.setValue(animalBehaviourTypeField.getItems().get(params.animalBehaviourType()));
    }


    private SimParams createSimParams(){
        return new SimParams(
                seed,
                mapHeight,
                mapWidth,
                mapType,
                plantStartNumber,
                plantEnergy,
                plantGrowth,
                0,
                animalStartNumber,
                animalStartEnergy,
                animalFedThreshold,
                animalReproductionCost,
                animalGenomeLength,
                animalBehaviourType,
                animalMinMutation,
                animalMaxMutation,
                0
                );
    }

    public void onSimulationStartClicked(){
        try {
            verifyData();
            SimParams parameters = createSimParams();
            Simulation simulation = SimulationBuilder.build(parameters);

            if (saveToCSV.isSelected()){
                FileDisplay csvSaving = new FileDisplay();
                simulation.addObserver(csvSaving);
            }

            app.startSimulation(simulation);
            driver.addToThePool(simulation);

        } catch (Exception e){
            displayPopup(e.getMessage(),Color.RED);
        }
    }

    public void loadConfiguration(){
        try {
            if (saveFileName.getText().isEmpty())
                throw  new IllegalArgumentException("Configuration name can't be blank");

            File confFile = new File(saveFileName.getText() + ".json");
            if (!confFile.isFile())
                throw  new IllegalArgumentException("Configuration by that name doesn't exist");

            SimParams params = null;
            try(FileReader reader = new FileReader(confFile)){
               if (reader.ready()){
                   params = gson.fromJson(reader,SimParams.class);
               }
            }
            if (params == null) throw new IOException("The file couldn't be read");
            writeData(params);
            displayPopup("Configuration loaded from " + saveFileName.getText() + ".json",Color.GRAY);
        } catch (Exception e){
            displayPopup(e.getMessage(), Color.RED);
        }
    }

    public void saveConfiguration(){
        try {
            if (saveFileName.getText().isEmpty())
                throw  new IllegalArgumentException("Configuration name can't be blank");
            verifyData();
            SimParams params = createSimParams();
            String paramsJSON = gson.toJson(params);

            File confFile = new File(saveFileName.getText()+".json");
            try (FileWriter writer = new FileWriter(confFile)){
                writer.write(paramsJSON);
            }
            displayPopup("Configuration saved to " + saveFileName.getText() + ".json",Color.GRAY);
        } catch (Exception e){
            displayPopup(e.getMessage(), Color.RED);
        }

    }


    public void exit(){
        driver.shutdownThePool();
    }


}
