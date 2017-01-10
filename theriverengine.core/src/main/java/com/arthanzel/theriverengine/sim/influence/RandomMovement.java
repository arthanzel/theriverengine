package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.ui.DoubleBinding;
import com.arthanzel.theriverengine.util.FishMath;

import java.util.Random;

/**
 * Influence that applies a random motion to the Agents present in a RiverSystem.
 *
 * @author Martin
 */
public class RandomMovement extends BaseInfluence {
    @DoubleBinding(min = 0, max = 10)
    private volatile double spread = 2.0;

//    @BooleanBinding
//    private volatile boolean parallel = false;

    Random r = new Random();

    public void influence(RiverSystem system, double dt) {
//        if (parallel) {
//            for (final Agent a : system.getAgents()) {
//                getPool().submit(() -> {
//                    run(a);
//                });
//            }
//        }
//        else {
        for (final Agent a : system.getAgents()) {
            run(a);
        }
//        }
    }

    private void run(Agent a) {
        // TODO: Put Random in a ThreadLocal
        final double v = a.getAttributes().getDouble("velocity");
        a.getAttributes().put("velocity", v + FishMath.random(-spread, spread));
    }

    public double getSpread() {
        return spread;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }

//    public boolean isParallel() {
//        return parallel;
//    }
//
//    public void setParallel(boolean parallel) {
//        this.parallel = parallel;
//    }
}
