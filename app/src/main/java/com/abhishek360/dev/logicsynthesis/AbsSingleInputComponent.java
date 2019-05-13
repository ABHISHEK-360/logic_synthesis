package com.abhishek360.dev.logicsynthesis;

public abstract class AbsSingleInputComponent extends AbsCircuitComponent
{
    protected AbsCircuitComponent input;


    public AbsSingleInputComponent(String name)
    {
        super(name);
    }

    public AbsCircuitComponent getInputComponent()
    {
        return input;

    }

    public void setInputComponent(AbsCircuitComponent input)
    {
        this.input=input;
    }
}
