package sml;

/**
 * This class ....
 * 
 * @author someone
 */

public class BnzInstruction extends Instruction {
	private int register;
        private String function;
        private boolean isZero;

	public BnzInstruction(String label, String opcode) {
		super(label, opcode);
	}

	public BnzInstruction(String label, int register, String function) {
		super(label, "bnz");
		this.register = register;
                this.function = function;
	}

	@Override
	public void execute(Machine m) {
            if(m.getRegisters().getRegister(register)!=0)
            {
                int index = m.getLabels().indexOf(function);
                if(index != -1)
                {
                    m.setPc(index);
                    // Instruction get = m.getProg().get(index);
                    // get.execute(m);
                }
                else
                {
                    System.out.println("Function (" + label + ") references missing function (" + function + ").");
                }
            }
	}

	@Override
	public String toString() {
		return super.toString() + " Reg(" + register + ") value is " + (isZero()? "zero" : "not zero" + " so calling function (" + function + ").");
	}
        
        public boolean isZero()
        {
            return isZero;
        }
}
