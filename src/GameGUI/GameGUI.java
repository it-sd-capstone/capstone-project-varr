package GameGUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

import edu.cvtc.varr.Character;
import edu.cvtc.varr.Dungeon;
import edu.cvtc.varr.Items;
import edu.cvtc.varr.Monsters;


public class GameGUI extends JFrame {
    private static Character player = new Character();

    private Dungeon dungeon = new Dungeon();

    private static final Monsters monsters = new Monsters();
    private ArrayList<Items> inventory = new ArrayList<>();
    private ArrowButton leftArrow;
    private ArrowButton upArrow;
    private ArrowButton rightArrow;


    private static boolean bleed = false;
    private static int bleedCount = 0;
    private static final int playerBleedDamageValue = 1;
    private static final double playerCriticalDamageValue = 1.5;

    JPanel statsPanel;
    JButton playerAttackButton = new JButton("Attack");
    JLabel gameTextLabel;

    private static final ArrayList<String> statusEffect = new ArrayList<>();

    Random rng = new Random();

    int playerHealth = player.getHealth();
    int monsterHealth = monsters.getEnemyHealth();

    private static String status = "";


    public GameGUI() {
        /* Moved to main
        player.setHealth(100);
        player.setMana(50);
        player.setAttack(10);
        player.setArmor(10);
        player.setDefense(10);
        player.setGold(0);
        player.setLevel(1);
        player.setExperience(0);
         */


        Map<Integer, String> dungeonLevels = new HashMap<>();
        dungeonLevels.put(1, "Dwellers of Grave");

        setTitle("Varr Dungeon");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new BorderLayout());
        Border leftBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        leftPanel.setBorder(BorderFactory.createTitledBorder(leftBorder, "Stats"));


        // STATS

        statsPanel = new JPanel(new GridLayout(9, 1));

        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addStatsToPanel(statsPanel);

        // INVENTORY
        JPanel inventoryPanel = new JPanel(new GridLayout(1, 1));
        inventoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Inventory inventory = new Inventory();

        addInventoryToPanel(inventoryPanel, inventory);
        leftPanel.add(statsPanel, BorderLayout.NORTH);
        leftPanel.add(inventoryPanel, BorderLayout.CENTER);

        // ARROWS
        JPanel arrowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        leftArrow = new ArrowButton(50, 0, 0, 180);
        upArrow = new ArrowButton(50, 0, -20, 270);
        rightArrow = new ArrowButton(50, 0, 0, 0);
        Border arrowBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        arrowPanel.setBorder(BorderFactory.createTitledBorder(arrowBorder));
        arrowPanel.add(leftArrow);
        arrowPanel.add(upArrow);
        arrowPanel.add(rightArrow);

        // GAME TEXT
        JPanel gameTextPanel = new JPanel();
        gameTextPanel.setBackground(Color.WHITE);
        gameTextPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        container.add(gameTextPanel, BorderLayout.CENTER);

        gameTextLabel = new JLabel("TEST");
        gameTextLabel.setFont(new Font("Serif", Font.BOLD, 24));
        gameTextPanel.add(gameTextLabel);


        container.add(arrowPanel, BorderLayout.SOUTH);
        container.add(leftPanel, BorderLayout.WEST);


        // I added this to the arrow panel
        // this can be changed and moved, but set up to test function
        // attack button for player
        //playerAttackButton.setBorder(BorderFactory.createTitledBorder(arrowBorder));
        playerAttackButton.setPreferredSize(new Dimension(30, 30));
        playerAttackButton.setSize(50, 50);
        playerAttackButton.setForeground(Color.black);

