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

public class HeapVertex implements HeapElement, Comparable {

    private ElementPeer _p;
    private double _priority;
    private Object _additional;
    private final Object _vertex;

    public HeapVertex() {
        _vertex = this;
    }

    public HeapVertex(Object vertex) {
        _vertex = vertex;
    }

    public void setPeer(ElementPeer peer) {
        _p = peer;
    }

    public ElementPeer getPeer() {
        return _p;
    }

    public void setPriority(double prio) {
        _priority = prio;
    }

    public double getPriority() {
        return _priority;
    }

    public void setAdditional(Object add) {
        _additional = add;
    }

    public Object getAdditional() {
        return _additional;
    }

    public Object getVertex() {
        return _vertex;
    }

    public int compareTo(Object o) {
        HeapVertex other = (HeapVertex)o;
        if (other._priority < _priority) return 1;
        if (other._priority > _priority) return -1;
        return 0;
    }

    public String toString() {
        if (_vertex != this) return _vertex.toString()+" ("+_priority+")";
        return super.toString()+" ("+_priority+")";
    }
}
