/*
 * Created on Jul 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org._3pq.jgrapht.alg;

import org._3pq.jgrapht.alg.AlgUtils.SimpleStack;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SimpleStackTest extends TestCase {

	public void testIsEmpty() {
		SimpleStack ss = new SimpleStack();
		assertTrue(ss.isEmpty());
	}

	public void testAdd() {
		SimpleStack ss = new SimpleStack();
		ss.add(new Object());
		assertFalse(ss.isEmpty());
	}

	public void testRemove() {
		SimpleStack ss = new SimpleStack();
		Object o = new Object();
		ss.add(o);
		ss.add("string");
		assertEquals("string", ss.remove());
		assertFalse(ss.isEmpty());
		assertEquals(o, ss.remove());
		assertTrue(ss.isEmpty());
	}

}
