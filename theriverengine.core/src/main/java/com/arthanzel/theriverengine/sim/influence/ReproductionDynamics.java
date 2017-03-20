package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.common.ui.binding.FireableBinding;
import com.arthanzel.theriverengine.common.ui.binding.IntegerSpinnerBinding;
import com.arthanzel.theriverengine.common.util.TimeUtils;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.common.ui.binding.SliderBinding;
import com.arthanzel.theriverengine.common.util.FishMath;
import com.arthanzel.theriverengine.sim.agent.Location;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Influence that applies a random motion to the Agents present in a RiverSystem.
 *
 * @author Martin
 */
public class ReproductionDynamics extends BaseInfluence {
    @IntegerSpinnerBinding(min = 0, max = 10)
    private int minOffspring = 0;

    @IntegerSpinnerBinding(min = 0, max = 10)
    private int maxOffspring = 10;

    @IntegerSpinnerBinding(min = 0, max = 25)
    private int constantOffspring = 0;

    @FireableBinding
    private IntegerProperty addAgent = new SimpleIntegerProperty(0);

    private Random r = new Random();
    private RiverSystem system;
    private int lastYear = 0;

    public ReproductionDynamics(RiverSystem system) {
        this.system = system;
    }

    public void influence(RiverSystem system, double dt) {
        for (int i = 0; i < addAgent.get(); i++) {
            Agent a = new Agent();
            system.place(a);
            system.getAgents().add(a);
        }
        addAgent.set(0);

        // TODO: Refactor to a FireableInfluence that *queues* events onto the processing thread
        if (lastYear != (int) TimeUtils.years(system.getTime())) {

            List<Agent> newAgents = new LinkedList<>();
            for (Agent a : system.getAgents()) {
                int max = FishMath.randomInt(minOffspring, maxOffspring + 1);
                for (int i = minOffspring; i < max; i++) {
                    Agent na = new Agent();
                    na.setLocation(new Location(a.getLocation().getArc(), a.getLocation().getPosition()));
                    newAgents.add(na);
                }
            }
            system.getAgents().addAll(newAgents);

            List<Agent> constantAgents = new LinkedList<>();
            for (int i = 0; i < constantOffspring; i++) {
                constantAgents.add(new Agent());
            }
            system.place(constantAgents);
            system.getAgents().addAll(constantAgents);

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

    public int getConstantOffspring() {
        return constantOffspring;
    }

    public void setConstantOffspring(int constantOffspring) {
        this.constantOffspring = constantOffspring;
    }

    public int getAddAgent() {
        return addAgent.get();
    }

    public IntegerProperty addAgentProperty() {
        return addAgent;
    }

    public void setAddAgent(int addAgent) {
        this.addAgent.set(addAgent);
    }
}
