/*
    Copyright (C) 2003 Michael Behrisch

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org._3pq.jgrapht.experimental.heap;

import java.util.*;

public abstract class AbstractHeap implements Heap {

    private final Comparator _comp;
    private final int        _compareFactor;

    public AbstractHeap(Comparator comp, boolean maximum) {
        _comp = comp;
        _compareFactor = maximum ? -1 : 1;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public abstract int size();

    public abstract void add(Object x);

    public void addAll(Collection c) {
        Iterator it = c.iterator();
        while (it.hasNext()) {
            add(it.next());
        }
    }

    protected abstract Iterator peerIterator();

    public void update(Object x) {
        if (x instanceof HeapElement) {
            ((HeapElement)x).getPeer().update();
        } else {
            Iterator it = peerIterator();
            while (it.hasNext()) {
                ElementPeer peer = (ElementPeer)it.next();
                if (peer.getObject() == x) {
                    peer.update();
                }
            }
        }
    }

    public abstract Object extractTop();

    protected final boolean isSmaller(Object x, Object y) {
        if (_comp != null) return _comp.compare(x, y) * _compareFactor < 0;
        return ((Comparable)x).compareTo(y) * _compareFactor < 0;
    }
}
