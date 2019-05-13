package com.abhishek360.dev.logicsynthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BranchWire extends AbsSingleInputComponent
{
    private final Set<AbsCircuitComponent> outputs = new HashSet<>();

    public BranchWire()
    {
        super(null);
    }

    @Override
    public boolean doCycle()
    {
        return input.doCycle();
    }

    public void connectTo(AbsCircuitComponent circuitComponent)
    {
        outputs.add(circuitComponent);
    }

    public void removeFrom(AbsCircuitComponent circuitComponent)
    {
        outputs.remove(circuitComponent);
    }

    public Set<AbsCircuitComponent> getOutputs()
    {
        return Collections.unmodifiableSet(outputs);
    }

    @Override
    public List<AbsCircuitComponent> getInputComponents()
    {
        return Arrays.asList(input);
    }

    @Override
    public List<AbsCircuitComponent> getOutputComponents()
    {
        return new ArrayList<>(outputs);
    }
}
