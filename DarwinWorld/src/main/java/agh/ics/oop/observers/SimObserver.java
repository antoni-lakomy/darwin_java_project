package agh.ics.oop.observers;

import agh.ics.oop.simulation.Simulation;

public interface SimObserver {

    /**
     * Invoked when a simulation this object observes
     * has finished its step and is ready to be displayed.
     *
     * @param simulation The {@link Simulation to display}
     */
    void update(Simulation simulation);
}
