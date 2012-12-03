package com.ninedemons.leaderboard.remoting;

import com.ninedemons.leaderboard.api.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jon Barber
 */
@XmlRootElement
public class EntriesResponse {

    @XmlElement
    private List<EntryResponse> entries;

    public void setEntries(List<Entry> entries) {

        List<EntryResponse> copy = new ArrayList<EntryResponse>(entries.size());

        for (Entry entry : entries) {
            EntryResponse response = new EntryResponse();
            response.setRank(entry.getRank());
            response.setScore(entry.getScore());
            response.setUserId(entry.getUserId());
            copy.add(response);

        }

        this.entries = copy;
    }

    public List<EntryResponse> getEntries() {
        return new ArrayList<EntryResponse>(entries);
    }

}
