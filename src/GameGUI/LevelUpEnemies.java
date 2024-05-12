package GameGUI;

import edu.cvtc.varr.Dungeon;
import edu.cvtc.varr.Monsters;

import java.io.*;

public class LevelUpEnemies {
    private GameGUI gui;
    private Dungeon dungeon;

    private static final String REGULAR_ENEMY_FILE = "capstone-project-varr\\Orc.txt";
    private static final String BOSS_ENEMY_FILE = "capstone-project-varr\\Level1Boss.txt";

    private Monsters regularEnemy;
    private Monsters boss;

    public LevelUpEnemies() {
        regularEnemy = loadEnemy(REGULAR_ENEMY_FILE);
        boss = loadEnemy(BOSS_ENEMY_FILE);
    }

    private Monsters loadEnemy(String filePath) {
        Monsters enemy = new Monsters();
        try (BufferedReader enemyLoader = new BufferedReader(new FileReader(filePath))) {
            enemy.setName(enemyLoader.readLine());
            enemy.setLevel(enemyLoader.readLine());
            enemy.setEnemyHealth(Integer.parseInt(enemyLoader.readLine()));
            enemy.setEnemyAttack(Integer.parseInt(enemyLoader.readLine()));
            enemy.setExpDrop(Integer.parseInt(enemyLoader.readLine()));
        } catch (FileNotFoundException e) {
            System.out.println("Enemy file not found: " + filePath);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in enemy file: " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading enemy file: " + filePath);
        }
        return enemy;
    }

    public void increaseEnemyLevel() {
        increaseRegularEnemyLevel();
        increaseBossLevel();
    }

    public void increaseRegularEnemyLevel() {
        try {
            if (regularEnemy.getLevel()!= null) {
                // Read the current stats from the file
                reloadEnemyStats(REGULAR_ENEMY_FILE);

                // Update the stats
                regularEnemy.setLevel(String.valueOf(Integer.parseInt(regularEnemy.getLevel()) + 1));
                regularEnemy.setEnemyHealth(regularEnemy.getEnemyHealth() + 10);
                regularEnemy.setEnemyAttack(regularEnemy.getEnemyAttack() + 5);
                regularEnemy.setExpDrop(regularEnemy.getExpDrop() + 5);
                System.out.println("Regular enemy level: " + regularEnemy.getLevel());

                // Write the updated stats back to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(REGULAR_ENEMY_FILE))) {
                    writer.write(regularEnemy.getName() + "\n");
                    writer.write(regularEnemy.getLevel() + "\n");
                    writer.write(String.valueOf(regularEnemy.getEnemyHealth()) + "\n");
                    writer.write(String.valueOf(regularEnemy.getEnemyAttack()) + "\n");
                    writer.write(String.valueOf(regularEnemy.getExpDrop()) + "\n");
                } catch (IOException e) {
                    System.out.println("Error writing to regular enemy file: " + e.getMessage());
                }

                System.out.println("Enemy EXP: " + regularEnemy.getExpDrop());
                System.out.println("Enemy Health: " + regularEnemy.getEnemyHealth());
                System.out.println("Enemy Level: " + regularEnemy.getLevel());
            } else {
                System.out.println("Regular enemy level is null");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error parsing regular enemy level: " + e.getMessage());
        }
    }

    public void increaseBossLevel() {
        try {
            if (boss.getLevel()!= null) {
                // Read the current stats from the file
                reloadEnemyStats(BOSS_ENEMY_FILE);

                // Update the stats
                boss.setLevel(String.valueOf(Integer.parseInt(boss.getLevel()) + 1));
                boss.setEnemyHealth(boss.getEnemyHealth() + 50);
                boss.setEnemyAttack(boss.getEnemyAttack() + 10);
                boss.setExpDrop(boss.getExpDrop() + 10);

                // Write the updated stats back to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOSS_ENEMY_FILE))) {
                    writer.write(boss.getName() + "\n");
                    writer.write(boss.getLevel() + "\n");
                    writer.write(String.valueOf(boss.getEnemyHealth()) + "\n");
                    writer.write(String.valueOf(boss.getEnemyAttack()) + "\n");
                    writer.write(String.valueOf(boss.getExpDrop()) + "\n");
                } catch (IOException e) {
                    System.out.println("Error writing to boss file: " + e.getMessage());
                }

                System.out.println("Boss EXP: " + boss.getExpDrop());
                System.out.println("Boss Health: " + boss.getEnemyHealth());
                System.out.println("Boss Level: " + boss.getLevel());
            } else {
                System.out.println("Boss level is null");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error parsing boss level: " + e.getMessage());
        }
    }
    public Monsters reloadEnemyStats(String filePath) {
        Monsters enemy = new Monsters();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            enemy.setName(reader.readLine());
            enemy.setLevel(reader.readLine());
            enemy.setEnemyHealth(Integer.parseInt(reader.readLine()));
            enemy.setEnemyAttack(Integer.parseInt(reader.readLine()));
            enemy.setExpDrop(Integer.parseInt(reader.readLine()));
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing enemy stats: " + e.getMessage());
        }
        return enemy;
    }



}