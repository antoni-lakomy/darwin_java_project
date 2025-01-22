package agh.ics.oop.presenters;

import agh.ics.oop.organisms.Animal;
import agh.ics.oop.organisms.Plant;
import agh.ics.oop.records.Vector2d;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


/**
 * A single cell on the displayed {@link GridPane}
 */
public class DisplayCell extends StackPane {

    Rectangle highlight;

    Circle animalCircle;

    Color originalColor;

    /**
     * Creates a new normal cell based on the provided parameters.
     *
     * @param animal Animal to be displayed, can be null.
     * @param plant Plant to be displayed, can be null.
     * @param size The size of the cell, can't be null.
     */
    public DisplayCell(Animal animal, Plant plant,double size){
        super();
        addPlant(plant,size);
        addAnimal(animal,size);
    }

    /**
     * Creates a new clickable cell based on the provided parameters.
     * Click action selects the animal on this cell to be tracked.
     * Does nothing if no animal is present.
     *
     * @param animal Animal to be displayed, can be null.
     * @param plant Plant to be displayed, can be null.
     * @param size The size of the cell, can't be null.
     * @param position The position of this cell on the grid, can't be null.
     * @param presenter The presenter that handles the selection function, can't be null.
     */
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

    /**
     * Adds a highlight to this cell in the desired color.
     * If the cell is already highlighted then changes the highlight color
     * to be a mix of the new and the previous one.
     *
     * @param color The desired color of the highlight.
     * @param size The size of the highlight, in most cases it should
     *             be the same as the cell size.
     */
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

    /**
     * Removes the cell highlight if present
     */
    public void removeCellHighlight(){
        if (highlight == null) return;
        getChildren().remove(highlight);
        highlight = null;
    }

    /**
     * Adds the desired color to the animal on this cell.
     *
     * @param color The color of the highlight
     *
     * @throws NullPointerException If no animal is present on the cell.
     */
    public void addAnimalHighlight(Color color){
        animalCircle.setFill(originalColor.interpolate(color,0.5));
    }

    /**
     * Removes the highlight from the animal on this cell.
     *
     * @throws NullPointerException If no animal is present on the cell.
     */
    public void removeAnimalHighlight(){
        animalCircle.setFill(originalColor);
    }

}
