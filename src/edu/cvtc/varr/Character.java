package edu.cvtc.varr;

public class Character {

    private String name;
    private int health;
    private int experience;
    private int attack;
    private int level;
    private int expToLevelUp = 5;
    private int maxHealth;
    private Items equippedItem;


    public Character() {

    }

    public void addExp(int exp){
        experience += exp;
        checkForLevelUp();
        System.out.println("Added " + experience);
        System.out.println("Current experience: " + getExperience());

    }


    public void checkForLevelUp(){
        while(experience >= expToLevelUp){
            levelUp();
            experience -= experience;
        }
    }

    public void levelUp(){
        level++;
        expToLevelUp = (int)(expToLevelUp * 1.5);
        health += 10;
        maxHealth += 10;
        attack += 5;
        System.out.println("You leveled up! You are now level " + level);

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExpToLevelUp() {
        return expToLevelUp;

    }
    public void setExpToLevelUp(int expToLevelUp) {
        this.expToLevelUp = expToLevelUp;

    }

    public int getMaxHealth() {
        return maxHealth;

    }
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;


    }
    public Items getEquippedItem() {
        return equippedItem;
    }
    public void setEquippedItem(Items equippedItem) {
        this.equippedItem = equippedItem;

    }

}
