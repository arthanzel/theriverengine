package com.arthanzel.theriverengine.sim;

import com.arthanzel.theriverengine.data.JsonSerializable;
import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
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
            Random r = new Random();

            // Pick a random arc. Shorts arcs should be selected less than long arcs.
            RiverArc arc;
            double pos;
            do {
                arc = arcs[r.nextInt(arcs.length)];
                pos = r.nextDouble() * maxLength;
            } while (pos > arc.length());
            Agent a = new Agent();
            a.setLocation(new Location(arc, pos));
            agents.add(a);
        }
    }

    public RiverSystem clone() {
        RiverSystem rs = new RiverSystem(network); // The network is immutable, so the clone can contain a reference.
        rs.time = time;

        rs.agents = new LinkedList<>();
        for (Agent a : agents) {
            rs.agents.add(a.clone());
        }

        for (String s : environments.keySet()) {
            rs.environments.put(s, Environment.clone(environments.get(s)));
        }

        return rs;
    }

    @Override
    public JsonObject toJson() {
        JsonObject me = new JsonObject();

        me.addProperty("time", this.time);

        JsonArray agentsArray = new JsonArray();
        for (Agent a : agents) {
            agentsArray.add(a.toJson());
        }
        me.add("agents", agentsArray);

        // TODO: Json serialize environments
//        JsonObject envsArray = new JsonObject();
//        for (String k : environments.keySet()) {
//            envsArray.add(k, environments.get(k).toJson());
//        }
//        me.add("environments", envsArray);

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
