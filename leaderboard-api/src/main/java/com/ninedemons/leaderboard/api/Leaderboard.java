package com.ninedemons.leaderboard.api;


import java.util.Collection;
import java.util.List;

public interface Leaderboard {

    List<Entry> aroundMe(String leaderboardName,String userId);

    List<Entry> friends(String leaderboardName,Collection<String> userIds);

    List<Entry> page(String leaderboardName,int page);

}
