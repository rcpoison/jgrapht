/*
 * Created on Jul 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org._3pq.jgrapht.alg.test;

import org._3pq.jgrapht.alg.SimpleQueue;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SimpleQueueTest extends TestCase {

	public void testIsEmpty() {
		SimpleQueue sq = new SimpleQueue();
		assertTrue(sq.isEmpty());
	}

	public void testAdd() {
		SimpleQueue sq = new SimpleQueue();
		sq.add(new Object());
		assertFalse(sq.isEmpty());
	}

	public void testRemove() {
		SimpleQueue sq = new SimpleQueue();
		Object o = new Object();
		sq.add(o);
		sq.add("string");
		assertEquals(o, sq.remove());
		assertFalse(sq.isEmpty());
		assertEquals("string", sq.remove());
		assertTrue(sq.isEmpty());
	}

}
