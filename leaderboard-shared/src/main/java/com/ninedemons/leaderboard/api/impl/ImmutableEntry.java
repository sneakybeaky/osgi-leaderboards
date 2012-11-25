package com.ninedemons.leaderboard.api.impl;

import com.ninedemons.leaderboard.api.Entry;

/**
 * Created by Jon Barber
 */
public class ImmutableEntry implements Entry {

    private String userId;
    private Long rank;
    private Double score;

    public ImmutableEntry(String userId, Long rank, Double score) {
        this.userId = userId;
        this.rank = rank;
        this.score = score;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public Long getRank() {
        return rank;
    }

    @Override
    public Double getScore() {
        return score;
    }


    @Override
    public int compareTo(Entry other) {

        int rankComparison = (int) (other.getRank() - getRank());

        if (rankComparison == 0) {
            return (int) (getScore() - other.getScore());
        } else {
            return rankComparison;
        }

    }
}