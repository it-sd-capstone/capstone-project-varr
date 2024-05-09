package GameGUI;

import edu.cvtc.varr.Dungeon;
import edu.cvtc.varr.Monsters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GameLoop {
    private GameGUI gui;
    private Dungeon dungeon;
    Monsters regularEnemy = new Monsters();
    Monsters bossEnemy = new Monsters();

    private static final String REGULAR_ENEMY_FILE = "C:\\Users\\Nhia Vue\\OneDrive\\Desktop\\v0.0.4\\capstone-project-varr\\Orc.txt";
    private static final String BOSS_ENEMY_FILE = "C:\\Users\\Nhia Vue\\OneDrive\\Desktop\\v0.0.4\\capstone-project-varr\\Dragon.txt";


    public GameLoop(GameGUI gui) {
        this.gui = gui;
        dungeon = new Dungeon(3, 4);
    }

    public void increaseEnemyLevel(Monsters regularEnemy,Monsters bossEnemy) {

        try (BufferedReader enemyLoader = new BufferedReader(new FileReader(REGULAR_ENEMY_FILE))) {
            regularEnemy.setName(enemyLoader.readLine());
            regularEnemy.setLevel(enemyLoader.readLine());
            regularEnemy.setEnemyHealth(Integer.parseInt(enemyLoader.readLine()));
            regularEnemy.setEnemyAttack(Integer.parseInt(enemyLoader.readLine()));
            regularEnemy.setExpDrop(Integer.parseInt(enemyLoader.readLine()));
        } catch (FileNotFoundException e) {
            System.out.println("Regular enemy file not found: " + REGULAR_ENEMY_FILE);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in regular enemy file: " + REGULAR_ENEMY_FILE);
        } catch (IOException e) {
            System.out.println("Error reading regular enemy file: " + REGULAR_ENEMY_FILE);
        }

        regularEnemy.setLevel(String.valueOf(Integer.parseInt(regularEnemy.getLevel()) + 1));

        try (BufferedReader bossLoader = new BufferedReader(new FileReader(BOSS_ENEMY_FILE))) {
            bossEnemy.setName(bossLoader.readLine());
            bossEnemy.setLevel(bossLoader.readLine());
            bossEnemy.setEnemyHealth(Integer.parseInt(bossLoader.readLine()));
            bossEnemy.setEnemyAttack(Integer.parseInt(bossLoader.readLine()));
            bossEnemy.setExpDrop(Integer.parseInt(bossLoader.readLine()));
        } catch (FileNotFoundException e) {
            System.out.println("Boss enemy file not found: " + BOSS_ENEMY_FILE);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in boss enemy file: " + BOSS_ENEMY_FILE);
        } catch (IOException e) {
            System.out.println("Error reading boss enemy file: " + BOSS_ENEMY_FILE);
        }

    }



}
