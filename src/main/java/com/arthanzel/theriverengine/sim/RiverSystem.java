package com.arthanzel.theriverengine.sim;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.sim.agent.Location;

import java.util.*;

/**
 * RiverSystem contains the entire model of a river system, including its population of agents, its topology, and its
 * environmental characteristics.
 */
public class RiverSystem {
    private Agent[] agents;
    private Map<String, Environment> environments = new HashMap<>();
    private final RiverNetwork network;
    private long time = 0;

    private RiverSystem(RiverNetwork network) {
        this.network = network;

        environments.put("nutrients", new Environment(network));
    }

    public RiverSystem(RiverNetwork network, int numAgents) {
        this(network);
        this.agents = new Agent[numAgents];

        initAgentsRandomly();
    }

    /**
     * Initializes this river system's array of agents with random locations and properties.
     */
    public void initAgentsRandomly() {
        // Get the arcs into an array and find the longest length
        RiverArc[] arcs = network.edgeSet().toArray(new RiverArc[0]);
        double maxLength = 0;
        for (RiverArc a : arcs) {
            double len = a.length();
            if (len > maxLength) {
                maxLength = len;
            }
        }

        // Re-initialize agents and place them randomly in the river system
        for (int i = 0; i < agents.length; i++) {
            Random r = new Random();

            // Pick a random arc. Shorts arcs should be selected less than long arcs.
            RiverArc arc = null;
            double pos = -1;
            do {
                arc = arcs[r.nextInt(arcs.length)];
                pos = r.nextDouble() * maxLength;
            } while (pos > arc.length());

            agents[i] = new Agent();
            agents[i].setLocation(new Location(arc, pos));
        }
    }

    // ====== Accessors ======

    public Agent[] getAgents() {
        return agents;
    }

    public RiverNetwork getNetwork() {
        return network;
    }

    public Environment getEnvironment(String env) { return environments.get(env); }

    public void putEnvironment(String envName, Environment env) { environments.put(envName, env); }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
