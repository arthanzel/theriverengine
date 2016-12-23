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
    private RiverNetwork network;
    private DiscreteEnvironment env;
    private double dt;

    @DoubleBinding(min = 0, max = 2)
    @BindingName("Feed Rate (/day)")
    private double feedRate = 0.1;

    @Override
    public void influence(RiverSystem system, double dt) {
        network = system.getNetwork();
        env = (DiscreteEnvironment) system.getEnvironments().get("nutrients");
        this.dt = dt;

        for (Agent a : system.getAgents()) {
            Location loc = a.getLocation();
            double vi = env.getVirtualIndex(loc.getArc(), loc.getPosition());

            try {
                feedPoint(vi % 1, loc.getArc(), (int) vi, false, false);
                feedPoint(1 - vi % 1, loc.getArc(), (int) vi, true, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void feedPoint(double distance, RiverArc arc, int vi, boolean downstream, boolean skip) {
        if (distance > 20) {
            return;
        }

        DiscretePoint dp = env.getPoints(arc)[vi];

        if (!skip) {
            final double rate = feedRate / TimeUtils.S_IN_DAY * dt;

            dp.setValue(Math.max(0, dp.getValue() - rate));
        }

        // Find the next discrete point
        if (dp.isNode()) {
            if (downstream) {
                for (RiverArc nextArc : Graphs.downstreamEdgesOf(network, arc)) {
                    feedPoint(distance + env.getSeparation(arc), nextArc, 1, true, false);
                }
            }
            else {
                for (RiverArc nextArc : Graphs.upstreamEdgesOf(network, arc)) {
                    feedPoint(distance + env.getSeparation(arc), nextArc, env.getPoints(arc).length - 2, false, false);
                }
            }
        }
        else {
            double separation = env.getSeparation(arc);
            if (downstream) {
                feedPoint(distance + separation, arc, vi + 1, true, false);
            }
            else {
                feedPoint(distance + separation, arc, vi - 1, false, false);
            }
        }
    }

    public double getFeedRate() {
        return feedRate;
    }

    public void setFeedRate(double feedRate) {
        this.feedRate = feedRate;
    }
}
