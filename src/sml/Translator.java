package sml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 */
public class Translator {

	// word + line is the part of the current line that's not yet processed
	// word has no whitespace
	// If word and line are not empty, line begins with whitespace
	private String line = "";
	private Labels labels; // The labels of the program being translated
	private ArrayList<Instruction> program; // The program to be created
	private String fileName; // source file of SML code

	private static final String SRC = "src";

	public Translator(String fileName) {
		this.fileName = SRC + "/" + fileName;
	}

	// translate the small program in the file into lab (the labels) and
	// prog (the program)
	// return "no errors were detected"
	public boolean readAndTranslate(Labels lab, ArrayList<Instruction> prog) {

		try (Scanner sc = new Scanner(new File(fileName))) {
			// Scanner attached to the file chosen by the user
			labels = lab;
			labels.reset();
			program = prog;
			program.clear();

			try {
				line = sc.nextLine();
			} catch (NoSuchElementException ioE) {
				return false;
			}

			// Each iteration processes line and reads the next line into line
			while (line != null) {
				// Store the label in label
				String label = scan();

				if (label.length() > 0) {
					Instruction ins = getInstruction(label);
					if (ins != null) {
						labels.addLabel(label);
						program.add(ins);
					}
				}

				try {
					line = sc.nextLine();
				} catch (NoSuchElementException ioE) {
					return false;
				}
			}
		} catch (IOException ioE) {
			System.out.println("File: IO error " + ioE.getMessage());
			return false;
		}
		return true;
	}

	// line should consist of an MML instruction, with its label already
	// removed. Translate line into an instruction with label label
	// and return the instruction
	public Instruction getInstruction(String label)
        {
            int s1; // Possible operands of the instruction
            int s2;
            int r;
            int x;
            String fn;
                
            if (line.equals(""))
                return null;

            String ins = scan();
            String command = ConvertToClassName(ins);

            Class c;
            try {
                c = Class.forName(command);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            // if exception thrown then we know the command 
            Constructor constructor = null;
            
            /*
            String     int      int     int     String
            label      r        s1      s2      fn
            return new AddInstruction(label, r, s1, s2);
            return new SubInstruction(label, r, s1, s2);
            return new MulInstruction(label, r, s1, s2);
            return new DivInstruction(label, r, s1, s2);
            return new OutInstruction(label, r);
            return new LinInstruction(label, r, s1);
            return new BnzInstruction(label, r, fn);
            */
            try
            {
                try 
                {
                    // Add||Sub||Multiply||Divide
                    constructor = c.getConstructor(String.class, int.class, int.class, int.class);
                    r = scanInt();
                    s1 = scanInt();
                    s2 = scanInt();
                    Object o = constructor.newInstance(label, r, s1, s2);
                    return (Instruction) o;
                } catch (NoSuchMethodException asmd) {
                    try {
                        // Lin
                        constructor = c.getConstructor(String.class, int.class, int.class);
                        r = scanInt();
                        s1 = scanInt();
                        Object o = constructor.newInstance(label, r, s1);
                        return (Instruction) o;
                    } catch (NoSuchMethodException l) {
                        try {
                            // Bnz
                            constructor = c.getConstructor(String.class, int.class, String.class);
                            r = scanInt();
                            fn = scan();
                            Object o = constructor.newInstance(label, r, fn);
                            return (Instruction) o;
                        } catch (NoSuchMethodException b) {
                            try {
                                //Out
                                constructor = c.getConstructor(String.class, int.class);
                                r = scanInt();
                                Object o = constructor.newInstance(label, r);
                                return (Instruction) o;
                            } catch (NoSuchMethodException ex) {
                                Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
                                // final exception therefore we have to exit!
                                return null;
                            }
                        }
                    }
                }
            } catch(IllegalAccessException|IllegalArgumentException|InstantiationException
                    |InvocationTargetException|SecurityException ex)
            {
                Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return null;
	}

	/*
	 * Return the first word of line and remove it from line. If there is no
	 * word, return ""
	 */
	private String scan() {
		line = line.trim();
		if (line.length() == 0)
			return "";

		int i = 0;
		while (i < line.length() && line.charAt(i) != ' ' && line.charAt(i) != '\t') {
			i = i + 1;
		}
		String word = line.substring(0, i);
		line = line.substring(i);
		return word;
	}

	// Return the first word of line as an integer. If there is
	// any error, return the maximum int
	private int scanInt() {
		String word = scan();
		if (word.length() == 0) {
			return Integer.MAX_VALUE;
		}

		try {
			return Integer.parseInt(word);
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}

    private String ConvertToClassName(String ins) {
        String packageName = "sml";
        String seperator = "."; 
        String instruction = "Instruction";
        
        char[] opArray = ins.toCharArray();
        opArray[0] = Character.toUpperCase(ins.charAt(0));
        return packageName + seperator + new String(opArray) + instruction;
    }
}