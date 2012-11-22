package com.ninedemons.leaderboard.redis.pipelined;

import redis.clients.jedis.Response;

/**
 * Created by Jon Barber
 */
public class ScoreAndRankResponse {
    private final Response<Double> score;
    private final Response<Long> rank;

    public ScoreAndRankResponse(Response<Double> score, Response<Long> rank) {
        this.score = score;
        this.rank = rank;
    }

    public Double getScore() {
        return score.get();
    }

    public Long getRank() {
        return rank.get();
    }

    public boolean isResponseValid() {
        return (getScore() != null && getRank() != null);
    }


}
