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

public class FibonacciHeap extends AbstractHeap {

    private final class PeerIterator implements Iterator {

        private Elem _pos = _min;
        private final int _mod = _modify;

        public boolean hasNext() {
            if (_mod != _modify) throw new ConcurrentModificationException();
            return _pos != null;
        }

        public Object next() {
            if (!hasNext()) throw new NoSuchElementException();
            Elem result = _pos;
            if (_pos.child != null) {
                _pos = _pos.child;
            } else {
                while (_pos.right.parent != null && _pos.right.parent.child == _pos.right) {
                    _pos = _pos.right.parent;
                }
                if (_pos.right != _min) {
                    _pos = _pos.right;
                } else {
                    _pos = null;
                }
            }
            return result;
        }

        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }

    private static final HeapFactory FACTORY = new AbstractHeapFactory() {
        public Heap createHeap(Collection c, Comparator comp, boolean maximum) {
            return new FibonacciHeap(c, comp, maximum);
        }
    };

    public static final HeapFactory getFactory() {
        return FACTORY;
    }

    private final class Elem implements ElementPeer {

        Object  x;
        Elem    parent = null;
        Elem    child = null;
        Elem    left = this;
        Elem    right = this;
        int     rank = 0;
        boolean marked = false;

        Elem(Object e) {
            x = e;
        }

        void addChild(Elem c) {
            c.parent = this;
            rank++;
            if (child == null) {
                child = c;
            } else {
                c.right = child.right;
                c.right.left = c;
                c.left = child;
                child.right = c;
            }
        }

        void removeChild(Elem c) {
            c.parent = null;
            rank--;
            if (rank == 0) {
                child = null;
                return;
                }
                if (c == child) {
                child = c.left;
            }
            c.left.right = c.right;
            c.right.left = c.left;
            c.right = c;
            c.left = c;
        }

        public void update() {
            if (parent != null) {
                if (isSmaller(x, parent.x)) {
                    _modify++;
                    cut(this);
                }
            } else {
                if (isSmaller(x, _min.x)) {
                    _modify++;
                    _min = this;
                }
            }
        }

        public Object getObject() {
            return x;
        }

        public String toString() {

            Elem c;
            StringBuffer b = new StringBuffer(x.toString()).append(" [");

            if (child != null) {
                c = child;
                do {
                    b.append(c);
                    b.append(", ");
                    c = c.right;
                } while (c != child);
                b.delete(b.length() - 2, b.length());
            }
            b.append("]");
            return b.toString();
        }
    }

    private Elem _min = null;
    private int  _size = 0;
    private int  _modify = 0;

    public FibonacciHeap(Collection c, Comparator comp, boolean maximum) {
        super(comp, maximum);
        addAll(c);
    }

    public int size() {
        return _size;
    }
    
    public void clear() {
        _modify++;
        _min = null;
        _size = 0;
    }

    private void addToRoot(Elem n) {
        if (_min == null) {
            _min = n;
        } else {
            n.right = _min.right;
            n.right.left = n;
            n.left = _min;
            _min.right = n;
            if (isSmaller(n.x, _min.x)) {
                _min = n;
            }
        }
    }

    private void removeFromRoot(Elem n) {
        if (n.left == n) {
            _min = null;
            return;
        }
        if (n == _min) {
            _min = n.left;
        }
        n.right.left = n.left;
        n.left.right = n.right;
        n.right = n;
        n.left = n;
    }

    public void add(Object x) {
        _modify++;
        Elem e = new Elem(x);
        addToRoot(e);
        _size++;
        if (x instanceof HeapElement) ((HeapElement)x).setPeer(e);
    }

    protected Iterator peerIterator() {
        return new PeerIterator();
    }

    public Object extractTop() {
        _modify++;
        Elem z = _min;
        if (z != null) {
            while (z.child != null) {
                Elem x = z.child;
                z.removeChild(x);
                addToRoot(x);
            }
            removeFromRoot(z);
            if (_min != null) {
                consolidate();
            }
            _size--;
        }
        return z.x;
    }

    private void consolidate() {

        int l = (int)Math.round(Math.log(_size) / Math.log(1.5) + 0.5);
        Elem[] a = new Elem[l];
        Elem x, y, h;
        int i;

        do {
            x = _min;
            removeFromRoot(x);
            while (a[x.rank] != null) {
                y = a[x.rank];
                a[x.rank] = null;
                if (isSmaller(y.x, x.x)) {
                    h = x;
                    x = y;
                    y = h;
                }
                x.addChild(y);
                y.marked = false;
            }
            a[x.rank] = x;
        } while (_min != null);
        for (i = 0; i < l; i++) {
            if (a[i] != null) {
                addToRoot(a[i]);
            }
        }
    }

    private void cut(Elem x) {
        if (x.parent != null) {
            Elem y = x.parent;
            y.removeChild(x);
            addToRoot(x);
            if (y.marked) {
                cut(y);
            }
            else {
                y.marked = true;
            }
        }
    }

    public String toString() {
        StringBuffer b = new StringBuffer("[");
        if (_min != null) {
            Elem x = _min;
            do {
                b.append(x);
                b.append(", ");
                x = x.right;
            } while (x != _min);
            b.delete(b.length() - 2, b.length());
        }
        b.append("]");
        return b.toString();
    }
}
