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
 * CompoundPermutationIter.java
 * -----------------
 * (C) Copyright 2005, by Barak Naveh and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * Changes
 * -------
 */
package org._3pq.jgrapht.util.permutation;

import java.util.Arrays;
import java.util.Iterator;

import org._3pq.jgrapht.util.MathUtil;


/**
 *  for permutation like this:
 * <li>1,2 are the same eq.group (numbers) 
 * <li>a,b	are og the same eq.group (letters)
 * <li>'$' is of its own eq. group (signs)
 * Let the order of the group be (arbitrary): signs,numbers,letters
 * (note that for performence reasons , this arbitrary order is the worst!
 * see Performance section below)<p>
 * These are the possible complex perm:
 * [$,1,2,a,b,c]	<p>				 
 * [$,1,2,a,c,b]	<p>					
 * [$,1,2,b,a,c]	<p>	
 * [$,1,2,b,c,a]	<p>	
 * [$,1,2,c,a,b]	<p>	
 * [$,1,2,c,b,a]	<p>	
 * [$,2,1,a,b,c]	<p>					 
 * [$,2,1,a,c,b]	<p>					
 * [$,2,1,b,a,c]	<p>	
 * [$,2,1,b,c,a]	<p>	
 * [$,2,1,c,a,b]	<p>	
 * [$,2,1,c,b,a]	<p>	
 * The overall number is the multiplaction of each eq. group size fractinonal!, in
 * our example : (1!)x(2!)x(3!)=1x2x6=12
 * 
 * Use the constructor with eq.group sizes and initial order [1,2,3]
 * The result permutations are retrieved as numbers in an array
 * [0,1,2,3,4,5] means [$,1,2,a,b,c]<p>	
 * [0,1,2,3,5,4]	<p>	
 * [0,1,2,4,3,5]	<p>	
 * etc etc , till:	<p>	
 * [0,2,1,5,4,3] means [$,2,1,c,b,a] <p>	
 * 
 * <p><i>Performence:</i>
 * The implementation try to advance each time the group zero , if does not succedd , 
 * tries the next group (1,2 and so on) , so:
 * try to put the largest group as the first groups , UNLIKE the example. Performence wise
 * it is better to do [a,b,c,1,2,$] .The effect is improvement by constant (for example , by 2) 
 *  @author Assaf
 *	@since	May 30, 2005
 *
 */
public class CompoundPermutationIter implements ArrayPermutationsIter,Iterator {
	IntegerPermutationIter[] permArray;
	
	/** on the example 1+2+3=6  	*/
	private int totalPermArraySize ;
	
	/** The overall number is the multiplaction of each eq. group size fractinonal!
	 */
	private int max;
	
	private int iterCounter=0;
	
	/**
	 * for the class example , use [1,2,2]. order matters ! (performence wise too)
	 * @param equalityGroupsSizesArray
	 */
	public CompoundPermutationIter(int[] equalityGroupsSizesArray)
	{
		init(equalityGroupsSizesArray);
		
	}
	/** create a IntegerPermutationIter class per equalityGroup with different
	 * integers.
	 * 
	 * @param equalityGroupsSizesArray
	 */
	private void init(int[] equalityGroupsSizesArray)
	{
				
		this.permArray= new IntegerPermutationIter[equalityGroupsSizesArray.length];
		
		int counter=0;
		this.max=1; 	//	each time , multiply by factorail(eqGroupSize)  
		for (int eqGroup=0;eqGroup<equalityGroupsSizesArray.length;eqGroup++)
		{
			//create an array of eq.group size filled with values
			//of counter, counter+1, ... counter+size-1
			int currGroupSize = equalityGroupsSizesArray[eqGroup];
			int[] currArray = new int[currGroupSize];
			for (int i=0;i<currGroupSize;i++ )
			{
				currArray[i]=counter;
				counter++;
			}
			this.permArray[eqGroup] = new IntegerPermutationIter(currArray);
			this.permArray[eqGroup].getNext(); //first iteration return the source
			//	each time , multiply by factorail(eqGroupSize)
			this.max *= MathUtil.factorial(currGroupSize);
		}
		this. totalPermArraySize = counter;
		
		
		//calc max
	}
	
	public Object next()
	{
		return getNext();
	}
	/** iteration may be one of these two:
	 * 1. the last group advances by one iter , all else stay.
	 * 2. the last group cannot advance , so it restarts but telling the group
	 * on the after it to advance( done recursively till some group can advance)
	 */
	public int[] getNext()
	{
		if (this.iterCounter==0)
		{
			//just return it , without change
			this.iterCounter++;
			return getPermAsArray();
		}
			
		int firstGroupCapableOfAdvancing=-1;
		int currGroupIndex=0; //
		while (firstGroupCapableOfAdvancing==-1)
		{
			IntegerPermutationIter currGroup = this.permArray[currGroupIndex];
		
			if (currGroup.hasNext())
			{
				currGroup.getNext();
				//restart all that we passed on
				for (int i=0;i<currGroupIndex;i++)
				{
					restartPermutationGroup(i);
				}
				firstGroupCapableOfAdvancing = currGroupIndex;
			}
			
			currGroupIndex++;
			if (currGroupIndex>=this.permArray.length)
			{
				break;
			}
		}
		
		this.iterCounter++;
		
		if (firstGroupCapableOfAdvancing==-1)
		{
			//nothing found. we finished all iterations
			return null;
		}
		else
		{
			int[] tempArray=getPermAsArray();
			return tempArray;
		}
		
	}
	/**
	 * creates and returns a new array which is consisted of the eq. group
	 * current permutation arrays.
	 * for example , in the 10th iter ([$,2,1,b,c,a])
	 * The permutations current statuses is [0] [2,1] [4,5,3]
	 * so retrieve [0,2,1,4,5,3]
	 * @return
	 */
	public int[] getPermAsArray() {
		int[] resultArray = new int[this.totalPermArraySize];
		int counter=0;
		for (int groupIndex=0;groupIndex<this.permArray.length;groupIndex++)
		{
			int[] currPermArray = this.permArray[groupIndex].getCurrent();
			System.arraycopy(currPermArray,0,resultArray,counter,currPermArray.length);
			counter+=currPermArray.length;
		}
		return resultArray;
	}
	
	/** restarts by creating a new one instead 
	 * 
	 * @param groupIndex
	 */
	private void restartPermutationGroup(int groupIndex)
	{
		int[] oldPermArray = this.permArray[groupIndex].getCurrent();
		Arrays.sort(oldPermArray);
		this.permArray[groupIndex] = new IntegerPermutationIter(oldPermArray);
		this.permArray[groupIndex].getNext();
		
	}
	
	public boolean hasNext()
	{
		boolean result;
		if (this.iterCounter<this.max)
		{
			result=true;
		}
		else
		{
			result=false;
		}
		return result;
		
	}
	
	public int getMax() {
		return max;
	}
	
	/* (non-Javadoc)
	 * @see org._3pq.jgrapht.util.ArrayPermutationsIter#nextPermutation()
	 */
	public int[] nextPermutation() {
		return (int[]) next();
	}
	/* (non-Javadoc)
	 * @see org._3pq.jgrapht.util.ArrayPermutationsIter#hasNextPermutaions()
	 */
	public boolean hasNextPermutaions() {
		return hasNext();
	}
	/**
	 * UNIMPLEMENTED.
	 * always throws new UnsupportedOperationException
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
		
	}
	
	
}
