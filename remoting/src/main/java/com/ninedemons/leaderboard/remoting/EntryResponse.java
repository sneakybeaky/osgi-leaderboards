package com.ninedemons.leaderboard.remoting;

/**
 * Created by Jon Barber
 */
public class EntryResponse {


    private String userId;

    private Double score;

    private Long rank;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }
}
