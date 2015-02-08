/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sml.Machine;
import sml.Translator;

/**
 *
 * @author Langly
 */
public class SmlFileAcceptanceUnitTest {
    
    private String m_filename = "./sample.sml";
    private Translator m_translator;
    private Machine m_machine = new Machine();
    
    public SmlFileAcceptanceUnitTest() {
        m_translator = new Translator(m_filename);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test that the file can be loaded.
     */
     @Test
     public void ValidateFile() 
     {
         m_translator.readAndTranslate(m_machine.getLabels(), m_machine.getProg());
         assertTrue("Translator file loaded.",
                 m_machine.getProg().isEmpty()==false);
     }

     /**
      * Commands being loaded.
      */
     @Test
     public void ValidateCommand() 
     {
         m_translator.readAndTranslate(m_machine.getLabels(), m_machine.getProg());
         assertTrue("File contains valid commands.",
                 m_machine.getProg().get(0).toString().length()>0);
     }
     
     /**
      * Verify register using sample has expected value
      */
     @Test
     public void ValidateValueInRegister0() 
     {
         m_translator.readAndTranslate(m_machine.getLabels(), m_machine.getProg());
         m_machine.execute();
         int index = 0;
         
         assertEquals(m_machine.getRegisters().getRegister(index++),0);
         assertEquals(m_machine.getRegisters().getRegister(index++),1);
         assertEquals(m_machine.getRegisters().getRegister(index++),1);
         assertEquals(m_machine.getRegisters().getRegister(index++),2);
         assertEquals(m_machine.getRegisters().getRegister(index++),3);
         assertEquals(m_machine.getRegisters().getRegister(index++),6);
         assertEquals(m_machine.getRegisters().getRegister(index++),3);
         assertEquals(m_machine.getRegisters().getRegister(index++),0);
         assertEquals(m_machine.getRegisters().getRegister(index++),100);
         assertEquals(m_machine.getRegisters().getRegister(index++),1);
         assertEquals(m_machine.getRegisters().getRegister(index++),1);
         assertEquals(m_machine.getRegisters().getRegister(index++),0);
     }
}
