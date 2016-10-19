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
public class RandomMovement extends BaseInfluence {
    @DoubleBinding(min = 0, max = 10) private volatile double spread = 2.0;
    Random r = new Random();

    public void influence(RiverSystem system, double dt) {
        for (Agent a : system.getAgents()) {
            double v = a.getAttributes().getDouble("velocity");
            a.getAttributes().put("velocity", v + r.nextDouble() * 2 * spread - spread);
        }
    }

    public double getSpread() {
        return spread;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }
}
