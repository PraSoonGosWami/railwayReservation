package com.invaderx.railway.models;

import ir.mirrajabi.searchdialog.core.Searchable;

public class Stations implements Searchable {
    private String title;

    public Stations(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

}
