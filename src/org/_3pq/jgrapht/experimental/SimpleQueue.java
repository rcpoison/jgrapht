/*
 * Created on Jul 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org._3pq.jgrapht.experimental;

import java.util.LinkedList;


/**
 * A simple queue structure.
 *
 * @author Barak Naveh
 *
 * @since Jul 19, 2003
 */
public class SimpleQueue {
    private LinkedList m_elementList = new LinkedList(  );

    /**
     * Tests if this queue is empty.
     *
     * @return <code>true</code> if empty, otherwise <code>false</code>.
     */
    public boolean isEmpty(  ) {
        return m_elementList.size(  ) == 0;
    }


    /**
     * Adds the specified object to the tail of the queue.
     *
     * @param o the object to be added.
     */
    public void add( Object o ) {
        m_elementList.addLast( o );
    }


    /**
     * Remove the object at the head of the queue and return it.
     *
     * @return the object and the head of the queue.
     */
    public Object remove(  ) {
        return m_elementList.removeFirst(  );
    }
    
	public void removeVisited(Object o) {
		m_elementList.remove(o);
	}
}