package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.sim.RiverSystem;

import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * Created by martin on 2017-03-05.
 */
public class FireableInfluence extends BaseInfluence {
    private LinkedList<Method> pendingActions = new LinkedList<>();

    @Override
    public void influence(RiverSystem system, double dt) {

    }
}
