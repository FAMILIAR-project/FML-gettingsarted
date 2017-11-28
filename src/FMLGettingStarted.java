import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import fr.familiar.FMLTest;
import fr.familiar.parser.FMBuilder;
import fr.familiar.variable.Comparison;
import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.SetVariable;
import fr.familiar.variable.Variable;

/**
 *  
 * Created by macher1 on 28/11/2017.
 */
public class FMLGettingStarted extends FMLTest {   
	
	@Test
    public void testHelloWorld() throws Exception {
        FeatureModelVariable fmv = FM ("fm1", "FM (A : [B] [C] ;)"); // B and C are optional features of A (root)        
        assertEquals(4.0, fmv.counting(), 0.0);

        // alternate way to build a feature model
        FeatureModelVariable fmv2 = new FeatureModelVariable ("fm2", FMBuilder.getInternalFM("A : [B] [C] ;"));
        assertEquals(Comparison.REFACTORING, fmv2.compare(fmv));
        
        // void or unsatisfiable feature model
        FeatureModelVariable fmv3 = FM ("fm1", "FM (A : B C ; B -> !C; )");   // B and C are mandatory features and B -> !C introduces a logical contradiction     
        assertEquals(0.0, fmv3.counting(), 0.0);
        assertFalse(fmv3.isValid());
        
        // false optional, dead, core, all configs
        FeatureModelVariable fmv4 = FM ("fm1", "FM (A : B [C] [D] ; B -> !C; B -> D; )"); // C is a dead feature and D is a core feature      
        assertEquals(1.0, fmv4.counting(), 0.0);
        assertTrue(fmv4.isValid());
        assertEquals(1, fmv4.falseOptionalFeatures().size());
        assertEquals(3, fmv4.cores().size());
        assertEquals(1, fmv4.deads().size());
                 
        Set<Variable> allConfigs = fmv.configs();	 
        for (Variable cf : allConfigs) {
            Set<String> confFts = ((SetVariable) cf).names();
            FeatureConfiguration ftConf = new FeatureConfiguration(confFts, fmv);	  
            System.err.println("" + confFts + " " + ftConf.getConfMap());
        }
        
        // slice, merge, etc       
        
    }
    
}