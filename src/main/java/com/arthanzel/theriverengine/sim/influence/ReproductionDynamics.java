package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.ui.BindingName;
import com.arthanzel.theriverengine.ui.DoubleBinding;
import com.arthanzel.theriverengine.util.FishMath;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Influence that applies a random motion to the Agents present in a RiverSystem.
 *
 * @author Martin
 */
public class ReproductionDynamics extends BaseInfluence {
    @DoubleBinding(min = 0, max = 10)
    private int minOffspring = 0;

    @DoubleBinding(min = 0, max = 10)
    private int maxOffspring = 10;

    private Random r = new Random();
    private RiverSystem system;
    private boolean reproduce = false;

    public ReproductionDynamics(RiverSystem system) {
        this.system = system;
    }

    public void influence(RiverSystem system, double dt) {
        // TODO: Refactor to a FireableInfluence that *queues* events onto the processing thread
        if (reproduce) {
            reproduce = false;

            List<Agent> newAgents = new LinkedList<>();
            for (Agent a : system.getAgents()) {
                int max = FishMath.randomInt(minOffspring, maxOffspring + 1);
                for (int i = minOffspring; i <= max; i++) {
                    newAgents.add(new Agent());
                }
            }
            system.getAgents().addAll(newAgents);
        }
    }

    public void fireReproduce() {
        System.out.println("Reproduced");
        reproduce = true;
    }

    public int getMinOffspring() {
        return minOffspring;
    }

    public void setMinOffspring(int minOffspring) {
        this.minOffspring = minOffspring;
    }

    public int getMaxOffspring() {
        return maxOffspring;
    }

    public void setMaxOffspring(int maxOffspring) {
        this.maxOffspring = maxOffspring;
    }
}
