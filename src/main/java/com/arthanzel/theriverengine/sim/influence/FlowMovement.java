package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.ui.DoubleBinding;

import java.util.Random;

/**
 * Influence that applies a random motion to the Agents present in a RiverSystem.
 *
 * @author Martin
 */
public class FlowMovement extends BaseInfluence {
    @DoubleBinding(min = -0.1, max = 0.1) private volatile double flow = 0.01;
    Random r = new Random();

    public void influence(RiverSystem system, double dt) {
        for (Agent a : system.getAgents()) {
            double v = a.getAttributes().getDouble("velocity");
            a.getAttributes().put("velocity", v + flow);
        }
    }

    public double getFlow() {
        return flow;
    }

    public void setFlow(double flow) {
        this.flow = flow;
    }
}
