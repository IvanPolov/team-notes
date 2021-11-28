package com.gbdevteam.teamnotes.model;

public enum BoardRoleEnum {
    READER("reader of private board, only can read notes"),
    WRITER("writer of private board, can manage notes"),
    MANAGER("can manage users of board, except owner, includes writer rights"),
    OWNER("owns private board, have all rights");

    private final String description;

    BoardRoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
