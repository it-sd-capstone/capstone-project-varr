package edu.cvtc.varr;

public class Monsters {
    private String name;
    private String level;
    private int enemyHealth;
    private int enemyMana;
    private int enemyAttack;
    private int enemyDefense;
    private int enemyArmor;
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

    public int getEnemyMana() {
        return enemyMana;
    }

    public void setEnemyMana(int enemyMana) {
        this.enemyMana = enemyMana;
    }

    public int getEnemyAttack() {
        return enemyAttack;
    }

    public void setEnemyAttack(int enemyAttack) {
        this.enemyAttack = enemyAttack;
    }

    public int getEnemyDefense() {
        return enemyDefense;
    }

    public void setEnemyDefense(int enemyDefense) {
        this.enemyDefense = enemyDefense;
    }

    public int getEnemyArmor() {
        return enemyArmor;
    }

    public void setEnemyArmor(int enemyArmor) {
        this.enemyArmor = enemyArmor;
    }

    public void getExpDrop() {
        this.expDrop = expDrop;
    }
    public void setExpDrop(int expDrop) {this.expDrop = expDrop;}

}
