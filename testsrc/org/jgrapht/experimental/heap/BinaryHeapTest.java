package org.jgrapht.experimental.heap;

import junit.framework.TestCase;

//=========================================================================
/**
 * Very thin test of a binary heap.
 * 
 * I basically did this to observe in the debugger how the binary tree
 * is working in order to get the generics right (until now I failed :-).
 * 
 * @author Hartmut
 */
public class BinaryHeapTest extends TestCase {

    BinaryHeap heap;
    
    //~ Methods ---------------------------------------------------------------

    protected void setUp() throws Exception {
        super.setUp();
        
    }

    /**
     * .
     */
    public void testAdd() {
        heap = new BinaryHeap(null,null,true);
        assertEquals(true, heap.isEmpty());

        for (int i=0; i<10; i++) {
            heap.add( i );
        }
        
        for (int i=9; i>=0; i--) {
            int top = (Integer)heap.extractTop();
            assertEquals(i,top);
        }
    }

}
