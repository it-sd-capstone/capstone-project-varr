package GameGUI;

import edu.cvtc.varr.Character;
import edu.cvtc.varr.Dungeon;

public class Heal {

    private Dungeon dungeon = new Dungeon(3,4);

    public void healPlayer(Character player) {
        int healAmount = 10;
        int currentLevel = dungeon.getLevel();
        healAmount = healAmount + (currentLevel * 10);
        int newHealth = player.getHealth() + healAmount;
        if (newHealth > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        } else {
            player.setHealth(newHealth);
        }
        System.out.println("You have been healed for " + healAmount + " health.");
        System.out.println("Your current health is " + player.getHealth() + ".");
    }

}
