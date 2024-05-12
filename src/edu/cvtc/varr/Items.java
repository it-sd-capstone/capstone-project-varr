package edu.cvtc.varr;

public class Items {

    private String name;
    private String rarity;
    private int damage;

    public Items() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }
    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public Items(String name, String rarity, int damage) {
        this.name = name;
        this.rarity = rarity;
        this.damage = damage;
    }


    @Override
    public String toString() {
        return getName() + " (" + getRarity() + ")";
    }
}
