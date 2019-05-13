package com.abhishek360.dev.logicsynthesis;

public class LogicGates
{
    private Circuit circuit;
    private Circuit myXOR;




    public LogicGates(int gateCode)
    {
        switch (gateCode)
        {
            case 0 :   createANDGate();
                        break;
            case 1:   createORGAte();
                break;

            case 2 :   createXORGate();
                break;

            case 3 : createHalfAdder();

                break;

        }

    }

    private void createXORGate()
    {
        circuit = new Circuit("myXor", 2, 1);


        circuit.addAndGate("and1");
        circuit.addAndGate("and2");
        circuit.addNotGate("not1");
        circuit.addNotGate("not2");
        circuit.addOrGate("or");
        circuit.connect("inputPin0").to("not1");
        circuit.connect("not1").toFirstPinOf("and1");
        circuit.connect("inputPin1").toSecondPinOf("and1");
        circuit.connect("inputPin1").to("not2");
        circuit.connect("not2").toSecondPinOf("and2");
        circuit.connect("inputPin0").toFirstPinOf("and2");
        circuit.connect("and1").toFirstPinOf("or");
        circuit.connect("and2").toSecondPinOf("or");
        circuit.connect("or").to("outputPin0");

        myXOR=circuit;
    }

    private void createORGAte()
    {
        circuit = new Circuit("myOr",2,1);

        circuit.addOrGate("or1");
        circuit.connect("inputPin0").toFirstPinOf("or1");
        circuit.connect("inputPin1").toSecondPinOf("or1");
        circuit.connect("or1").to("outputPin0");
    }

    private void createANDGate()
    {
        circuit = new Circuit("myAnd",2,1);

        circuit.addAndGate("and1");
        circuit.connect("inputPin0").toFirstPinOf("and1");
        circuit.connect("inputPin1").toSecondPinOf("and1");
        circuit.connect("and1").to("outputPin0");
    }

    private void createHalfAdder()
    {
        circuit= new Circuit("myHalfAdder",2,2);

        circuit.addAndGate("and1");
        circuit.addAndGate("and2");
        circuit.addNotGate("not1");
        circuit.addNotGate("not2");
        circuit.addOrGate("or");
        circuit.connect("inputPin0").to("not1");
        circuit.connect("not1").toFirstPinOf("and1");
        circuit.connect("inputPin1").toSecondPinOf("and1");
        circuit.connect("inputPin1").to("not2");
        circuit.connect("not2").toSecondPinOf("and2");
        circuit.connect("inputPin0").toFirstPinOf("and2");
        circuit.connect("and1").toFirstPinOf("or");
        circuit.connect("and2").toSecondPinOf("or");
        circuit.connect("or").to("outputPin0");


        circuit.addAndGate("and3");

        circuit.connect("inputPin0").toFirstPinOf("and3");
        circuit.connect("inputPin1").toSecondPinOf("and3");

        circuit.connect("and3").to("outputPin1");




    }

    public boolean getXOROutput(boolean bit1,boolean bit2)
    {
        return  circuit.doCycle(bit1,bit2)[0];

    }

    public boolean getOROutput(boolean bit1,boolean bit2)
    {
        return  circuit.doCycle(bit1,bit2)[0];

    }

    public boolean getANDOutput(boolean bit1,boolean bit2)
    {
        return  circuit.doCycle(bit1,bit2)[0];

    }

    public boolean[] getHalfAdderOutput(boolean bit1,boolean bit2)
    {
        return  circuit.doCycle(bit1,bit2);

    }
}
