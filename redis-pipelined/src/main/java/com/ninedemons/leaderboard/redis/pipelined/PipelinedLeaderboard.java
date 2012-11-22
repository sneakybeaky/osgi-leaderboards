package com.ninedemons.leaderboard.redis.pipelined;

import com.ninedemons.leaderboard.api.Entry;
import com.ninedemons.leaderboard.api.Leaderboard;
import com.ninedemons.leaderboard.api.impl.ImmutableEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.*;

/**
 * Created by Jon Barber
 */
public class PipelinedLeaderboard implements Leaderboard {

    private final static List<Entry> EMPTY_RESULT = Collections.emptyList();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JedisPool jedisPool;

    public static final int DEFAULT_PAGE_SIZE = 20;

    private int pageSize = DEFAULT_PAGE_SIZE;

    private boolean useZeroIndexForRank = Boolean.TRUE;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isUseZeroIndexForRank() {
        return useZeroIndexForRank;
    }

    public void setUseZeroIndexForRank(boolean useZeroIndexForRank) {
        this.useZeroIndexForRank = useZeroIndexForRank;
    }

    @Override
    public List<Entry> aroundMe(String leaderboardName, String userId) {

        Jedis jedis = jedisPool.getResource();

        try {

            Long reverseRankForMember = jedis.zrevrank(leaderboardName, userId);

            if (reverseRankForMember == null) {
                logger.debug("No such user {} or leaderboard {}", userId, leaderboardName);
                return EMPTY_RESULT;
            }


            int startingOffset = (int) reverseRankForMember.longValue() - (pageSize / 2);
            long usersInLeaderboard = jedis.zcard(leaderboardName);

            if (reverseRankForMember > (usersInLeaderboard - pageSize)) {
                startingOffset = (int) usersInLeaderboard - pageSize;
            }

            if (startingOffset < 0) {
                startingOffset = 0;
            }

            int endingOffset = (startingOffset + pageSize) - 1;

            Pipeline pipeline = jedis.pipelined();
            Response<Set<Tuple>> rawLeaderDataResponse = pipeline.zrevrangeWithScores(leaderboardName, startingOffset, endingOffset);
            pipeline.sync();

            return responseToEntries(pipeline, leaderboardName, rawLeaderDataResponse);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public List<Entry> friends(String leaderboardName, Collection<String> userIds) {

        Map<String, ScoreAndRankResponse> responses = getRankAndScoresFor(leaderboardName, userIds);
        List<Entry> friends = getEntriesFor(responses);
        Collections.sort(friends,Collections.reverseOrder());
        return friends;

    }

    private List<Entry> getEntriesFor(Map<String, ScoreAndRankResponse> responses) {
        List<Entry> result = new ArrayList<Entry>(responses.size());

        for (Map.Entry<String, ScoreAndRankResponse> response : responses.entrySet()) {
            ScoreAndRankResponse scoreAndRank = response.getValue();

            if (scoreAndRank.isResponseValid()) {
                result.add(new ImmutableEntry(response.getKey(), scoreAndRank.getRank(), scoreAndRank.getScore()));
            }
        }
        return result;
    }

    private Map<String, ScoreAndRankResponse> getRankAndScoresFor(String leaderboardName, Collection<String> userIds) {
        Map<String, ScoreAndRankResponse> responses;Jedis jedis = jedisPool.getResource();

        try {

            responses = new HashMap<String, ScoreAndRankResponse>(userIds.size());
            Pipeline pipeline = jedis.pipelined();
            for (String userid : userIds) {
                responses.put(userid, new ScoreAndRankResponse(pipeline.zscore(leaderboardName, userid),
                        pipeline.zrevrank(leaderboardName, userid)));
            }
            pipeline.sync();

        } finally {
            jedisPool.returnResource(jedis);
        }
        return responses;
    }

    @Override
    public List<Entry> page(String leaderboardName, int page) {
        return null;
    }

    private List<Entry> responseToEntries(Pipeline pipeline, String leaderboardName, Response<Set<Tuple>> rawLeaderDataResponse) {

        Set<Tuple> memberData = rawLeaderDataResponse.get();
        Tuple[] members = memberData.toArray(new Tuple[memberData.size()]);
        List<Response<Long>> responseList = new ArrayList<Response<Long>>(members.length);

        for (Tuple member : members) {
            responseList.add(pipeline.zrevrank(leaderboardName, member.getElement()));
        }

        pipeline.sync();

        List<Entry> leaderData = new ArrayList<Entry>(members.length);

        for (int i = 0; i < members.length; i++) {
            Tuple member = members[i];
            Long rank = responseList.get(i).get();

            if (rank != null && !useZeroIndexForRank) {
                rank++;
            }

            ImmutableEntry entry = new ImmutableEntry(member.getElement(), rank, member.getScore());
            leaderData.add(entry);
        }

        return leaderData;
    }


    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


}
