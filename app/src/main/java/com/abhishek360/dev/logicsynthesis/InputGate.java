package com.abhishek360.dev.logicsynthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class InputGate extends AbsSingleInputComponent
{
    public static final boolean DEFAULT_BIT = false;

    private boolean bit;

    public InputGate(String name, boolean bit)
    {
        super(name);
        setBit(bit);
    }

    public InputGate(String name)
    {
        super(name);
    }

    public boolean getBit()
    {
        return bit;
    }

    public void setBit(boolean bit)
    {
        this.bit = bit;
    }

    @Override
    public boolean doCycle()
    {
        if (getInputComponent() !=null)
        {
            return getInputComponent().doCycle();
        }

        return bit;
    }

    @Override
    public List<AbsCircuitComponent> getInputComponents()
    {
        if (input==null)
        {
            return Collections.<AbsCircuitComponent>emptyList();
        }

        return Arrays.asList();

    }

    @Override
    public List<AbsCircuitComponent> getOutputComponents()
    {
        if (output instanceof BranchWire) {
            return new ArrayList<>(((BranchWire) output).getOutputs());
        }
        return Arrays.asList(output);

    }
}
