package com.ral.server.game.db.user.model;

import java.io.Serializable;

public class User implements Serializable {


    private static final long serialVersionUID = 2690067586540641247L;

    public Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
