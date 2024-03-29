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

            case 4 : createFullAdder();

                break;

            case 5 : createSRFlipFlop();

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


    private void createFullAdder()
    {
        circuit= new Circuit("myFullAdder",3,2);

        circuit.addAndGate("and11");
        circuit.addAndGate("and12");
        circuit.addNotGate("not11");
        circuit.addNotGate("not12");
        circuit.addOrGate("or11");

        circuit.addAndGate("and21");
        circuit.addAndGate("and22");
        circuit.addNotGate("not21");
        circuit.addNotGate("not22");
        circuit.addOrGate("or21");

        circuit.connect("inputPin0").to("not11");
        circuit.connect("not11").toFirstPinOf("and11");
        circuit.connect("inputPin1").toSecondPinOf("and11");
        circuit.connect("inputPin1").to("not12");
        circuit.connect("not12").toSecondPinOf("and12");
        circuit.connect("inputPin0").toFirstPinOf("and12");
        circuit.connect("and11").toFirstPinOf("or11");
        circuit.connect("and12").toSecondPinOf("or11");
        circuit.connect("or11").to("not21");


        //circuit.connect("inputPin0").to("not21");
        circuit.connect("not21").toFirstPinOf("and21");
        circuit.connect("inputPin2").toSecondPinOf("and21");
        circuit.connect("inputPin2").to("not22");
        circuit.connect("not22").toSecondPinOf("and22");
        circuit.connect("or11").toFirstPinOf("and22");
        circuit.connect("and21").toFirstPinOf("or21");
        circuit.connect("and22").toSecondPinOf("or21");
        circuit.connect("or21").to("outputPin0");


        circuit.addAndGate("and31");
        circuit.addAndGate("and32");
        circuit.addOrGate("or31");

        circuit.connect("inputPin0").toFirstPinOf("and31");
        circuit.connect("inputPin1").toSecondPinOf("and31");

        circuit.connect("or11").toFirstPinOf("and32");
        circuit.connect("inputPin2").toSecondPinOf("and32");

        circuit.connect("and31").toFirstPinOf("or31");
        circuit.connect("and32").toSecondPinOf("or31");

        circuit.connect("or31").to("outputPin1");

        circuit.lock();





    }

    private void createSRFlipFlop()
    {
        circuit= new Circuit("mySRFF",3,2);

        circuit.addAndGate("and1");
        circuit.addNotGate("not1");
        circuit.addAndGate("and2");
        circuit.addNotGate("not2");

        circuit.connect("inputPin0").toFirstPinOf("and1");
        circuit.connect("not2").toSecondPinOf("and1");
        circuit.connect("and1").to("not1");
        circuit.connect("not1").to("outputPin0");


        circuit.connect("inputPin1").toFirstPinOf("and2");
        circuit.connect("inputPin2").toSecondPinOf("and2");
        circuit.connect("and2").to("not2");
        circuit.connect("not2").to("outputPin1");

        circuit.lock();





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

    public boolean[] getFullAdderOutput(boolean bit1,boolean bit2,boolean bit3)
    {
        return  circuit.doCycle(bit1,bit2,bit3);

    }

    public boolean[] getSRFFOutput(boolean bit1,boolean bit2,boolean bit3)
    {
        return  circuit.doCycle(bit1,bit2,bit3);

    }
}
