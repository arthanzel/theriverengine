package com.arthanzel.theriverengine.sim;

import com.arthanzel.theriverengine.common.data.JsonSerializable;
import com.arthanzel.theriverengine.common.rivergen.RiverArc;
import com.arthanzel.theriverengine.common.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.common.util.FishMath;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.sim.agent.Location;
import com.arthanzel.theriverengine.sim.environment.DiscreteEnvironment;
import com.arthanzel.theriverengine.sim.environment.Environment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * RiverSystem contains the entire model of a river system, including its population of agents, its topology, and its
 * environmental characteristics.
 */
public class RiverSystem implements JsonSerializable {
    private List<Agent> agents;
    private Map<String, Environment> environments = new HashMap<>();
    private final RiverNetwork network;
    private double time = 0; // Seconds

    private RiverSystem(RiverNetwork network) {
        this.network = network;
    }

    public RiverSystem(RiverNetwork network, int numAgents) {
        this(network);
        this.agents = new LinkedList<>();

        initAgentsRandomly(numAgents);
    }

    /**
     * Initializes this river system's array of agents with random locations and properties.
     */
    public void initAgentsRandomly(int numAgents) {
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
        for (int i = 0; i < numAgents; i++) {
            // Pick a random arc. Shorts arcs should be selected less than long arcs.
            RiverArc arc;
            double pos;
            do {
                arc = arcs[FishMath.randomInt(0, arcs.length)];
                pos = FishMath.random(0, maxLength);
            } while (pos > arc.length());
            Agent a = new Agent();
            a.setLocation(new Location(arc, pos));
            agents.add(a);
        }
    }

    @Override
    public JsonObject toJson() {
        JsonObject me = new JsonObject();

        me.addProperty("time", this.time);
        me.addProperty("numAgents", agents.size());

        JsonArray agentsArray = new JsonArray();
        for (Agent a : agents) {
            agentsArray.add(a.toJson());
        }
        me.add("agents", agentsArray);

        JsonObject envs = new JsonObject();
        for (String k : environments.keySet()) {
            envs.add(k, environments.get(k).toJson());
        }
        me.add("environments", envs);

        return me;
    }

    // ====== Accessors ======

    public List<Agent> getAgents() {
        return agents;
    }

    public RiverNetwork getNetwork() {
        return network;
    }

    public Map<String, Environment> getEnvironments() {
        return environments;
    }

    public void putEnvironment(String envName, DiscreteEnvironment env) { environments.put(envName, env); }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
