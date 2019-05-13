package com.abhishek360.dev.logicsynthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NotGate extends AbsSingleInputComponent
{

    public NotGate(String name)
    {
        super(name);
    }

    @Override
    public boolean doCycle()
    {
        return !input.doCycle();
    }

    @Override
    public List<AbsCircuitComponent> getInputComponents()
    {
        return Arrays.asList(input);
    }

    @Override
    public List<AbsCircuitComponent> getOutputComponents()
    {
       if (output instanceof BranchWire)
       {
           return new ArrayList<>(((BranchWire) output).getOutputs());
       }
       else
       {
           return Arrays.asList(output);
       }
    }
}
