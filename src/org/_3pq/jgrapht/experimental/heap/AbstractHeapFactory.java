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

public abstract class AbstractHeapFactory implements HeapFactory {

    public Heap createHeap() {
        return createHeap(Collections.EMPTY_LIST, null, false);
    }

    public Heap createHeap(Comparator comp) {
        return createHeap(Collections.EMPTY_LIST, comp, false);
    }

    public Heap createHeap(Collection c) {
        return createHeap(c, null, false);
    }

    public Heap createHeap(Collection c, Comparator comp) {
        return createHeap(c, comp, false);
    }

    public Heap createHeap(boolean maximum) {
        return createHeap(Collections.EMPTY_LIST, null, maximum);
    }

    public Heap createHeap(Comparator comp, boolean maximum) {
        return createHeap(Collections.EMPTY_LIST, comp, maximum);
    }

    public Heap createHeap(Collection c, boolean maximum) {
        return createHeap(c, null, maximum);
    }

}
