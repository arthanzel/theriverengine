package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.sim.environment.DiscreteEnvironment;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.ui.BindingName;
import com.arthanzel.theriverengine.ui.DoubleBinding;

/**
 * Base implementation of an Influence. BaseInfluence does very little except provide getters and setters for the
 * influence's "enabled" flag. Every influence should extend BaseInfluence.
 *
 * @author Martin
 */
public class NutrientDynamics extends BaseInfluence {
    @BindingName("Growth Rate (constant)")
    @DoubleBinding(min = 0, max = 1)
    private volatile double growthRate = 0.02;

    @BindingName("Death Rate (% of population)")
    @DoubleBinding(min = 0, max = 1)
    private volatile double deathRate = 0.05;

    @Override
    public void influence(RiverSystem system, double dt) {
        boolean first = true;

        DiscreteEnvironment env = (DiscreteEnvironment) system.getEnvironments().get("nutrients");
//        for (RiverArc key : env.getValues().keySet()) {
//            double[] values = env.getValues().get(key);
//            for (int i = 0; i < values.length; i++) {
//                double n = values[i];
//                double n1 = n + growthRate - deathRate * n;
//                values[i] = n1;
//
//                if (first) {
//                    first = false;
//                }
//            }
//            env.getValues().put(key, values);
//        }

        first = false;
    }

    public double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate;
    }

    public double getDeathRate() {
        return deathRate;
    }

    public void setDeathRate(double deathRate) {
        this.deathRate = deathRate;
    }
}
