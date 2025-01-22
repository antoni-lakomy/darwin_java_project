package agh.ics.oop.util;

import agh.ics.oop.presenters.SimulationPresenter;
import agh.ics.oop.presenters.StartPresenter;
import agh.ics.oop.simulation.Simulation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

public class SimulationApp extends Application {


    private StartPresenter mainPresenter;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("start.fxml"));
        BorderPane viewRoot = loader.load();

        this.mainPresenter = loader.getController();
        mainPresenter.setUp(this,stage);

        configureStage(stage,viewRoot,"Simulation Starter");

        stage.show();
    }

    @Override
    public void stop(){
        mainPresenter.exit();
    }

    private void configureStage(Stage stage, BorderPane viewRoot, String name) {
        var scene = new Scene(viewRoot);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("style.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle(name);
        stage.minWidthProperty().bind(viewRoot.minWidthProperty());
        stage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    public void startSimulation(Simulation simulation) throws Exception {
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();

        SimulationPresenter presenter = loader.getController();
        presenter.setSimulation(simulation);
        simulation.addObserver(presenter);
        stage.setOnCloseRequest(event -> presenter.closeWindowEvent());

        configureStage(stage,viewRoot,"Simulation");
        stage.show();
    }
}
