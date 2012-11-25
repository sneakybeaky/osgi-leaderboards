package com.ninedemons.leaderboard.api.impl;

import com.ninedemons.leaderboard.api.Entry;
import com.ninedemons.leaderboard.api.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jon Barber
 */
public class ImmutablePage implements Page {

    protected int pageNumber;
    protected List<Entry> entries;

    public ImmutablePage(int pageNumber, List<Entry> entries) {
        this.pageNumber = pageNumber;
        this.entries = new ArrayList<Entry>(entries);
    }

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Override
    public List<Entry> getEntries() {
        return new ArrayList<Entry>(entries);
    }
}
