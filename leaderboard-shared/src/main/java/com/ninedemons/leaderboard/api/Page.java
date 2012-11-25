package com.ninedemons.leaderboard.api;

import java.util.List;

/**
 * Created by Jon Barber
 */
public interface Page {

    /**
     * The page in the leaderboard this data is from
     *
     * @return The page in the leaderboard this data is from
     *
     */
    public int getPageNumber();

    /**
     * The entries for this page, ordered in descending
     * rank
     *
     * @return the entries for this page
     */
    public List<Entry> getEntries();

}
