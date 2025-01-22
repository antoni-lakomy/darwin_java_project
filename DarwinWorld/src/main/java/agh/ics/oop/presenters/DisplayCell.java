package agh.ics.oop.presenters;

import agh.ics.oop.organisms.Animal;
import agh.ics.oop.organisms.Plant;
import agh.ics.oop.records.Vector2d;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class DisplayCell extends StackPane {

    Rectangle highlight;

    Circle animalCircle;

    Color originalColor;

    public DisplayCell(Animal animal, Plant plant,double size){
        super();
        addPlant(plant,size);
        addAnimal(animal,size);
    }

    public DisplayCell(Animal animal, Plant plant,double size,Vector2d position, SimulationPresenter presenter){
        this(animal,plant,size);
        setOnMouseClicked((event) -> presenter.selectAnimal(position,this));
    }

    private void addPlant(Plant plant, double size){
        if (plant != null){
            Rectangle rectangle = new Rectangle(size*0.9,size*0.9);
            rectangle.setFill(Color.valueOf("#125D18"));
            getChildren().add(rectangle);
            StackPane.setAlignment(rectangle, Pos.CENTER);
        }
    }

    private void addAnimal(Animal animal,double size){
        if (animal != null){
            double animalSize = Math.min(size/2,animal.getAge()/20f*size);
            Circle circle = new Circle(Math.max(animalSize,size/10));
            originalColor = Color.BLACK.interpolate(Color.BLUE,Math.min(1,animal.getEnergy()/50f));
            circle.setFill(originalColor);
            getChildren().add(circle);
            StackPane.setAlignment(circle, Pos.CENTER);
            animalCircle = circle;
        }
    }

    public void addCellHighlight(Color color,double size){
        Color newColor = color.deriveColor(1,1,1,0.7);
        if (highlight == null){
            Rectangle rectangle = new Rectangle(size,size);
            rectangle.setFill(newColor);
            int index = getChildren().size() > 1 ? 1 : 0;
            getChildren().add(index,rectangle);
            StackPane.setAlignment(rectangle, Pos.CENTER);
            highlight = rectangle;
        } else {
            highlight.setFill(newColor.interpolate((Color) highlight.getFill(),0.5));
        }
    }
//    public void removeCellHighlight(){
//        if (highlight == null) return;
//        getChildren().remove(highlight);
//        highlight = null;
//    }

    public void addAnimalHighlight(Color color){
        animalCircle.setFill(originalColor.interpolate(color,0.5));
    }

    public void removeAnimalHighlight(){
        animalCircle.setFill(originalColor);
    }

}
