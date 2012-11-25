package com.ninedemons.leaderboard.api;


import java.util.Collection;
import java.util.List;

public interface Leaderboard {

    /**
     * For a given id return a list of entries centred around the id
     *
     * @param leaderboardName  The leaderboard to return results from
     * @param userId  The id to centre the list around
     * @return the list of entries surrounding the id
     */

    List<Entry> aroundMe(String leaderboardName,String userId);

    /**
     * Returns a list of entries for the given list of ids. If an id supplied doesn't exist then no corresponding
     * entry will be in the return value.
     *
     * @param leaderboardName The leaderboard to return results from
     * @param userIds the list of ids to return results for
     * @return a list of entries in descending order of rank - the first element will be the highest ranked.
     */
    List<Entry> friends(String leaderboardName,Collection<String> userIds);

    /**
     * Returns a page of entries from the leaderboard.
     *
     * The lower the page number the higher the rank of the entries returned. Page 1 is always the first page in the
     * leader board - - this is the page of highest entries in the leader board.
     *
     * If the page number is too low, e.g. -1, then page 1 will be returned.
     *
     * If the page is too high then the last page in the leader board will be returned - this will be the page of
     * lowest entries in the leader board.
     *
     * @param leaderboardName the leader board to return results from
     * @param pageNumber the page to return
     * @return a page of data.
     */
    Page page(String leaderboardName,int pageNumber);

}
