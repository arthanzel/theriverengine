package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.sim.environment.DiscreteEnvironment;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.environment.Environment;
import com.arthanzel.theriverengine.common.ui.binding.SliderBinding;
import com.arthanzel.theriverengine.common.util.TimeUtils;

/**
 * Base implementation of an Influence. BaseInfluence does very little except provide getters and setters for the
 * influence's "enabled" flag. Every influence should extend BaseInfluence.
 *
 * @author Martin
 */
public class NutrientDynamicsLog extends BaseInfluence {
    @SliderBinding(min = 0, max = 1)
    private volatile double growthRate = 0.02; // What about doubling time of 24 hrs?

    @SliderBinding(min = 0, max = 1)
    private volatile double carryingCapacity = 0.05;

    @SliderBinding(min = 0, max = 1)
    private volatile double spawnRate = 0.05;

    @Override
    public void influence(RiverSystem system, double dt) {
        DiscreteEnvironment env = (DiscreteEnvironment) system.getEnvironments().get("nutrients");
        env.transform((value) -> {
            final double n = value.getValue();
            if (carryingCapacity == 0) {
                value.setValue(0); // Avoid divide-by-zero
                return;
            }
            final double delta = growthRate * n * (1 - n / carryingCapacity) * TimeUtils.days(dt);
            final double spawnDelta = spawnRate * TimeUtils.days(dt);
            value.setValue(n + delta + spawnDelta);
        });
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

    public double getSpawnRate() {
        return spawnRate;
    }

    public void setSpawnRate(double spawnRate) {
        this.spawnRate = spawnRate;
    }
}
