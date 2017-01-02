package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.sim.agent.Location;
import com.arthanzel.theriverengine.sim.environment.DiscreteEnvironment;
import com.arthanzel.theriverengine.sim.environment.DiscretePoint;
import com.arthanzel.theriverengine.sim.environment.Environment;
import com.arthanzel.theriverengine.ui.BindingName;
import com.arthanzel.theriverengine.ui.DoubleBinding;
import com.arthanzel.theriverengine.util.Graphs;
import com.arthanzel.theriverengine.util.TimeUtils;

import java.util.Set;

/**
 * @author Martin
 */
public class FeedingInfluence extends BaseInfluence {
    private static final double MAX_DISTANCE = 20;

    private RiverNetwork network;
    private DiscreteEnvironment env;
    private double dt;

    @DoubleBinding(min = 0, max = 2)
    @BindingName("Feed Rate (/day)")
    private double feedRate = 0.1;

    @DoubleBinding(min = 1, max = 25)
    private double feedRadius = 10;

    @Override
    public void influence(RiverSystem system, double dt) {
        network = system.getNetwork();
        env = (DiscreteEnvironment) system.getEnvironments().get("nutrients");
        this.dt = dt;

        for (Agent a : system.getAgents()) {
            Location loc = a.getLocation();
            double vi = env.getVirtualIndex(loc.getArc(), loc.getPosition());
            double dist = loc.getPosition() - env.getPosition(loc.getArc(), (int) vi);
            assert dist < env.getSeparation(loc.getArc());

            try {
                double uptake = 0;
                uptake += feedPoint(dist, loc.getArc(), (int) vi, false, false);
                uptake += feedPoint(-dist, loc.getArc(), (int) vi, true, true);
                double energy = a.getAttributes().getDouble("energy");
                System.out.println("e" + energy);
                System.out.println("u" + uptake);
                a.getAttributes().put("energy", energy + uptake);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Simulates feeding at a certain point on the nutrient environment.
     * @param distance Distance from the fish.
     * @param arc Arc on which to feed.
     * @param vi Virtual index of the discrete point.
     * @param downstream Whether to propagate feeding in the downstream or upstream direction.
     * @param skip Whether to skip the actual feeding and propagate only.
     */
    private double feedPoint(double distance, RiverArc arc, int vi, boolean downstream, boolean skip) {
        if (distance > feedRadius) {
            return 0;
        }

        DiscretePoint dp = env.getPoints(arc)[vi];
        double amountFed = 0;

        if (!skip) {
            final double factor = Math.max(0, 1 - distance / feedRadius);
            final double rate = feedRate * factor * TimeUtils.days(dt);
            amountFed = Math.max(0, dp.getValue() - rate);
            dp.setValue(amountFed);
        }

        // Find the next discrete point
        if (dp.isNode()) {
            if (downstream) {
                for (RiverArc nextArc : arc.getDownstreamArcs()) {
                    boolean ds = arc.getDownstreamNode().getDownstreamArcs().contains(nextArc);
                    int nextVI = ds ? 1 : env.getPoints(nextArc).length - 2;
                    amountFed += feedPoint(distance + env.getSeparation(nextArc), nextArc, nextVI, ds, false);
                }
            }
            else {
                for (RiverArc nextArc : arc.getUpstreamArcs()) {
                    boolean ds = arc.getUpstreamNode().getDownstreamArcs().contains(nextArc);
                    int nextVI = ds ? 1 : env.getPoints(nextArc).length - 2;
                    amountFed += feedPoint(distance + env.getSeparation(nextArc), nextArc, nextVI, ds, false);
                }
            }
        }
        else {
            double separation = env.getSeparation(arc);
            if (downstream) {
                amountFed += feedPoint(distance + separation, arc, vi + 1, true, false);
            }
            else {
                amountFed += feedPoint(distance + separation, arc, vi - 1, false, false);
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
}
