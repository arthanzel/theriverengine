package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.sim.agent.Location;
import com.arthanzel.theriverengine.sim.environment.DiscreteEnvironment;
import com.arthanzel.theriverengine.sim.environment.DiscretePoint;
import com.arthanzel.theriverengine.sim.environment.Environment;
import com.arthanzel.theriverengine.ui.DoubleBinding;

import java.util.Set;

/**
 * @author Martin
 */
public class FeedingInfluence extends BaseInfluence {
    RiverNetwork network;
    DiscreteEnvironment env;

    @Override
    public void influence(RiverSystem system, double dt) {
        network = system.getNetwork();
        env = (DiscreteEnvironment) system.getEnvironments().get("nutrients");

        for (Agent a : system.getAgents()) {
            Location loc = a.getLocation();
            double vi = env.getVirtualIndex(loc.getArc(), loc.getPosition());

            try {
                feedPoint(vi % 1, loc.getArc(), (int) vi, false);
                //feedPoint(1 - vi % 1, loc.getArc(), (int) vi + 1, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void feedPoint(double distance, RiverArc arc, int vi, boolean downstream) {
        if (distance > 20) {
            return;
        }

        DiscretePoint dp = env.getPoint(arc, vi);
        dp.setValue(Math.max(0, dp.getValue() - 0.1));

        // Find the next discrete point
        if (dp.isNode()) {

        }
        else {
            double separation = env.getSeparation(arc);
            if (downstream) {
                feedPoint(distance + separation, arc, vi + 1, true);
            }
            else {
                feedPoint(distance + separation, arc, vi - 1, false);
            }
        }
    }
}
