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
        List<Entry> found = underTest.friends(leaderboardName, Arrays.asList(MID_RANKED_USER));
        assertNotNull("Should always return a List<Entry>", found);
        assertEquals("Should be one result", 1, found.size());
        assertEquals("Wrong user in result",MID_RANKED_USER,found.get(0).getUserId());
    }
}