package com.ninedemons.playthings.leaderboard.api;

/**
 * Represents an entry in a leaderboard
 */
public interface Entry {

    String getUserId();

    Long getRank();

    Double getScore();

}
