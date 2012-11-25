package com.ninedemons.leaderboard.redis.pipelined;

import com.ninedemons.leaderboard.api.Entry;
import org.testng.annotations.Test;

import java.util.*;

import static junit.framework.Assert.*;

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
        testEntriesAreCorrect(found, underTest.isUseZeroIndexForRank());
    }

    @Test
    public void testTwoUsers() {
        Collection<String> friendsToFind = Arrays.asList(LOWEST_RANKED_USER,HIGHEST_RANKED_USER);
        List<Entry> found = underTest.friends(leaderboardName, friendsToFind);
        assertNotNull("Should always return a List<Entry>", found);
        assertEquals("Should be two results", 2, found.size());
        testEntriesAreCorrect(found, underTest.isUseZeroIndexForRank());
        testEntriesAreOrdered(found);
        testAllEntriesPresent(found,friendsToFind);
    }

    @Test
    public void testThreeUsersOneDoesntExist() {
        Collection<String> friendsToFind = Arrays.asList(LOWEST_RANKED_USER,NO_SUCH_USER,HIGHEST_RANKED_USER);
        List<Entry> found = underTest.friends(leaderboardName, friendsToFind);
        assertNotNull("Should always return a List<Entry>", found);
        assertEquals("Should be two results", 2, found.size());
        testEntriesAreCorrect(found, underTest.isUseZeroIndexForRank());
        testEntriesAreOrdered(found);

        friendsToFind.remove(NO_SUCH_USER);
        testAllEntriesPresent(found,friendsToFind);
    }

    private void testAllEntriesPresent(List<Entry> foundEntries, Collection<String> idsToFind) {

        Set<String> allFoundIds = new HashSet<String>(foundEntries.size());

        for (Entry foundEntry: foundEntries) {
            allFoundIds.add(foundEntry.getUserId());
        }


        for (String idToFind : idsToFind) {
            assertTrue("Should have id " + idToFind + " in the returned list",allFoundIds.contains(idToFind));
            allFoundIds.remove(idToFind);
        }

        assertTrue("ID(s) returned that wasn't in list to find " + allFoundIds,allFoundIds.isEmpty());

    }

}
