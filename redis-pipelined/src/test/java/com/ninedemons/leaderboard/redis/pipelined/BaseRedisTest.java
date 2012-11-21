package com.ninedemons.leaderboard.redis.pipelined;

import com.ninedemons.leaderboard.api.Entry;
import org.testng.annotations.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Jon Barber at AKQA
 */
public abstract class BaseRedisTest {

    protected static String HIGHEST_RANKED_USER;
    protected static String LOWEST_RANKED_USER;
    protected static String MID_RANKED_USER;

    JedisPool pool;
    String leaderboardName;
    PipelinedLeaderboard underTest;

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
        Jedis jedis = pool.getResource();
        leaderboardName = "test_leaderboard_" + Long.toString(jedis.incr("test_loaderboard_name"));

        for (int userNumber = 0; userNumber < 100; userNumber++) {
            int score = 1000 - userNumber;
            jedis.zadd(leaderboardName, score, "user_" + userNumber);
        }
        HIGHEST_RANKED_USER = "user_0";
        MID_RANKED_USER = "user_50";
        LOWEST_RANKED_USER = "user_99";

        pool.returnResource(jedis);
    }

    public void checkRedisReachable() {

        Jedis jedis = pool.getResource();
        String pong = jedis.ping();
        assertEquals("Unable to contact Redis", "PONG", pong);
        pool.returnResource(jedis);
    }
}
