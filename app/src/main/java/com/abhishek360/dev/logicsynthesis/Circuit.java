package com.abhishek360.dev.logicsynthesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public final class Circuit extends AbsCircuitComponent
{
    private static final int MINMUN_INPUT_PINS=1;
    private static final int MINIMUM_OUTPUT_PINS=1;
    private static final String INPUT_PIN_PREFIX="inputPin";
    private static final String OUTPUT_PIN_PREFIX="outputPin";
    private final Map<String,AbsCircuitComponent> componentMap = new TreeMap<>();
    private final Set<AbsCircuitComponent> componentSet = new HashSet<>();
    private final int inputPinNos;
    private final int outputPinNos;
    private final List<InputGate> inputGates;
    private final List<OutputGate> outputGates;
    private boolean locked = false;

    public Circuit(String name, int inputPins,int outputPins)
    {
        super(checkName(name));
        this.inputPinNos=inputPins;
        this.outputPinNos=outputPins;

        this.inputGates= new ArrayList<>(this.inputPinNos);
        this.outputGates= new ArrayList<>(this.outputPinNos);

        for (int i=0;i<inputPins;i++)
        {
            String inputComponentName = INPUT_PIN_PREFIX+i;
            InputGate inputComponent = new InputGate(inputComponentName);

            componentMap.put(inputComponentName,inputComponent);
            inputGates.add(inputComponent);
            addComponent(inputComponent);
        }

        for (int i=0;i<outputPins;i++)
        {
            String outputComponentName = OUTPUT_PIN_PREFIX+i;
            OutputGate outputComponent = new OutputGate(outputComponentName);

            componentMap.put(outputComponentName,outputComponent);
            outputGates.add(outputComponent);
            addComponent(outputComponent);
        }


    }

    public Circuit(Circuit circuit,String name)
    {
        this(checkName(name),circuit.getInputPinNos(),circuit.getOutputPinNos());

        Map<AbsCircuitComponent,AbsCircuitComponent> componentMap = new HashMap<>(circuit.componentSet.size());

        for (InputGate mappedInputGate : this.inputGates)
        {
            AbsCircuitComponent inputGate = circuit.componentMap.get(mappedInputGate.getName());
            componentMap.put(inputGate,mappedInputGate);
        }

        for (OutputGate mappedOutputGate : this.outputGates)
        {
            AbsCircuitComponent outputGate = circuit.componentMap.get(mappedOutputGate.getName());
            componentMap.put(outputGate,mappedOutputGate);
        }

        for (AbsCircuitComponent component : circuit.componentSet)
        {
            if (component instanceof InputGate || component instanceof OutputGate)
            {
                continue;
            }

            AbsCircuitComponent newComponent = copyComponent(component);
            componentMap.put(component,newComponent);

        }

        for (AbsCircuitComponent component : circuit.componentSet)
        {
            AbsCircuitComponent mappedComponent = componentMap.get(component);

            for (AbsCircuitComponent inputComponent : component.getInputComponents())
            {
                AbsCircuitComponent mappedInputComponent = componentMap.get(inputComponent);

                connectInput(component,inputComponent,mappedComponent,mappedInputComponent);

            }

            for (AbsCircuitComponent outputComponent : component.getOutputComponents())
            {
                AbsCircuitComponent mappedOutputComponent = componentMap.get(outputComponent);

                connectOutput(component,mappedComponent,mappedOutputComponent);

            }

        }
    }

    private void connectOutput(AbsCircuitComponent component, AbsCircuitComponent mappedComponent, AbsCircuitComponent mappedOutputComponent)
    {
        if (component instanceof  BranchWire)
        {
            BranchWire branchWire = (BranchWire) mappedComponent;
            branchWire.connectTo(mappedOutputComponent);

        }
        else
        {
            mappedComponent.setOutputComponent(mappedOutputComponent);
        }
    }

    private void connectInput(AbsCircuitComponent component, AbsCircuitComponent inputComponent,AbsCircuitComponent mappedComponent, AbsCircuitComponent mappedInputComponent)
    {
        if (mappedComponent instanceof AbsSingleInputComponent )
        {
            ((AbsSingleInputComponent) mappedComponent).setInputComponent(mappedInputComponent);
        }
        else {

            AbsDoubleInputComponent c1 = (AbsDoubleInputComponent) mappedComponent;
            AbsDoubleInputComponent c2 = (AbsDoubleInputComponent) component;

            if(inputComponent ==c2.getInputComponent1())
            {
                c1.setInputComponent1(mappedInputComponent);

            }
            else
            {
                c1.setInputComponent2(mappedInputComponent);
            }

        }
    }

    private AbsCircuitComponent copyComponent(AbsCircuitComponent component)
    {
        if (component instanceof NotGate)
        {
            NotGate gate = (NotGate) component;
            return new NotGate(gate.getName());
        }

        if (component instanceof AndGate)
        {
            AndGate gate = (AndGate) component;
            return new AndGate(gate.getName());
        }

        if (component instanceof OrGate)
        {
            OrGate gate = (OrGate) component;
            return new OrGate(gate.getName());
        }

        if (component instanceof BranchWire)
        {
            BranchWire wire = (BranchWire) component;
            return new BranchWire();

        }

        if (component instanceof InputGate)
        {
            InputGate gate = (InputGate) component;
            return new InputGate(gate.getName());
        }

        if (component instanceof OutputGate)
        {
            OutputGate gate = (OutputGate) component;
            return new OutputGate(gate.getName());
        }

        throw new IllegalStateException("Unknown Gate "+component.getClass());
    }

    public int size()
    {
        return componentSet.size();
    }

    public void addNotGate(String gateName)
    {
        checkisNotLocked();
        checkNewGateName(gateName);
        NotGate notGate = new NotGate(gateName);
        componentMap.put(gateName,notGate);
        componentSet.add(notGate);
    }

    public void addAndGate(String gateName)
    {
        checkisNotLocked();
        checkNewGateName(gateName);
        AndGate andGate = new AndGate(gateName);
        componentMap.put(gateName,andGate);
        componentSet.add(andGate);
    }

    public void addOrGate(String gateName)
    {
        checkisNotLocked();
        checkNewGateName(gateName);
        OrGate orGate = new OrGate(gateName);
        componentMap.put(gateName,orGate);
        componentSet.add(orGate);
    }

    public void addCircuit(Circuit circuit)
    {
        checkisNotLocked();
        checkNewGateName(circuit.getName());
        componentMap.put(circuit.getName(),circuit);
        componentSet.add(circuit);
    }

    @Override
    public boolean doCycle()
    {
        for (OutputGate outputGate: outputGates)
        {
            outputGate.doCycle();

        }
        return false;
    }

    public boolean[] doCycle(boolean... bits)
    {
        setInputBits(bits);
        doCycle();
        return getOutputBits();
    }

    private void setInputBits(boolean... bits)
    {
        Objects.requireNonNull(bits,"null input bit array");
        unsetAllInputPins();
        for (int i = 0; i< Math.min(bits.length,inputGates.size());i++)
        {
            inputGates.get(i).setBit(bits[i]);

        }

    }

    private boolean[] getOutputBits()
    {
        boolean[] bits = new boolean[outputPinNos];

        for (int i=0; i<bits.length;++i)
        {
            bits[i]=outputGates.get(i).doCycle();

        }
        return bits;
    }

    public void lock()
    {
        if (locked)
        {
            return;
        }
        locked=true;
        checkAllPinsAreConnected();
        checkIsDagInForwardDirection();
        checkIsDagInBackwardDirection();

    }

    public TargetComponentSelector connect(String srcComponentName)
    {
        checkisNotLocked();
        return new TargetComponentSelector(srcComponentName);

    }

    public final class TargetComponentSelector {
        private final AbsCircuitComponent srcComponent;

        TargetComponentSelector(String srcComponentName) {
            Objects.requireNonNull(srcComponentName, "null name src component");

            AbsCircuitComponent srcComponent;

            if (srcComponentName.contains(".")) {
                String[] nameComponent = srcComponentName.split("\\.");
                if (nameComponent.length != 2) {
                    throw new IllegalStateException("More than 1 dot operators" + srcComponentName);

                }
                Circuit subCircuit = (Circuit) componentMap.get(nameComponent[0]);

                if (subCircuit == null) {
                    throw new IllegalArgumentException("Subscript \"" + nameComponent[0] + "\" is not present in \"" + getName() + "\"");
                }
                srcComponent = subCircuit.componentMap.get(nameComponent[1]);


            } else {
                srcComponent = componentMap.get(srcComponentName);

            }

            if (srcComponent == null) {
                throwComponentNotPresent(srcComponentName);
            }

            this.srcComponent = srcComponent;
        }

        public void toFirstPinOf(String targetComponentName) {
            AbsCircuitComponent targetComponent = getTargetComponent(targetComponentName);

            checkIsDoubleInputGate(targetComponent);

            if (((AbsDoubleInputComponent) targetComponent).getInputComponent1() != null) {
                throw new MyException("The 1st input pin of \"" + targetComponentName + "\"" + " is occupied.");
            }

            if (srcComponent.getOutputComponent() == null)
            {
                ((AbsDoubleInputComponent) targetComponent).setInputComponent1(srcComponent);

                srcComponent.setOutputComponent(targetComponent);

            }
            else if (srcComponent.getOutputComponent() instanceof BranchWire)
            {
                ((AbsDoubleInputComponent) targetComponent).setInputComponent1(srcComponent.getOutputComponent());

                ((BranchWire) srcComponent.getOutputComponent()).connectTo(targetComponent);

            }
            else
                {

                BranchWire branchWire = new BranchWire();
                addComponent(branchWire);

                branchWire.connectTo(srcComponent.getOutputComponent());
                branchWire.connectTo(targetComponent);
                if (srcComponent.getOutputComponent() instanceof AbsDoubleInputComponent) {
                    AbsDoubleInputComponent tmpComponent = (AbsDoubleInputComponent) srcComponent.getOutputComponent();
                    if (tmpComponent.getInputComponent1() == srcComponent)
                    {
                        tmpComponent.setInputComponent1(branchWire);
                    } else {
                        tmpComponent.setInputComponent2(branchWire);
                    }
                } else {
                    AbsSingleInputComponent tmpComponent = (AbsSingleInputComponent) srcComponent.getOutputComponent();
                    tmpComponent.setInputComponent(branchWire);

                }

                ((AbsDoubleInputComponent) targetComponent).setInputComponent1(branchWire);
                srcComponent.setOutputComponent(branchWire);
                branchWire.setInputComponent(srcComponent);
            }

        }


        public void toSecondPinOf(String targetComponentName) {
            AbsCircuitComponent targetComponent = getTargetComponent(targetComponentName);

            checkIsDoubleInputGate(targetComponent);
            if (((AbsDoubleInputComponent) targetComponent).getInputComponent2() != null) {
                throw new MyException("The 2nd input pin of \"" + targetComponentName + "\"" + " is occupied.");
            }

            if (srcComponent.getOutputComponent() == null) {
                ((AbsDoubleInputComponent) targetComponent).setInputComponent2(srcComponent);

                srcComponent.setOutputComponent(targetComponent);
            } else if (srcComponent.getOutputComponent() instanceof BranchWire) {
                ((AbsDoubleInputComponent) targetComponent).setInputComponent2(srcComponent.getOutputComponent());

                ((BranchWire) srcComponent.getOutputComponent()).connectTo(targetComponent);

            } else {
                BranchWire branchWire = new BranchWire();
                addComponent(branchWire);

                branchWire.connectTo(srcComponent.getOutputComponent());
                branchWire.connectTo(targetComponent);
                if (srcComponent.getOutputComponent() instanceof AbsDoubleInputComponent) {
                    AbsDoubleInputComponent tmpComponent = (AbsDoubleInputComponent) srcComponent.getOutputComponent();
                    if (tmpComponent.getInputComponent1() == srcComponent) {
                        tmpComponent.setInputComponent1(branchWire);
                    } else {
                        tmpComponent.setInputComponent2(branchWire);
                    }
                } else {
                    AbsSingleInputComponent tmpComponent = (AbsSingleInputComponent) srcComponent.getOutputComponent();
                    tmpComponent.setInputComponent(branchWire);

                }

                ((AbsDoubleInputComponent) targetComponent).setInputComponent2(branchWire);
                srcComponent.setOutputComponent(branchWire);
                branchWire.setInputComponent(srcComponent);
            }

        }

        public void to(String targetComponentName) {
            AbsCircuitComponent targetComponent = getTargetComponent(targetComponentName);

            checkIsSingleInputGate(targetComponent);
            if (((AbsSingleInputComponent) targetComponent).getInputComponent() != null) {
                throw new MyException("The input pin of \"" + targetComponentName + "\"" + " is occupied.");
            }

            if (srcComponent.getOutputComponent() == null) {
                ((AbsSingleInputComponent) targetComponent).setInputComponent(srcComponent);

                srcComponent.setOutputComponent(targetComponent);
            } else if (srcComponent.getOutputComponent() instanceof BranchWire) {
                ((AbsSingleInputComponent) targetComponent).setInputComponent(srcComponent.getOutputComponent());

                ((BranchWire) srcComponent.getOutputComponent()).connectTo(targetComponent);

            } else {
                BranchWire branchWire = new BranchWire();
                addComponent(branchWire);

                branchWire.connectTo(srcComponent.getOutputComponent());
                branchWire.connectTo(targetComponent);

                ((AbsSingleInputComponent) targetComponent).setInputComponent(branchWire);
                srcComponent.setOutputComponent(branchWire);
                branchWire.setInputComponent(srcComponent);
            }

        }

        private AbsCircuitComponent getTargetComponent(String targetComponentName) {
            Objects.requireNonNull(targetComponentName,
                    "The target component name is null.");

            AbsCircuitComponent targetComponent;

            if (targetComponentName.contains(".")) {
                String[] targetComponentNameComponents =
                        targetComponentName.split("\\.");

                if (targetComponentNameComponents.length != 2) {
                    throw new IllegalArgumentException(
                            "More than one dot operators in \"" +
                                    targetComponentName + "\".");
                }

                Circuit subcircuit =
                        (Circuit)
                                componentMap.get(targetComponentNameComponents[0]);

                if (subcircuit == null) {
                    throw new IllegalStateException(
                            "Sub-Circuit \"" + targetComponentNameComponents[0] +
                                    "\" not present in circuit \"" + getName() + "\".");
                }

                targetComponent =
                        subcircuit.componentMap
                                .get(targetComponentNameComponents[1]);
            } else {
                targetComponent = componentMap.get(targetComponentName);
            }

            if (targetComponent == null) {
                throw new IllegalStateException(
                        "The component \"" + targetComponentName + "\" is " +
                                "not present in circuit \"" + getName() + "\".");
            }

            return targetComponent;


        }

        private void throwComponentNotPresent(String componentName) {
            throw new IllegalStateException(
                    "The component \"" + componentName + "\" is " +
                            "not present in the circuit \"" + getName() + "\".");
        }

        private void checkIsSingleInputGate(AbsCircuitComponent gate) {
            if (!(gate instanceof AbsSingleInputComponent)) {
                throw new IllegalArgumentException(
                        "A single input pin component is expected here.");
            }
        }

        private void checkIsDoubleInputGate(AbsCircuitComponent gate) {
            if (!(gate instanceof AbsDoubleInputComponent)) {
                throw new IllegalArgumentException(
                        "A double input pin component is expected here.");
            }
        }

    }

    Map<String,AbsCircuitComponent> getComponentMap()
    {
        return componentMap;
    }

    Set<AbsCircuitComponent> getComponentSet()
    {
        return componentSet;
    }



    private void unsetAllInputPins()
    {
        for (InputGate inputGate:inputGates)
        {
            inputGate.setBit(false);
        }
    }

    private static String checkName(String name)
    {
        Objects.requireNonNull(name, "The circuit name is null.");

        if (name.isEmpty()) {
            throw new IllegalArgumentException("The circuit name is empty.");
        }

        if (name.startsWith(INPUT_PIN_PREFIX)) {
            throw new IllegalArgumentException(
                    "The circuit name (" + name + ") has illegal prefix \"" +
                            INPUT_PIN_PREFIX + "\".");
        }

        if (name.startsWith(OUTPUT_PIN_PREFIX)) {
            throw new IllegalArgumentException(
                    "The circuit name (" + name + ") has illegal prefix \"" +
                            OUTPUT_PIN_PREFIX + "\".");
        }

        return name;
    }

    @Override
    public List<AbsCircuitComponent> getInputComponents()
    {
        return new ArrayList<AbsCircuitComponent>(inputGates);
    }

    @Override
    public List<AbsCircuitComponent> getOutputComponents()
    {
        return new ArrayList<AbsCircuitComponent>(outputGates);
    }
    private static int checkInputPinCount(int inputPins) {
        if (inputPins < MINMUN_INPUT_PINS) {
            throw new IllegalArgumentException(
                    "Too few input pins (" + inputPins + "). At least " +
                            MINMUN_INPUT_PINS + " expected.");
        }

        return inputPins;
    }

    private static int checkOutputPinCount(int outputPins) {
        if (outputPins < MINIMUM_OUTPUT_PINS) {
            throw new IllegalArgumentException(
                    "Too few output pins (" + outputPins + "). At least " +
                            MINIMUM_OUTPUT_PINS + " expected.");
        }

        return outputPins;
    }

    public int getInputPinNos()
    {
        return inputPinNos;
    }

    public int getOutputPinNos()
    {
        return outputPinNos;
    }


    private String checkNewGateName(String gateName) {
        Objects.requireNonNull(gateName, "The new gate name is null.");

        if (gateName.isEmpty()) {
            throw new IllegalArgumentException("The new gate name is empty.");
        }



        if (gateName.startsWith(INPUT_PIN_PREFIX)) {
            throw new IllegalArgumentException(
                    "The new gate name (" + gateName + ") has illegal prefix " +
                            "\"" + INPUT_PIN_PREFIX + "\".");
        }

        if (gateName.startsWith(OUTPUT_PIN_PREFIX)) {
            throw new IllegalArgumentException(
                    "The new gate name (" + gateName + ") has illegal prefix " +
                            "\"" + OUTPUT_PIN_PREFIX + "\".");
        }

        if (componentMap.containsKey(gateName)) {
            throw new IllegalArgumentException(
                    "The new gate name (" + gateName + ") is already "+
                            "occupied.");
        }

        return gateName;
    }

    private void checkisNotLocked()
    {
        if (locked) {
            throw new IllegalStateException(
                    "The circuit \"" + getName() + "\" is locked.");
        }
    }

    private void checkInputGateComplete(InputGate inputGate, String name) {
        if (inputGate.getOutputComponent() == null) {
            throw new MyException(
                    "The input gate \"" + name + "\" has no " +
                            "output gate.");
        }
    }

    private void checkOutputGateComplete(OutputGate outputGate, String name) {
        if (outputGate.getInputComponent() == null) {
            throw new MyException(
                    "The output gate \"" + name + "\" has no input gate.");
        }
    }

    private void checkNotGateComplete(NotGate notGate, String name) {
        if (notGate.getInputComponent() == null) {
            throw new MyException(
                    "The not gate \"" + name + "\" has no input gate.");
        }

        if (notGate.getOutputComponent() == null) {
            throw new MyException(
                    "The not gate \"" + name + "\" has no output gate.");
        }
    }

    private void checkOrGateComplete(OrGate gate, String name) {
        if (gate.getInputComponent1() == null) {
            throw new MyException(
                    "The OrGate \"" + name + "\" has no 1st input gate.");
        }

        if (gate.getInputComponent2() == null) {
            throw new MyException(
                    "The OrGate \"" + name + "\" has no 2nd input gate.");
        }

        if (gate.getOutputComponent() == null) {
            throw new MyException(
                    "The OrGate \"" + name + "\" has no output gate.");
        }
    }

    private void checkAndGateComplete(AndGate gate, String name) {
        if (gate.getInputComponent1() == null) {
            throw new MyException(
                    "The AndGate \"" + name + "\" has no 1st input gate.");
        }

        if (gate.getInputComponent2() == null) {
            throw new MyException(
                    "The AndGate \"" + name + "\" has no 2nd input gate.");
        }

        if (gate.getOutputComponent() == null) {
            throw new MyException(
                    "The AndGate \"" + name + "\" has no output gate.");
        }
    }

    private void checkAllPinsAreConnected() {
        for (Map.Entry<String, AbsCircuitComponent> e :
                componentMap.entrySet()) {
            if (e.getValue() instanceof InputGate) {
                checkInputGateComplete((InputGate) e.getValue(), e.getKey());
            } else if (e.getValue() instanceof OutputGate) {
                checkOutputGateComplete((OutputGate) e.getValue(), e.getKey());
            } else if (e.getValue() instanceof NotGate) {
                checkNotGateComplete((NotGate) e.getValue(), e.getKey());
            } else if (e.getValue() instanceof OrGate) {
                checkOrGateComplete((OrGate) e.getValue(), e.getKey());
            } else if (e.getValue() instanceof AndGate) {
                checkAndGateComplete((AndGate) e.getValue(), e.getKey());
            } else {
                throw new IllegalStateException(
                        "Unknown component type: " + e.getValue());
            }
        }
    }

    private enum NodeColor
    {
        WHITE,
        GRAY,
        BLACK
    }

    private void checkIsDagInForwardDirection()
    {
        Map<AbsCircuitComponent, NodeColor> colors = new HashMap<>();

        for (AbsCircuitComponent component : componentSet) {
            colors.put(component, NodeColor.WHITE);
        }

        for (AbsCircuitComponent component : inputGates) {
            if (colors.get(component).equals(NodeColor.WHITE)) {
                dfsForwardVisit(component, colors);
            }
        }

    }

    private void checkIsDagInBackwardDirection() {
        Map<AbsCircuitComponent, NodeColor> colors = new HashMap<>();

        for (AbsCircuitComponent component : componentSet) {
            colors.put(component, NodeColor.WHITE);
        }

        for (AbsCircuitComponent component : outputGates) {
            if (colors.get(component).equals(NodeColor.WHITE)) {
                dfsBackwardVisit(component, colors);
            }
        }
    }


    void addComponent(AbsCircuitComponent component)
    {
        componentSet.add(component);
    }

    void removeComponent(AbsCircuitComponent component)
    {
        componentSet.remove(component);
    }

    private void dfsForwardVisit(AbsCircuitComponent component, Map<AbsCircuitComponent, NodeColor> colors) {
        colors.put(component, NodeColor.GRAY);

        for (AbsCircuitComponent child : component.getOutputComponents()) {
            if (colors.get(child).equals(NodeColor.GRAY)) {
                throw new MyException(
                        "Forward cycle detected in circuit \"" + getName() +
                                "\".");
            }

            if (colors.get(child).equals(NodeColor.WHITE)) {
                dfsForwardVisit(child, colors);
            }
        }

        colors.put(component, NodeColor.BLACK);
    }

    private void dfsBackwardVisit(AbsCircuitComponent component, Map<AbsCircuitComponent, NodeColor> colors)
    {
        colors.put(component, NodeColor.GRAY);

        for (AbsCircuitComponent parent : component.getInputComponents())
        {
            if (colors.get(parent).equals(NodeColor.GRAY)) {
                throw new MyException(
                        "Backward cycle detected in circuit \"" + getName() +
                                "\".");
            }

            if (colors.get(parent).equals(NodeColor.WHITE)) {
                dfsBackwardVisit(parent, colors);
            }
        }

        colors.put(component, NodeColor.BLACK);
    }
}
