package com.abhishek360.dev.logicsynthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class OutputGate extends AbsSingleInputComponent
{

    public OutputGate(String name)
    {
        super(name);
    }

    @Override
    public boolean doCycle() {
        return input.doCycle();
    }

    @Override
    public List<AbsCircuitComponent> getInputComponents() {
        return Arrays.asList(input);
    }

    @Override
    public List<AbsCircuitComponent> getOutputComponents()
    {
        if (output==null)
        {
            return Collections.<AbsCircuitComponent>emptyList();
        }

        if (output instanceof BranchWire )
        {
            return new ArrayList<>(((BranchWire) output).getOutputs());
        }

        return Arrays.asList(output);

    }
}
