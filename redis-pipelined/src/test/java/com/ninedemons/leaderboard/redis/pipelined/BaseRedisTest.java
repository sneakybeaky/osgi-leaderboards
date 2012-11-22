package com.ninedemons.leaderboard.redis.pipelined;

import com.ninedemons.leaderboard.api.Entry;
import org.testng.annotations.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Jon Barber at AKQA
 */
public abstract class BaseRedisTest {

    public static final String NO_SUCH_LEADERBOARD = "no_such_leaderboard";
    public static final String NO_SUCH_USER = "no_such_user";
    public static final int NUMBER_OF_TEST_USERS = 100;
    public static final int MAXIMUM_SCORE = NUMBER_OF_TEST_USERS * 10;
    protected static String HIGHEST_RANKED_USER;
    protected static String LOWEST_RANKED_USER;
    protected static String MID_RANKED_USER;

    JedisPool pool;
    String leaderboardName;
    PipelinedLeaderboard underTest;

    Map<String,Double> userAndScore;

    @Parameters({"redis.host", "redis.port"})
    @BeforeSuite(alwaysRun = true)
    public void setupRedis(@Optional("localhost") String hostname, @Optional("6379") int port) {
        pool = new JedisPool(hostname, port);
        checkRedisReachable();
    }

    @BeforeTest
    public void beforeTest() {
        underTest = new PipelinedLeaderboard();
        underTest.setJedisPool(pool);
        underTest.setPageSize(PipelinedLeaderboard.DEFAULT_PAGE_SIZE);
        underTest.setUseZeroIndexForRank(true);
        populateTestData();
    }

    @AfterTest
    public void afterTest() {
        rollbackRedisChanges();
    }

    public void testResultsAreOrdered(List<Entry> results) {

        if (results.size() == 0) {
            return;
        }

        Entry previousEntry = null;
        for (Entry entry : results) {

            if (previousEntry != null) {

                assertTrue("Results should be ordered in descending order of score", !(entry.getScore().compareTo(previousEntry.getScore()) > 0));
                assertTrue("Results should be ordered in ascending order of rank", entry.getRank().compareTo(previousEntry.getRank()) > 0);
            }
            previousEntry = entry;
        }

    }

    private void rollbackRedisChanges() {
        Jedis jedis = pool.getResource();
        jedis.del(leaderboardName);
        pool.returnResource(jedis);
    }

    private void populateTestData() {

        userAndScore = new HashMap<String, Double>(NUMBER_OF_TEST_USERS);

        Jedis jedis = pool.getResource();
        leaderboardName = "test_leaderboard_" + Long.toString(jedis.incr("test_loaderboard_name"));

        for (int userNumber = 0; userNumber < NUMBER_OF_TEST_USERS; userNumber++) {
            int score = MAXIMUM_SCORE - userNumber;
            String memberName = "user_" + userNumber;
            jedis.zadd(leaderboardName, score, memberName);
            userAndScore.put(memberName, (double) score);
        }
        HIGHEST_RANKED_USER = "user_0";
        MID_RANKED_USER = "user_50";
        LOWEST_RANKED_USER = "user_99";

        pool.returnResource(jedis);
    }

    public void checkEntryIsCorrect(Entry entry,boolean zeroBasedIndex) {
        Double expectedScore = userAndScore.get(entry.getUserId());
        assertEquals("Score is not correct", entry.getScore(), expectedScore);
        Long expectedRank = new Long(MAXIMUM_SCORE - expectedScore.intValue());

        if (!zeroBasedIndex) {
            expectedRank++;
        }
        assertEquals("Rank is not correct",expectedRank,entry.getRank());

    }

    public void checkRedisReachable() {

        Jedis jedis = pool.getResource();
        String pong = jedis.ping();
        assertEquals("Unable to contact Redis", "PONG", pong);
        pool.returnResource(jedis);
    }

    protected void testEntriesAreCorrect(List<Entry> entries, boolean zeroBasedIndex) {
        for (Entry entry : entries) {
            checkEntryIsCorrect(entry,zeroBasedIndex);
        }
    }
}
