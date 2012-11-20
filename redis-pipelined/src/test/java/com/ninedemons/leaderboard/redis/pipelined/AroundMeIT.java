package com.ninedemons.leaderboard.redis.pipelined;

import com.ninedemons.leaderboard.api.Entry;
import junit.framework.Assert;
import org.testng.annotations.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Jon Barber
 */
public class AroundMeIT {

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
        populateTestData();
    }

    @AfterTest
    public void afterTest() {
        restoreRedisState();
    }

    @Test
    public void testAroundMeHappyPath() {
        List<Entry> found = underTest.aroundMe(leaderboardName,"user_50");
        Assert.assertNotNull("Should have returned a list of users",found);
        assertEquals("Should have the full page size of users", underTest.getPageSize(), found.size());

        Entry middleEntry = found.get(found.size() / 2);
        assertEquals("User_50 should be the first entry",middleEntry.getUserId(),"user_50");

    }

    @Test
    public void testForTopRankedUser() {
        List<Entry> found = underTest.aroundMe(leaderboardName,"user_0");
        assertEquals("Should have the full page size of users", underTest.getPageSize(), found.size());

        Entry first = found.get(0);
        assertEquals("User_0 should be the first entry",first.getUserId(),"user_0");
    }

    @Test
    public void testForBottomRankedUser() {
        List<Entry> found = underTest.aroundMe(leaderboardName,"user_99");
        assertEquals("Should have the full page size of users", underTest.getPageSize(), found.size());

        Entry last = found.get(found.size()-1);
        assertEquals("User_99 should be the first entry",last.getUserId(),"user_99");
    }

    @Test
    public void testZeroBasedIndex() {
        underTest.setUseZeroIndexForRank(true);
        List<Entry> found = underTest.aroundMe(leaderboardName,"user_0");
        assertEquals("Should have the full page size of users", underTest.getPageSize(), found.size());

        Entry first = found.get(0);
        assertEquals("First user should have a rank of 0",new Long(0),first.getRank());
    }

    @Test
    public void testOneBasedIndex() {
        underTest.setUseZeroIndexForRank(false);
        List<Entry> found = underTest.aroundMe(leaderboardName,"user_0");
        assertEquals("Should have the full page size of users", underTest.getPageSize(), found.size());

        Entry first = found.get(0);
        assertEquals("First user should have a rank of 1",new Long(1),first.getRank());
    }

    private void restoreRedisState() {
        Jedis jedis = pool.getResource();
        jedis.del(leaderboardName);
        pool.returnResource(jedis);
    }

    private void populateTestData() {
        Jedis jedis = pool.getResource();
        leaderboardName = "test_leaderboard_" + Long.toString(jedis.incr("test_loaderboard_name"));

        for (int userNumber=0;userNumber<100;userNumber++) {
            int score = 1000 - userNumber;
            jedis.zadd(leaderboardName,score,"user_"+userNumber);
        }

        pool.returnResource(jedis);
    }

    public void checkRedisReachable() {

        Jedis jedis = pool.getResource();
        String pong = jedis.ping();
        assertEquals("Unable to contact Redis", "PONG", pong);
        pool.returnResource(jedis);
    }

}