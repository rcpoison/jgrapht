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

public class BinaryHeap extends AbstractHeap {

    private static final HeapFactory FACTORY = new AbstractHeapFactory() {
        public Heap createHeap(Collection c, Comparator comp, boolean maximum) {
            return new BinaryHeap(c, comp, maximum);
        }
    };

    public static final HeapFactory getFactory() {
        return FACTORY;
    }

    private final class Elem implements ElementPeer {

        Object x;
        int    pos;

        Elem(Object e) {
            x = e;
        }

        public void update() {
            percolateUp(pos);
        }

        public Object getObject() {
            return x;
        }

        public String toString() {
            return x.toString();
        }
    }

    private final List _elems;

    public BinaryHeap(Collection c, Comparator comp, boolean maximum) {
        super(comp, maximum);
        _elems = new ArrayList(Math.max(c.size(), 10));
        addAll(c);
    }

    public int size() {
        return _elems.size();
    }

    public void clear() {
        _elems.clear();
    }

    private void percolateUp(int pos) {
        Elem h = (Elem)_elems.get(pos);
        while (pos != 0 &&
               isSmaller(h.x, ((Elem)_elems.get((pos + 1) / 2 - 1)).x)) {
            Elem p = (Elem)_elems.get((pos + 1) / 2 - 1);
            _elems.set(pos, p);
            p.pos = pos;
            pos = (pos + 1) / 2 - 1;
        }
        _elems.set(pos, h);
        h.pos = pos;
    }

    private void percolateDown(int pos) {
        Elem h = (Elem)_elems.get(pos);
        while (2 * pos + 1 < size()) {
            int i = 1;
            Elem c = (Elem)_elems.get(2 * pos + i);
            if (2 * pos + 2 < size() &&
                isSmaller(((Elem)_elems.get(2 * pos + 2)).x, c.x)) {
                i++;
                c = (Elem)_elems.get(2 * pos + i);
            }
            if (isSmaller(h.x, c.x)) break;
            _elems.set(pos, c);
            c.pos = pos;
            pos = 2 * pos + i;
        }
        _elems.set(pos, h);
        h.pos = pos;
    }

    public void add(Object x) {
        Elem e = new Elem(x);
        if (x instanceof HeapElement) ((HeapElement)x).setPeer(e);
        _elems.add(e);
        e.pos = _elems.size() - 1;
        percolateUp(e.pos);
    }

    protected Iterator peerIterator() {
        return _elems.iterator();
    }

    public Object extractTop() {

        Elem z = (Elem)_elems.get(0);
        Elem e = (Elem)_elems.remove(_elems.size() - 1);

        if (!isEmpty()) {
            _elems.set(0, e);
            e.pos = 0;
            percolateDown(0);
        }
        return z.x;
    }

    public String toString() {
        return _elems.toString();
    }
}
