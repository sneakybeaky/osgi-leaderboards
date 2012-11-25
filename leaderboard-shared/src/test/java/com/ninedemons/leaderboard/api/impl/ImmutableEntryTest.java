package com.ninedemons.leaderboard.api.impl;

import junit.framework.Assert;
import org.testng.annotations.Test;

/**
 * Created by Jon Barber at AKQA
 */
public class ImmutableEntryTest {

    ImmutableEntry underTest = new ImmutableEntry("underTest",(long)10,(double)100);

    @Test
    public void testCompareToEquivalent() throws Exception {

        ImmutableEntry equivalent = createEquivalent();
        Assert.assertEquals("Objects should be equivalent",0,underTest.compareTo(equivalent));
    }

    private ImmutableEntry createEquivalent() {
        return new ImmutableEntry("equivalent",underTest.getRank(),underTest.getScore());
    }

    @Test
    public void testCompareOtherRankLower() throws Exception {

        ImmutableEntry other = new ImmutableEntry("other",underTest.getRank()+1,underTest.getScore());

        Assert.assertTrue("I should be higher",underTest.compareTo(other) > 0);
    }

    @Test
    public void testCompareOtherRankHigher() throws Exception {

        ImmutableEntry other = new ImmutableEntry("other",underTest.getRank()-1,underTest.getScore());

        Assert.assertTrue("I should be lower",underTest.compareTo(other) < 0);
    }

    @Test
    public void testCompareOtherScoreLower() throws Exception {

        ImmutableEntry other = new ImmutableEntry("other",underTest.getRank(),underTest.getScore()-1);

        Assert.assertTrue("I should be higher",underTest.compareTo(other) > 0);
    }

    @Test
    public void testCompareOtherScoreHigher() throws Exception {

        ImmutableEntry other = new ImmutableEntry("other",underTest.getRank(),underTest.getScore()+1);

        Assert.assertTrue("I should be lower",underTest.compareTo(other) < 0);
    }
}
