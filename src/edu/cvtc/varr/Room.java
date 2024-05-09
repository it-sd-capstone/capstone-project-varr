package edu.cvtc.varr;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private String description;
    private boolean monsterEncounter;
    private Map<String, Room> exits;
    private boolean encounterOccurred;

    public Room(String description, boolean monsterEncounter) {
        this.description = description;
        this.monsterEncounter = monsterEncounter;
        this.exits = new HashMap<>();
    }
    public Room(boolean monsterEncounter) {
        this.monsterEncounter = monsterEncounter;

    }

    public boolean monsterEncounter() {
        return monsterEncounter;
    }

    public void addExit(String direction, Room room) {
        exits.put(direction, room);
    }

    public String getDescription() {
        return description;
    }


    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public boolean hasEncounterOccurred() {
        return encounterOccurred;
    }

    public void setEncounterOccurred(boolean encounterOccurred) {
        this.encounterOccurred = encounterOccurred;
    }


}