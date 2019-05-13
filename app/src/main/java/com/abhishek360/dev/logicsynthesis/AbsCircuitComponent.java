package com.abhishek360.dev.logicsynthesis;

import java.util.List;

public abstract class AbsCircuitComponent
{
    public final String name;

    protected AbsCircuitComponent output;

    public AbsCircuitComponent(String name)
    {
        this.name=name;
    }

    public abstract boolean doCycle();

    public String getName()
    {
        return  name;

    }

    public AbsCircuitComponent getOutputComponent()
    {
        return output;
    }

    public void setOutputComponent(AbsCircuitComponent output)
    {
        this.output=output;

    }

    public abstract List<AbsCircuitComponent> getInputComponents();
    public  abstract List<AbsCircuitComponent> getOutputComponents();

}
