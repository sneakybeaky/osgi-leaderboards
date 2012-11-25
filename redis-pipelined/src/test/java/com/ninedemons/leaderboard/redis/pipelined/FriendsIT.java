package com.ninedemons.leaderboard.redis.pipelined;

import com.ninedemons.leaderboard.api.Entry;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Jon Barber
 */
public class FriendsIT extends BaseRedisTest {

    @Test
    public void testNullLeaderboard() {
        List<Entry> found = underTest.friends(null, Collections.<String>emptySet());
        assertNotNull("Should always return a List<Entry>", found);
        assertEquals("Should return an empty List<Entry>", 0, found.size());
    }

    @Test
    public void testNullFriendsList() {
        List<Entry> found = underTest.friends(leaderboardName, null);
        assertNotNull("Should always return a List<Entry>", found);
        assertEquals("Should return an empty List<Entry>", 0, found.size());
    }

    @Test
    public void testNoSuchLeaderboard() {
        List<Entry> found = underTest.friends(NO_SUCH_LEADERBOARD, Collections.<String>emptySet());
        assertNotNull("Should always return a List<Entry>", found);
        assertEquals("Should return an empty List<Entry>", 0, found.size());
    }

    @Test
    public void testEmptyFriendsList() {
        List<Entry> found = underTest.friends(leaderboardName, Collections.<String>emptySet());
        assertNotNull("Should always return a List<Entry>", found);
        assertEquals("Should return an empty List<Entry>", 0, found.size());
    }

    @Test
    public void testNoSuchUsers() {
        List<Entry> found = underTest.friends(leaderboardName, Arrays.asList(NO_SUCH_USER));
        assertNotNull("Should always return a List<Entry>", found);
        assertEquals("Should return an empty List<Entry>", 0, found.size());
    }

    @Test
    public void testOneUser() {
        List<Entry> found = underTest.friends(leaderboardName, Arrays.asList(HIGHEST_RANKED_USER));
        assertNotNull("Should always return a List<Entry>", found);
        assertEquals("Should be one result", 1, found.size());
        assertEquals("Wrong user in result",HIGHEST_RANKED_USER,found.get(0).getUserId());
        assertEquals("Wrong user in result",HIGHEST_RANKED_USER,found.get(0).getUserId());
        testEntriesAreCorrect(found, underTest.isUseZeroIndexForRank());
    }

    @Test
    public void testTwoUsers() {
        List<Entry> found = underTest.friends(leaderboardName, Arrays.asList(LOWEST_RANKED_USER,HIGHEST_RANKED_USER));
        assertNotNull("Should always return a List<Entry>", found);
        assertEquals("Should be two results", 2, found.size());
        testEntriesAreCorrect(found, underTest.isUseZeroIndexForRank());
        testEntriesAreOrdered(found);
    }

}
