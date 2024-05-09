package edu.cvtc.varr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Monsters {
    private String name;
    private String level;
    private int enemyHealth;
    private int enemyAttack;
    private int expDrop;

    public Monsters() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getEnemyHealth() {
        return enemyHealth;
    }

    public void setEnemyHealth(int enemyHealth) {
        this.enemyHealth = enemyHealth;
    }


    public int getEnemyAttack() {
        return enemyAttack;
    }

    public void setEnemyAttack(int enemyAttack) {
        this.enemyAttack = enemyAttack;
    }


    public int getExpDrop() {
        return expDrop;
    }
    public void setExpDrop(int expDrop) {this.expDrop = expDrop;}

    public static Monsters readMonstersFromFile(String filename) {
        Monsters monsters = new Monsters();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            monsters.setName(reader.readLine()); // Name(Orc)
            monsters.setLevel(reader.readLine()); // Level(1)
            monsters.setEnemyHealth(Integer.parseInt(reader.readLine()));
            monsters.setEnemyAttack(Integer.parseInt(reader.readLine()));
            monsters.setExpDrop(Integer.parseInt(reader.readLine()));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return monsters;
    }

    public void setInitialStats(String name, String level, int health, int attack, int expDrop) {
        this.name = name;
        this.level = level;
        this.enemyHealth = health;
        this.enemyAttack = attack;
        this.expDrop = expDrop;
    }

}
