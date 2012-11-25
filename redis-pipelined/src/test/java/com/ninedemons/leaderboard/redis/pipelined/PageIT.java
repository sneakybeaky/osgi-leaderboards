package com.ninedemons.leaderboard.redis.pipelined;

import com.ninedemons.leaderboard.api.Page;
import org.testng.annotations.Test;

import static junit.framework.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by Jon Barber
 */
public class PageIT extends BaseRedisTest {

    @Test
    public void testNullLeaderboardName() {
        Page found = underTest.page(null,0);
        assertNotNull("Should always return a Page", found);
        assertEquals("Should be page 0", 0, found.getPageNumber());
        assertNotNull("Should have a 0 length list of entries", found.getEntries());
        assertEquals("Should have a 0 length list of entries", 0, found.getEntries().size());
    }


}