        // attack button listen to initiated combat method
        playerAttackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                combat();
            }
        });

        arrowPanel.add(playerAttackButton);

        // Add action listeners to arrow buttons
        leftArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Left arrow clicked");
            }
        });

        upArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Up arrow clicked");
            }
        });

        rightArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Right arrow clicked");
            }
        });

        setVisible(true);
    }


    public static void main(String[] args) {

        // added monster health for testing
        monsters.setEnemyHealth(100);
        monsters.setEnemyAttack(10);


        statusEffect.add("bleedHit");
        statusEffect.add("criticalHit");
        statusEffect.add("normalHit");
        statusEffect.add("missHit");


        player.setName("TestName");
        player.setHealth(100);
        player.setMana(50);
        player.setAttack(10);
        player.setArmor(10);
        player.setDefense(10);
        player.setGold(0);
        player.setLevel(1);
        player.setExperience(0);

        //saveGame("saveFile1");
        //loadData("saveFile1");

        new GameGUI();


    }

    private void addStatsToPanel(JPanel statsPanel) {
        statsPanel.add(new JLabel("Name: " + player.getName()));
        statsPanel.add(new JLabel("Health: " + player.getHealth()));
        statsPanel.add(new JLabel("Mana: " + player.getMana()));
        statsPanel.add(new JLabel("Attack: " + player.getAttack()));
        statsPanel.add(new JLabel("Armor: " + player.getArmor()));
        statsPanel.add(new JLabel("Defense: " + player.getDefense()));
        statsPanel.add(new JLabel("Gold: " + player.getGold()));
        statsPanel.add(new JLabel("Level: " + player.getLevel()));
        statsPanel.add(new JLabel("Experience: " + player.getExperience()));
    }
    private void addInventoryToPanel(JPanel inventoryPanel, Inventory inventory) {
        DefaultListModel<Items> model = new DefaultListModel<>();
        for (Items item : inventory.showInventory()) {
            model.addElement(item);
        }
        JList<Items> inventoryList = new JList<>(model);
        inventoryPanel.add(inventoryList);
    }



    private void showStats() {
        StringBuilder statsMessage = new StringBuilder();
        statsMessage.append("Name: ").append(player.getName()).append("\n");
        statsMessage.append("Health: ").append(player.getHealth()).append("\n");
        statsMessage.append("Mana: ").append(player.getMana()).append("\n");
        statsMessage.append("Attack: ").append(player.getAttack()).append("\n");
        statsMessage.append("Armor: ").append(player.getArmor()).append("\n");
        statsMessage.append("Defense: ").append(player.getDefense()).append("\n");
        statsMessage.append("Gold: ").append(player.getGold()).append("\n");
        statsMessage.append("Level: ").append(player.getLevel()).append("\n");
        statsMessage.append("Experience: ").append(player.getExperience()).append("\n");

        JOptionPane.showMessageDialog(null, statsMessage.toString(), "Player Stats", JOptionPane.INFORMATION_MESSAGE);
    }

    // This is the outline for the saveGame method
    // Will need to update to save our data

    // May need to serialize data
    public static void saveGame(String fileNameToSave) {
        // This could be made a path if we have a folder for saves

        // Can do if (saveFile.createNewFile()) to check of the file
        // has already been created

        // This will overwrite the file. Do we want to overwrite files??
        try {

            // create file
            BufferedWriter saveToFile = new BufferedWriter(new FileWriter(fileNameToSave));

            // Will need to expand this for dungeon levels

            // For example
            saveToFile.write(player.getName());
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getHealth()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getMana()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getAttack()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getArmor()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getDefense()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getGold()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getLevel()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getExperience()));
            System.out.println("File Saved.");
            saveToFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This is the outline for the loadGame method
    // Will need to update to save our data

    // May need to serialize data
    public static void loadData(String fileNameToLoad) {
        try {

            // Read save file
            BufferedReader readLoadFile = new BufferedReader(new FileReader(fileNameToLoad));

            // Can load data from readLoadFile to player/game data variables
            player.setName(readLoadFile.readLine());
            player.setHealth(Integer.parseInt(readLoadFile.readLine()));
            player.setMana(Integer.parseInt(readLoadFile.readLine()));
            player.setAttack(Integer.parseInt(readLoadFile.readLine()));
            player.setArmor(Integer.parseInt(readLoadFile.readLine()));
            player.setDefense(Integer.parseInt(readLoadFile.readLine()));
            player.setGold(Integer.parseInt(readLoadFile.readLine()));
            player.setLevel(Integer.parseInt(readLoadFile.readLine()));
            player.setExperience(Integer.parseInt(readLoadFile.readLine()));

            System.out.println("Load successful.");
            readLoadFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void combat() {

        // if bleed is inflicted, player will take damage on their turn
        // player will only take 3 turns of bleed damage
        if (bleed) {
            bleedCount += 1;

            // message to user
            System.out.println("You take bleed damage");
            gameTextLabel.setText("You take bleed damage.");

            // player bleed damage
            player.setHealth(playerHealth -= playerBleedDamageValue);



            // only 3 turns of bleed damage
            if (bleedCount == 3) {
                bleedCount = 0;
                bleed = false;
            }

            // reset GUI to update player attributes
            statsPanel.removeAll();
            statsPanel.revalidate();
            statsPanel.repaint();
            addStatsToPanel(statsPanel);

        }

        System.out.println("You hit monster.");
        gameTextLabel.setText("You take bleed damage.");

        monsters.setEnemyHealth(monsterHealth -= player.getAttack());

        if (monsters.getEnemyHealth() <= 0) {

            // message to user
            System.out.println("You killed monster");
            return;
        }

        // random number to decide what type of hit monster will do
        Random rng = new Random();
        int selectedStatusEffect = rng.nextInt((4));

        // using random number to select a status type
        // status types are bleedHit, criticalHit, missHit, and normalHit
        status = statusEffect.get(selectedStatusEffect);
        System.out.println(status);

        if (status.equals("bleedHit")) {
            // message to user
            System.out.println("Monster inflicts bleed on you");

            // player takes hit and will take bleed damage on their turn
            player.setHealth(playerHealth -= monsters.getEnemyAttack());

            // reset GUI to update player attributes
            statsPanel.removeAll();
            statsPanel.revalidate();
            statsPanel.repaint();
            addStatsToPanel(statsPanel);

            // bleed status
            bleed = true;

        } else if (status.equals("criticalHit")) {
            // message to user
            System.out.println("Monster inflicts a critical on you.");
            player.setHealth(playerHealth -= (int) (monsters.getEnemyAttack() * playerCriticalDamageValue));

            // reset GUI to update player attributes
            statsPanel.removeAll();
            statsPanel.revalidate();
            statsPanel.repaint();
            addStatsToPanel(statsPanel);

        } else if (status.equals("missHit")) {
            // message to user
            System.out.println("Monster missed.");
        } else {
            // message to user
            System.out.println("Monster hit you.");
            player.setHealth(playerHealth -= monsters.getEnemyAttack());

            // reset GUI to update player attributes
            statsPanel.removeAll();
            statsPanel.revalidate();
            statsPanel.repaint();
            addStatsToPanel(statsPanel);
        }

        if (player.getHealth() <= 0) {
            // message to user
            System.out.println("You died");
            player.setHealth(0);
            statsPanel.removeAll();
            statsPanel.revalidate();
            statsPanel.repaint();
            addStatsToPanel(statsPanel);
            return;
        }
    }

}