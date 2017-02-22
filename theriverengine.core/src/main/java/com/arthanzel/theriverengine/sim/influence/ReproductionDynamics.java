package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.common.util.TimeUtils;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.common.ui.binding.SliderBinding;
import com.arthanzel.theriverengine.common.util.FishMath;
import com.arthanzel.theriverengine.sim.agent.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Influence that applies a random motion to the Agents present in a RiverSystem.
 *
 * @author Martin
 */
public class ReproductionDynamics extends BaseInfluence {
    @SliderBinding(min = 0, max = 10)
    private int minOffspring = 0;

    @SliderBinding(min = 0, max = 10)
    private int maxOffspring = 10;

    private Random r = new Random();
    private RiverSystem system;
    private int lastYear = 0;

    public ReproductionDynamics(RiverSystem system) {
        this.system = system;
    }

    public void influence(RiverSystem system, double dt) {
        // TODO: Refactor to a FireableInfluence that *queues* events onto the processing thread
        if (lastYear != (int) TimeUtils.years(system.getTime())) {
            List<Agent> newAgents = new LinkedList<>();
            for (Agent a : system.getAgents()) {
                int max = FishMath.randomInt(minOffspring, maxOffspring + 1);
                for (int i = minOffspring; i <= max; i++) {
                    Agent na = new Agent();
                    na.setLocation(new Location(a.getLocation().getArc(), a.getLocation().getPosition()));
                    newAgents.add(na);
                }
            }
            system.getAgents().addAll(newAgents);
            lastYear = (int) TimeUtils.years(system.getTime());
        }
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
