package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.common.rivergen.RiverArc;
import com.arthanzel.theriverengine.common.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.common.util.FishMath;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.sim.agent.Location;
import com.arthanzel.theriverengine.sim.environment.DiscreteEnvironment;
import com.arthanzel.theriverengine.sim.environment.DiscretePoint;
import com.arthanzel.theriverengine.common.ui.binding.BindingName;
import com.arthanzel.theriverengine.common.ui.binding.SliderBinding;
import com.arthanzel.theriverengine.common.util.TimeUtils;

/**
 * @author Martin
 */
public class FeedingInfluence extends BaseInfluence {
    private static final double MAX_DISTANCE = 20;

    private RiverNetwork network;
    private DiscreteEnvironment env;
    private double dt;

    @SliderBinding(min = 0, max = 2)
    @BindingName("Feed Rate (/day)")
    private double feedRate = 0.1;

    @SliderBinding(min = 1, max = 25)
    private double feedRadius = 10;

    @SliderBinding(min = 1, max = 25)
    private double satisfiedThreshold = 6;

    @Override
    public void influence(RiverSystem system, double dt) {
        network = system.getNetwork();
        env = (DiscreteEnvironment) system.getEnvironments().get("nutrients");
        this.dt = dt;

        for (Agent a : system.getAgents()) {
            double energy = a.getAttributes().getDouble("energy");
            if (energy >= satisfiedThreshold) {
                continue;
            }

            Location loc = a.getLocation();
            double vi = env.getVirtualIndex(loc.getArc(), loc.getPosition());

            // Start feeding at the closest point.
            try {
                double uptake = 0;
                uptake += feedPoint(0, env.closestTo(loc), null);
                a.getAttributes().put("energy", energy + uptake);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Simulates feeding at a certain point on the nutrient environment.
     * @param distance Distance from the fish.
     */
    private double feedPoint(double distance, DiscretePoint point, DiscretePoint previousPoint) {
        if (distance > feedRadius) {
            return 0;
        }

        double amountFed = 0;

        final double factor = env.getSeparation(point.getArc()) / feedRadius;
        final double rate = feedRate * factor * TimeUtils.days(dt);

        // A fish will never eat more food than available, or if it's full.
        amountFed = Math.min(point.getValue(), rate);
        point.setValue(Math.max(0, point.getValue() - amountFed));

        for (DiscretePoint.DiscreteNeighbor n : point.getNeighbors()) {
            if (n.getPoint() != previousPoint) {
                amountFed += feedPoint(distance + n.getDistance(), n.getPoint(), point);
            }
        }

        return amountFed;
    }

    public double getFeedRate() {
        return feedRate;
    }

    public void setFeedRate(double feedRate) {
        this.feedRate = feedRate;
    }

    public double getFeedRadius() {
        return feedRadius;
    }

    public void setFeedRadius(double feedRadius) {
        this.feedRadius = feedRadius;
    }

    public double getSatisfiedThreshold() {
        return satisfiedThreshold;
    }

    public void setSatisfiedThreshold(double satisfiedThreshold) {
        this.satisfiedThreshold = satisfiedThreshold;
    }
}
