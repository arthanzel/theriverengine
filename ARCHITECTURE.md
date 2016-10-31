RiverSystem: Represents the model of a river system. Contains a RiverNetwork, the array of fish, and environmental constants.

RiverNetwork: Extension of a directed graph that holds the physical configuration of a river network. Contains methods for serializing into a GraphFile.

RiverRunner: Class responsible for taking a RiverSystem and simulating it. Parallellism will be done on this level.

RiverViewer: Swing or JFX frame that renders a RiverSystem.

Movement:

- Random - position or velocity jumps?
    - Bias for river flow? Water speed?
- Density-dependent
- Nutrient-dependent
    - Resources appearing/disappearing independent of state (constant alpha) vs dependent on state (game of life)
    - Nutrient overlays: apply to a new graph structure to get rid of superfluous pixels, and to enable flow of nutrients?
- -> Weighted result?

Todo:

    - Random movement
    - Generation and representation of overlays
    - Dynamics of overlays

Drift paradox
    
Competition based on a robust nutrient model. Fitness is proportional to the availability of nutrients to each fish in a given space.

2016-10-27
==========
- Make an Env so that params in simple, log growth are dependent on space
- Forks: cut the effect on each fork, or cut the sphere of influence in each fork
- Next steps:
    - Spatial component of nutrient dynamics
    - Environments for temperature, coverage, etc.
    - 