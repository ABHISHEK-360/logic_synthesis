package com.abhishek360.dev.logicsynthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class OrGate extends AbsDoubleInputComponent
{

    public OrGate(String name)
    {
        super(name);
    }

    @Override
    public boolean doCycle()
    {
        return input1.doCycle()||input2.doCycle();
    }

    @Override
    public List<AbsCircuitComponent> getInputComponents()
    {
        return Arrays.asList(input1,input2);
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
