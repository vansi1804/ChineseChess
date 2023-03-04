package com.example.chinesechesstrainning.model;

import java.io.Serializable;

public class Match implements Serializable {
    private long id;
    private String name;
    private long parentMatchId;

    public Match(long id, String name, long matchId) {
        this.id = id;
        this.name = name;
        this.parentMatchId = matchId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParentMatchId() {
        return parentMatchId;
    }

    public void setParentMatchId(long parentMatchId) {
        this.parentMatchId = parentMatchId;
    }
}
