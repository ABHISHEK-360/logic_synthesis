package com.abhishek360.dev.logicsynthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AndGate extends AbsDoubleInputComponent
{

    public AndGate(String name)
    {
        super(name);
    }

    public boolean doCycle()
    {
        return input1.doCycle()&& input2.doCycle();
    }

    public List<AbsCircuitComponent> getInputComponents()
    {
        return new ArrayList<>(Arrays.asList(input1,input2));

    }

    @Override
    public List<AbsCircuitComponent> getOutputComponents()
    {

        if (output instanceof BranchWire)
            return new ArrayList<>(((BranchWire) output).getOutputs());
        else
        {
            return Arrays.asList(output);
        }
    }
}
