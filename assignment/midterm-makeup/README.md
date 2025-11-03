We are building a multiplayer game. Each Player selects one of the tribes, FireTribe, AirTribe, or IceTribe, at the start. The GameEngine configures the Player when the game starts, and the Player must be able to create Villagers and Warriors that belong to its chosen tribe.

Villagers wander around ( `wander()`) and do their main action.

- A FireVillager's main action is harvestFire().
- An AirVillager's main action is catchFood().
- An IceVillager's main action is buildIglo().

Warriors `train()` and `fight()` with others.

- A FireWarrior's main fighting style is throwFire.
- An AirWarrior's main fighting style is makeStorm.
- An IceWarrior's main fighting style is freezeTarget.

You are assigned to design the part of the system responsible for creating Villagers and Warriors for each Player. Your design should ensure that all **created sprites belong to the Player's tribe.** The Player's code must not use if/else logic to decide which concrete classes to instantiate each time a sprite is created.

Other teams handle behaviors and multiplayer logic; so do not worry about multi-player logic, behaviors, and fighting styles.

1. Give pattern name to meet these requirements and reason why (3 points).
2. Give a UML diagram for your solution (6 points).
3. Give pattern related code (6 points).
4. Give client code (5 points).

The code must include the full class signatures of the classes playing a role in the pattern and any method implementation that plays a crucial role in the pattern realization.
