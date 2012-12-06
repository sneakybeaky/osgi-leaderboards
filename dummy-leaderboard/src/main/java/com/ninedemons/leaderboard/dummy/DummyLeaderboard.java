package com.ninedemons.leaderboard.dummy;

import com.ninedemons.leaderboard.api.Entry;
import com.ninedemons.leaderboard.api.Leaderboard;
import com.ninedemons.leaderboard.api.Page;
import com.ninedemons.leaderboard.api.impl.ImmutableEntry;
import com.ninedemons.leaderboard.api.impl.ImmutablePage;
import org.apache.felix.scr.annotations.*;
import org.osgi.framework.Constants;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jon Barber
 */
@Component(immediate = true)
@Service(Leaderboard.class)

@Properties({
        @Property(name = Constants.SERVICE_VENDOR, value = "Nine Demons"),
        @Property(name = Constants.SERVICE_DESCRIPTION, value = "Really dumb leaderboard"),
        @Property(name = Constants.SERVICE_RANKING, value = "-100")
})
public class DummyLeaderboard implements Leaderboard {

    private List<Entry> allEntries;
    private Page onlyPage;

    @Override
    public List<Entry> aroundMe(String leaderboardName, String userId) {
        return allEntries;
    }

    @Override
    public List<Entry> friends(String leaderboardName, Collection<String> userIds) {
        return allEntries;
    }

    @Override
    public Page page(String leaderboardName, int pageNumber) {
        return onlyPage;
    }

    @Activate
    public void setup() {
        Entry onlyEntry = new ImmutableEntry("only_user", (long) 1, (double) 10);
        allEntries = Collections.singletonList(onlyEntry);
        onlyPage = new ImmutablePage(0, allEntries);
    }
}
