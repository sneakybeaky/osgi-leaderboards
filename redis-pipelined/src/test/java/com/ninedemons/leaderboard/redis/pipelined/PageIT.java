package com.ninedemons.leaderboard.redis.pipelined;

import com.ninedemons.leaderboard.api.Entry;
import com.ninedemons.leaderboard.api.Page;
import junit.framework.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by Jon Barber
 */
public class PageIT extends BaseRedisTest {

    @Test
    public void testNoSuchLeaderboard() {
        Page found = underTest.page(NO_SUCH_LEADERBOARD,1);
        assertNotNull("Should always return a Page", found);
        assertNotNull("Should have a 0 length list of entries", found.getEntries());
        assertEquals("Should have a 0 length list of entries", 0, found.getEntries().size());
    }

    @Test
    public void testNullLeaderboardName() {
        Page found = underTest.page(null,0);
        assertNotNull("Should always return a Page", found);
        assertEquals("Should be the same page number as supplied in the call", 0, found.getPageNumber());
        assertNotNull("Should have a 0 length list of entries", found.getEntries());
        assertEquals("Should have a 0 length list of entries", 0, found.getEntries().size());
    }

    @Test
    public void testNegativePageNumberSupplied() {
        Page found = underTest.page(leaderboardName,-1);
        assertNotNull("Should always return a Page", found);
        assertEquals("Should be page 1", 1, found.getPageNumber());
        assertNotNull("Should have a list of entries", found.getEntries());
        assertEquals("Should have a full list of entries", underTest.getPageSize(), found.getEntries().size());
        assertTrue("First entry should be highest ranked user",found.getEntries().get(0).getUserId().equals(HIGHEST_RANKED_USER));
        testEntriesAreCorrect(found.getEntries(), underTest.isUseZeroIndexForRank());
    }

    @Test
    public void testFirstPage() {
        Page found = underTest.page(leaderboardName,1);
        assertNotNull("Should always return a Page", found);
        assertEquals("Should be page 1", 1, found.getPageNumber());
        assertNotNull("Should have a list of entries", found.getEntries());
        assertEquals("Should have a full list of entries", underTest.getPageSize(), found.getEntries().size());
        assertTrue("First entry should be highest ranked user",found.getEntries().get(0).getUserId().equals(HIGHEST_RANKED_USER));
        testEntriesAreCorrect(found.getEntries(), underTest.isUseZeroIndexForRank());
    }

    @Test
    public void testLargestPageNumberSupplied() {
        Page found = underTest.page(leaderboardName,Integer.MAX_VALUE);
        assertNotNull("Should always return a Page", found);

        int expectedPageNumber = NUMBER_OF_TEST_USERS / underTest.getPageSize();

        assertEquals("Should be last page", expectedPageNumber, found.getPageNumber());
        assertNotNull("Should have a list of entries", found.getEntries());
        assertEquals("Should have a full list of entries", underTest.getPageSize(), found.getEntries().size());
        assertTrue("Last entry should be highest ranked user",found.getEntries().
                get(underTest.getPageSize() - 1).getUserId().equals(LOWEST_RANKED_USER));
        testEntriesAreCorrect(found.getEntries(), underTest.isUseZeroIndexForRank());
    }

    @Test
    public void pageThroughTotalLeaderboard() {
        List<Entry> allFoundEntries = new ArrayList<Entry>(NUMBER_OF_TEST_USERS);

        int numberOfPagesInLeaderboard = NUMBER_OF_TEST_USERS / underTest.getPageSize();

        for (int pageNumber = 1; pageNumber <= numberOfPagesInLeaderboard; pageNumber++) {
            Page found = underTest.page(leaderboardName,pageNumber);
            assertEquals("Incorrect page number in result", pageNumber, found.getPageNumber());
            assertEquals("Should have a full list of entries", underTest.getPageSize(), found.getEntries().size());
            testEntriesAreCorrect(found.getEntries(), underTest.isUseZeroIndexForRank());
            allFoundEntries.addAll(found.getEntries());
        }

        Assert.assertEquals("Should have found all the test users",NUMBER_OF_TEST_USERS,allFoundEntries.size());

    }

    @Test
    public void testPageSizeSetToTen() {
        underTest.setPageSize(10);
        Page found = underTest.page(leaderboardName,1);
        assertNotNull("Should always return a Page", found);
        assertEquals("Should be page 1", 1, found.getPageNumber());
        assertNotNull("Should have a list of entries", found.getEntries());
        assertEquals("Should have a full list of entries", 10, found.getEntries().size());
        assertTrue("First entry should be highest ranked user",found.getEntries().get(0).getUserId().equals(HIGHEST_RANKED_USER));
        testEntriesAreCorrect(found.getEntries(), underTest.isUseZeroIndexForRank());
    }

    @Test
    public void testPageSizeSetToFifty() {
        underTest.setPageSize(50);
        Page found = underTest.page(leaderboardName,1);
        assertNotNull("Should always return a Page", found);
        assertEquals("Should be page 1", 1, found.getPageNumber());
        assertNotNull("Should have a list of entries", found.getEntries());
        assertEquals("Should have a full list of entries", 50, found.getEntries().size());
        assertTrue("First entry should be highest ranked user",found.getEntries().get(0).getUserId().equals(HIGHEST_RANKED_USER));
        testEntriesAreCorrect(found.getEntries(), underTest.isUseZeroIndexForRank());
    }

}
