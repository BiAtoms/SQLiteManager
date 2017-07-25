package com.sqlitemanager;

/**
 * Created by aslan on 7/22/2017.
 */

public enum SortOrder {
    ASC(1, "ASC"),
    DESC(2, "DESC");

    private int id;
    private String keyWord;

    private SortOrder(int id, String keyWord) {
        this.id = id;
        this.keyWord = keyWord;
    }

    int getId() {
        return this.id;
    }

    String getKeyWord() {
        return this.keyWord;
    }
}
