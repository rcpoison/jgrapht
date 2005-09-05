/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2005, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -----------------
 * PrefetchIterator.java
 * -----------------
 * (C) Copyright 2005, by Barak Naveh and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * Changes
 * -------
 */
package org._3pq.jgrapht.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Utility class to help implement an iterator/enumerator in which the
 * hasNext() method needs to calculate the next elements. <p>
 * Many classes which implement an iterator face a common problem:
 * if there is no easy way to calculate hasNext() other than to call getNext() ,
 * then they save the result for fetching in the next call to getNext().
 * this utility helps doing just that.
 * <p><b>Usage:</b>
 * The new iterator class will hold this class as a member variable and forward the
 * hasNext() and next() to it. When creating instanse of this class, you supply it with 
 * a functor that is doing the real job of calculating the next element.
 * 
 * <pre><code>
 *	//This class supllies enumeration of integer till 100. 
 *	public class iteratorExample implements Enumeration{
 *	private int counter=0;
 *	private PrefetchIterator nextSupplier;
 *		
 *		iteratorExample()
 *		{
 *			nextSupplier = new PrefetchIterator(new PrefetchIterator.NextElementFunctor(){
 *				
 *				public Object nextElement() throws NoSuchElementException {
 *					counter++;
 *					if (counter>=100)
 *						return new NoSuchElementException();
 *					else
 *						return new Integer(counter);
 *				}
 *			
 *			});
 *		}
 *		//forwarding to nextSupplier and return its returned value
 *		public boolean hasMoreElements() {
 *			return this.nextSupplier.hasMoreElements();
 *		}
 *	//	forwarding to nextSupplier and return its returned value
 *		public Object nextElement() {
 *			return this.nextSupplier.nextElement();
 *		}
 *}</pre>
</code>

 * @author Assaf_Lehr
 */
public class PrefetchIterator implements Iterator,Enumeration
{ 
	
	public interface NextElementFunctor
	{
		/** You must implement that NoSuchElementException is thrown
		 * on nextElement() if it is out of bound.
		 */
		public Object nextElement() throws NoSuchElementException;
	}
	
	private NextElementFunctor innerEnum;
	private Object getNextLastResult;
	private boolean isGetNextLastResultUpToDate=false;
	private boolean endOfEnumerationReached=false;
	private boolean flagIsEnumerationStartedEmpty=true; 
	private int innerFunctorUsageCounter=0;
	
	
	
	public PrefetchIterator(NextElementFunctor aEnum)
	{
		innerEnum = aEnum;
	}
	
	/**
	 * serves as one contact place to the functor
	 * all must use it and not directly the NextElementFunctor 
	 */
	private Object getNextElementFromInnerFunctor()
	{
		innerFunctorUsageCounter++;
		Object result = this.innerEnum.nextElement();
		//if we got here , an exception was not thrown, so at least
		//one time a good value returned
		flagIsEnumerationStartedEmpty=false;
		return result;
	}
	/** 
	 * 1. retrieve the saved value or calculate it if it does not exist
	 * 2. change isGetNextLastResultUpToDate to false (cause the it does not
	 * save the NEXT element now . it saves the current one!
	 * 
	 */
	public Object nextElement()
	{
		Object result=null;
		if (this.isGetNextLastResultUpToDate)
		{
			result=this.getNextLastResult;
		}
		else
		{
			result = getNextElementFromInnerFunctor();
		}
	
		this.isGetNextLastResultUpToDate = false;
		return result;
	}
	/**
	 * if (isGetNextLastResultUpToDate==true)
	 * returns true
	 * else
	 * 	1. calculate getNext() and save it
	 *  2. set isGetNextLastResultUpToDate to true
	 */
	public boolean hasMoreElements()
	{
		if (endOfEnumerationReached)
		{
			return false;
		}
		
		if (isGetNextLastResultUpToDate)
		{
			return true;
		}
		else
		{
			try
			{
				this.getNextLastResult = getNextElementFromInnerFunctor();
				this.isGetNextLastResultUpToDate = true;
				return true;
			}
			catch ( NoSuchElementException noSuchE )
			{
				endOfEnumerationReached = true;
				return false;
			}
		}//else
	}//method
	
	/**
	 * did the enumeration started as empty one ? .
	 * It does not matter if it hasMoreElements() now , only on initialization time.
	 * Effecincy: if nextElements() , hasMoreElements() were never use , it
	 * activate the hasMoreElements() once . Else it is immidiate(O(1))
	 *
	 */
	public boolean isEnumerationStartedEmpty()
	{
		if (this.innerFunctorUsageCounter==0)
		{
			if (hasMoreElements())
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else 	//it is not the first time , so use the saved value
				//which was initilaizeed during a call to 
				//getNextElementFromInnerFunctor
		{
			return flagIsEnumerationStartedEmpty;
		}
	}

	public boolean hasNext() {
		return this.hasMoreElements();
	}

	public Object next() {
		return this.hasNext();
	}
	/** always throws UnSupportedOperationException */
	public void remove() throws UnsupportedOperationException 
	{
		throw new UnsupportedOperationException();
		
	}
	
	
		
}
