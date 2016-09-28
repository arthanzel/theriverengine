RiverSystem: Represents the model of a river system. Contains a RiverNetwork, the array of fish, and environmental constants.
RiverNetwork: Extension of a directed graph that holds the physical configuration of a river network. Contains methods for serializing into a GraphFile.
RiverRunner: Class responsible for taking a RiverSystem and simulating it. Parallellism will be done on this level.
RiverViewer: Swing or JFX frame that renders a RiverSystem.
