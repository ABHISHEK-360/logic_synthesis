package com.abhishek360.dev.logicsynthesis;

public abstract class AbsDoubleInputComponent extends AbsCircuitComponent
{
    protected AbsCircuitComponent input1;
    protected AbsCircuitComponent input2;



    public AbsDoubleInputComponent(String name)
    {
        super(name);
    }

    public AbsCircuitComponent getInputComponent1()
    {
        return input1;
    }

    public AbsCircuitComponent getInputComponent2()
    {
        return input2;
    }

    public void setInputComponent1(AbsCircuitComponent input1)
    {
        this.input1=input1;
    }

    public void setInputComponent2(AbsCircuitComponent input2)
    {
        this.input2=input2;
    }


}
