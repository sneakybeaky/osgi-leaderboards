package com.ninedemons.leaderboard.redis.pipelined;

import com.ninedemons.leaderboard.api.Entry;
import junit.framework.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static junit.framework.Assert.*;

/**
 * Created by Jon Barber
 */
public class AroundMeIT extends BaseRedisTest {

    @Test
    public void testNullLeaderboardName() {
        List<Entry> found = underTest.aroundMe(null, MID_RANKED_USER);
        assertNotNull("Should have returned an empty list",found);
        assertEquals("Should have returned an empty list", 0, found.size());

    }

    @Test
    public void testNullUser() {
        List<Entry> found = underTest.aroundMe(leaderboardName, null);
        assertNotNull("Should have returned an empty list",found);
        assertEquals("Should have returned an empty list", 0, found.size());

    }

    @Test
    public void testNoSuchUser() {
        List<Entry> found = underTest.aroundMe(leaderboardName, NO_SUCH_USER);
        assertNotNull("Should have returned an empty list",found);
        assertEquals("Should have returned an empty list", 0, found.size());

    }

    @Test
    public void testNoSuchLeaderboard() {
        List<Entry> found = underTest.aroundMe(NO_SUCH_LEADERBOARD, NO_SUCH_USER);
        assertNotNull("Should have returned an empty list",found);
        assertEquals("Should have returned an empty list", 0, found.size());
    }


    @Test
    public void testWhenPageSizeChanged() {
        int newPageSize = PipelinedLeaderboard.DEFAULT_PAGE_SIZE / 2;
        assertNotSame(newPageSize,PipelinedLeaderboard.DEFAULT_PAGE_SIZE);

        underTest.setPageSize(newPageSize);
        List<Entry> found = underTest.aroundMe(leaderboardName, MID_RANKED_USER);
        Assert.assertNotNull("Should have returned a list of users", found);
        assertEquals("Should have the modified page size of users", newPageSize, found.size());
        testEntriesAreOrdered(found);
        testEntriesAreCorrect(found, underTest.isUseZeroIndexForRank());

    }

    @Test
    public void testAroundMeHappyPath() {
        List<Entry> found = underTest.aroundMe(leaderboardName, MID_RANKED_USER);
        Assert.assertNotNull("Should have returned a list of users", found);
        assertEquals("Should have the full page size of users", underTest.getPageSize(), found.size());

        Entry middleEntry = found.get(found.size() / 2);
        assertEquals("User_50 should be the first entry", middleEntry.getUserId(), MID_RANKED_USER);
        testEntriesAreOrdered(found);
        testEntriesAreCorrect(found, underTest.isUseZeroIndexForRank());
    }

    @Test
    public void testForTopRankedUser() {
        List<Entry> found = underTest.aroundMe(leaderboardName, HIGHEST_RANKED_USER);
        assertEquals("Should have the full page size of users", underTest.getPageSize(), found.size());

        Entry first = found.get(0);
        assertEquals("User_0 should be the first entry", first.getUserId(), HIGHEST_RANKED_USER);
        testEntriesAreOrdered(found);
        testEntriesAreCorrect(found, underTest.isUseZeroIndexForRank());
    }

    @Test
    public void testForBottomRankedUser() {
        List<Entry> found = underTest.aroundMe(leaderboardName, LOWEST_RANKED_USER);
        assertEquals("Should have the full page size of users", underTest.getPageSize(), found.size());

        Entry last = found.get(found.size() - 1);
        assertEquals("User_99 should be the first entry", last.getUserId(), LOWEST_RANKED_USER);
        testEntriesAreOrdered(found);
        testEntriesAreCorrect(found, underTest.isUseZeroIndexForRank());
    }

    @Test
    public void testZeroBasedIndex() {
        underTest.setUseZeroIndexForRank(true);
        assertTrue("Zero index for rank has been enabled", underTest.isUseZeroIndexForRank());
        List<Entry> found = underTest.aroundMe(leaderboardName, HIGHEST_RANKED_USER);
        assertEquals("Should have the full page size of users", underTest.getPageSize(), found.size());

        Entry first = found.get(0);
        assertEquals("First user should have a rank of 0", new Long(0), first.getRank());
        testEntriesAreOrdered(found);
        testEntriesAreCorrect(found, underTest.isUseZeroIndexForRank());
    }

    @Test
    public void testOneBasedIndex() {
        underTest.setUseZeroIndexForRank(false);
        assertFalse("Zero index for rank has been disabled",underTest.isUseZeroIndexForRank());
        List<Entry> found = underTest.aroundMe(leaderboardName, HIGHEST_RANKED_USER);
        assertEquals("Should have the full page size of users", underTest.getPageSize(), found.size());

        Entry first = found.get(0);
        assertEquals("First user should have a rank of 1", new Long(1), first.getRank());
        testEntriesAreOrdered(found);
        testEntriesAreCorrect(found, underTest.isUseZeroIndexForRank());
    }

}
