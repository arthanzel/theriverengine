package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.common.ui.binding.DoubleSpinnerBinding;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.common.ui.binding.BindingName;
import com.arthanzel.theriverengine.common.ui.binding.SliderBinding;

import java.util.Random;

/**
 * Influence that applies a random motion to the Agents present in a RiverSystem.
 *
 * @author Martin
 */
public class FlowMovement extends BaseInfluence {
    @DoubleSpinnerBinding(min = -5, max = 5)
    @BindingName("Flow speed (m/s)")
    private volatile double flow = 0.0;
    Random r = new Random();

    public void influence(RiverSystem system, double dt) {
        for (Agent a : system.getAgents()) {
            double v = a.getAttributes().getDouble("velocity");
            a.getAttributes().put("velocity", v + flow * dt);
        }
    }

    public double getFlow() {
        return flow;
    }

    public void setFlow(double flow) {
        this.flow = flow;
    }
}
