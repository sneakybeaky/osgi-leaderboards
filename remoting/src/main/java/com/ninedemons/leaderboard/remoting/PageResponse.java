package com.ninedemons.leaderboard.remoting;

import com.ninedemons.leaderboard.api.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jon Barber
 */
@XmlRootElement

public class PageResponse {

    private int pageNumber;

    @XmlElement
    private List<EntryResponse> entries;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<EntryResponse> getEntries() {
        return entries;
    }

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

}
