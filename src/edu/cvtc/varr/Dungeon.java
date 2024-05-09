package edu.cvtc.varr;

public class Dungeon {
    private String name;
    private int level = 1;
    private Room[][] rooms;

    public Dungeon(int width, int height) {
        rooms = new Room[width][height];
    }
    public void setRoom(int x, int y, Room room) {
        rooms[x][y] = room;
    }

    public Room getRoom(int x, int y) {
        return rooms[x][y];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
