package org._3pq.jgrapht.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

public class PrefetchIteratorTest extends TestCase {

	//	This test class supllies enumeration of integer from 1 till 100. 
	 public static class IterateFrom1To99 implements Enumeration,Iterator{
		    private int counter=0;
		    private PrefetchIterator nextSupplier;
		        
		    public IterateFrom1To99()
		        {
		            nextSupplier = new PrefetchIterator(new PrefetchIterator.NextElementFunctor(){
		                
		                public Object nextElement() throws NoSuchElementException {
		                    counter++;
		                    if (counter>=100)
		                        throw new NoSuchElementException();
		                    else
		                        return new Integer(counter);
		                }
		            
		            });
		        }
		        //forwarding to nextSupplier and return its returned value
		        public boolean hasMoreElements() {
		            return this.nextSupplier.hasMoreElements();
		        }
		    //  forwarding to nextSupplier and return its returned value
		        public Object nextElement() {
		            return this.nextSupplier.nextElement();
		        }
		        
		        public Object next() {
		        	return this.nextSupplier.next();
		    	}
		        public boolean hasNext() {
		        	return this.nextSupplier.hasNext
		        	();
		    	}
		    	public void remove() {
		    		this.nextSupplier.remove();
		    		
		    	}
	 }
		        
	
	
	public void testIteratorInterface() {
		Iterator iterator = new IterateFrom1To99();
		for (int i=1 ; i< 100 ; i++)
		{
			assertEquals(true,iterator.hasNext());
			assertEquals(i,iterator.next());
		}
		assertEquals(false,iterator.hasNext());
		Exception exceptionThrown = null;
		try
		{
			iterator.next();
		}
		catch (Exception e) {
			exceptionThrown = e;
		}
		assertTrue(exceptionThrown instanceof NoSuchElementException);
		
	}

	public void testEnumInterface() {
		Enumeration enumuration = new IterateFrom1To99();
		for (int i=1 ; i< 100 ; i++)
		{
			assertEquals(true,enumuration.hasMoreElements());
			assertEquals(i,enumuration.nextElement());
		}
		assertEquals(false,enumuration.hasMoreElements());
		Exception exceptionThrown = null;
		try
		{
			enumuration.nextElement();
		}
		catch (Exception e) {
			exceptionThrown = e;
		}
		assertTrue(exceptionThrown instanceof NoSuchElementException);

	}
	
	

}
