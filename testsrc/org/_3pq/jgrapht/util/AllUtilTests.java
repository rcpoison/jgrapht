package org._3pq.jgrapht.util;

import org._3pq.jgrapht.util.equivalence.EquivalenceGroupCreatorTest;
import org._3pq.jgrapht.util.permutation.CompoundPermutationIterTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUtilTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org._3pq.jgrapht.util");
		//$JUnit-BEGIN$
		suite.addTestSuite(PrefetchIteratorTest.class);
	    suite.addTestSuite(CompoundPermutationIterTest.class);
	    suite.addTestSuite(EquivalenceGroupCreatorTest.class);

		//$JUnit-END$
		return suite;
	}

}
