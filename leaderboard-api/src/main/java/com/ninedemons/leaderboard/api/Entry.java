package com.ninedemons.leaderboard.api;

/**
 * Represents an entry in a leaderboard
 */
public interface Entry {

    String getUserId();

    Long getRank();

    Double getScore();

}
