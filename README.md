# Darwin Java Project
Collaborative project for Object Oriented Programing Course at AGH University of Cracow

## Authors:
Antoni Łakomy, Bartłomiej Kaczyński

## Project Description
Darwin World is a simulation-based project where an evolving world of animals and plants unfolds over time. This project creates a virtual ecosystem where herbivorous animals roam, seek food, and reproduce. The simulation runs on a grid-based map consisting of steppes and jungles, with plants growing and animals adapting over time based on their genetic code. Key components include:

- Herbivorous animals that consume plants, move, and reproduce
- Genetic algorithms determining animal behavior and evolution
- Configurable simulation parameters (e.g., energy levels, reproduction rules)
- An interactive visualization of the evolving ecosystem
- For a more detailed description, refer to the [instructions](https://github.com/Soamid/obiektowe-lab/blob/proj-2024/proj/Readme.md).

## How to run the simulation?

- Clone repo into your PC
- Using your IDE (preferrably InteliJ) run the file `src/main/java/agh/ics/oop/Main.java`
- in the pop-up widow customize your simulation settings or load a pre-defined setup (a the botton, inside the `Configuration File` box type _default12_ or _default50_ and click `load`.
  You can also save your custom configuration by clicking `save` button)
- You can export simulation stats into `.csv` file if you want to. Just check the `Save to csv` box. After you finish simulation, you will see new `.csv` file in your project folder.
- Whenever you are ready to run the simulation, click the `Start` button and enjoy!

## Simulation explained
- Each animal is a blue circle - darker it gets, less energy it has
- Animals increase their radius when they get older, until they reach adulthood
- When a tile changes its background colour from light green to dark it means that there is a plant on it now
- You can see the current simulation stats on the left side on the window
- If you are beeing curious, or you are just a nerd, you can pause a simulation by smashing `change pause` button and select particular animal which you want to follow and see its statistics
- Then you can hit `change pause` again and simulation will resume, your selected animal stats will still be followed. You can deselect it anytime by clicking `deselect` button
- Another feature - while the simulation is paused, you can see the yellow highlight in the middle part of map - that means those tiles are being preffered by planter, so there are higher chances that next turn plant will grow on one of those tiles in comparison to non-highlighted ones
- Also some tiles during the pause change their backgroud to red, meaing that an animal with the most popular genome stands on it 
