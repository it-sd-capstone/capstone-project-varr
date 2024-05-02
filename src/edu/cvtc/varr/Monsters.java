package edu.cvtc.varr;

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


    public void getExpDrop() {
        this.expDrop = expDrop;
    }
    public void setExpDrop(int expDrop) {this.expDrop = expDrop;}

}
