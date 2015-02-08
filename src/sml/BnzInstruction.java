package sml;

/**
 * This class ....
 * 
 * @author someone
 */

public class BnzInstruction extends Instruction {
	private int register;
        private String function;
        private int value;

	public BnzInstruction(String label, String opcode) {
		super(label, opcode);
	}

	public BnzInstruction(String label, int register, String function) {
		super(label, "bnz");
		this.register = register;
                this.function = function;
	}

        /**
         * if the register is skip else goto another 
         * function in the text file.
         * @param m 
         */
	@Override
	public void execute(Machine m) {
            value = m.getRegisters().getRegister(register);
            // Provides an exit
            if(!isZero())
            {
                int index = m.getLabels().indexOf(function);
                if(index != -1) // Valid index... 
                {
                    m.setPc(index);
                }
                else
                {
                    System.out.println("Function (" + label + ") references missing function (" + function + ").");
                }
            }
	}

	@Override
	public String toString() {
		return super.toString() + " Reg(" + register + ") value is " + (isZero()? "zero therefore exiting loop." : "not zero therefore will call function (" + function + ").");
	}
        
        public boolean isZero()
        {
            return (value == 0);
        }
}
