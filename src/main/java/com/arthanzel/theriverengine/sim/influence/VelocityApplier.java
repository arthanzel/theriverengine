package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.sim.agent.Location;
import com.arthanzel.theriverengine.ui.DoubleBinding;
import com.arthanzel.theriverengine.util.FishMath;

/**
 * Influence that applies a random motion to the Agents present in a RiverSystem.
 *
 * @author Martin
 */
public class VelocityApplier extends BaseInfluence {
    @DoubleBinding(min = 0, max = 10) private volatile double maxVelocity = 2.0;

    public void influence(RiverSystem system, double dt) {
        for (Agent a : system.getAgents()) {
            double v = FishMath.clamp(a.getAttributes().getDouble("velocity"), -maxVelocity, maxVelocity);
            displace(a, v * dt);

            a.getAttributes().put("velocity", 0.0);
        }
    }

    /**
     * Displaces the Agent's location upstream (negative delta) or downstream (positive delta). If the new position is outside
     * of the current river Arc, a random connecting Arc is chosen.
     */
    public void displace(Agent agent, double delta) {
        Location location = agent.getLocation();
        double max = location.getArc().length();
        double pos = location.getPosition();
        double newPos = pos + delta;

        if (newPos >= 0 && newPos < max) {
            // The agent stays within its current Arc
            location.setPosition(newPos);
        }
        else if (newPos < 0) {
            // The agent moves to an upstream Arc

        }
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }
}
