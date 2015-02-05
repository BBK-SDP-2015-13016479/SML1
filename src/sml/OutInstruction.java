package sml;

/**
 * This class ....
 * 
 * @author someone
 */

public class OutInstruction extends Instruction {
	private int register;
        private int value = 0;

	public OutInstruction(String label, String opcode) {
		super(label, opcode);
	}

	public OutInstruction(String label, int register) {
		super(label, "out");
		this.register = register;
	}

	@Override
	public void execute(Machine m) {
		System.out.println("Reg(" + register + ") contains " + m.getRegisters().getRegister(register));
	}

	@Override
	public String toString() {
		return super.toString() + " Reg(" + register + ") value is " + value;
	}
}
