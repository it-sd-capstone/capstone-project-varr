package GameGUI;

import edu.cvtc.varr.Dungeon;
import edu.cvtc.varr.Monsters;
import edu.cvtc.varr.Room;

import java.util.Random;

public class MonsterEncounter {
    private Monsters monster;
    private Dungeon dungeon;
    private Room currentRoom;
    public MonsterEncounter(Dungeon dungeon, Room currentRoom){
        this.dungeon = dungeon;
        this.currentRoom = currentRoom;
        System.out.println("Current room: " + currentRoom.getDescription());
        System.out.println("Monster encounter: " + currentRoom.monsterEncounter());
        if(currentRoom.monsterEncounter()){
            int encounter = new Random().nextInt(100);
            if(currentRoom == dungeon.getRoom(0, 1)) {
                monster = Boss();
                System.out.println("You have encountered the" + monster.getName());
            } else{
                if(encounter < 50) {
                    monster = Monster();
                    System.out.println("You have encountered the " + monster.getName());
                }
            }
        }


    }
    public Monsters Monster() {
        Monsters monster = new Monsters();
        monster.setName("Orc");
        monster.setEnemyHealth(20);
        monster.setEnemyAttack(3);
        monster.setExpDrop(3);
        return monster;
    }

    public Monsters Boss() {
        Monsters boss = new Monsters();
        boss.setName("Boss");
        boss.setEnemyHealth(26);
        boss.setEnemyAttack(5);
        boss.setExpDrop(10);
        return boss;
    }



}
