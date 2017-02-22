package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.common.ui.binding.SliderBinding;
import com.arthanzel.theriverengine.common.util.FishMath;

import java.util.Random;

/**
 * Influence that applies a random motion to the Agents present in a RiverSystem.
 *
 * @author Martin
 */
public class RandomMovement extends BaseInfluence {
    @SliderBinding(min = 0, max = 10)
    private volatile double spread = 2.0;

    Random r = new Random();

    public void influence(RiverSystem system, double dt) {
        for (final Agent a : system.getAgents()) {
            run(a);
        }
    }

    private void run(Agent a) {
        final double v = a.getAttributes().getDouble("velocity");
        a.getAttributes().put("velocity", v + FishMath.gaussian(0, spread, spread * 2));
    }

    public double getSpread() {
        return spread;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }
}
