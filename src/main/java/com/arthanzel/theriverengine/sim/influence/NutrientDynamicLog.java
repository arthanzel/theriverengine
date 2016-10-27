package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.sim.Environment;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.ui.BindingName;
import com.arthanzel.theriverengine.ui.DoubleBinding;

/**
 * Base implementation of an Influence. BaseInfluence does very little except provide getters and setters for the
 * influence's "enabled" flag. Every influence should extend BaseInfluence.
 *
 * @author Martin
 */
public class NutrientDynamicLog extends BaseInfluence {
    @DoubleBinding(min = 0, max = 1)
    private volatile double growthRate = 0.02;

    @DoubleBinding(min = 0, max = 1)
    private volatile double carryingCapacity = 0.05;

    @Override
    public void influence(RiverSystem system, double dt) {
        if (!this.isEnabled()) return;

        Environment env = system.getEnvironment("nutrients");
        for (RiverArc key : env.getValues().keySet()) {
            double[] values = env.getValues().get(key);
            for (int i = 0; i < values.length; i++) {
                double n = values[i];
                double iota = Math.random() * 0.0000001;
                double n1 = n + growthRate * n * (1 - n / carryingCapacity) + iota;
                values[i] = n1;
            }
            env.getValues().put(key, values);
        }
    }

    public double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate;
    }

    public double getCarryingCapacity() {
        return carryingCapacity;
    }

    public void setCarryingCapacity(double carryingCapacity) {
        this.carryingCapacity = carryingCapacity;
    }
}
