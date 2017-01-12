package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.common.ui.binding.BindingName;
import com.arthanzel.theriverengine.common.ui.binding.SliderBinding;

import java.util.Iterator;

/**
 * Influence that applies a random motion to the Agents present in a RiverSystem.
 *
 * @author Martin
 */
public class DeathDynamics extends BaseInfluence {
    @SliderBinding(min = 0, max = 10)
    @BindingName("Survival Threshold (k)")
    private volatile double threshold = 0.0;

    public void influence(RiverSystem system, double dt) {
        Iterator<Agent> i = system.getAgents().iterator();
        while (i.hasNext()) {
            final Agent a = i.next();
            final double e = a.getAttributes().getDouble("energy");
            final double t = threshold * 1000;

            // If a fish doesn't have enough energy, roll a dice to determine death.
            if (e < t && Math.random() > e / t) {
                i.remove();
            }
        }
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
