package GameGUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

import edu.cvtc.varr.*;
import edu.cvtc.varr.Character;


public class GameGUI extends JFrame {
    private static Character player = new Character();

    private Dungeon dungeon;
    private Room currentRoom;
    private JTextArea textArea;

    private MonsterEncounter  monsterEncounter;
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

        setTitle("Varr Dungeon");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
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

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setPreferredSize(new Dimension(500, 300));
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);



        gameTextPanel.add(textArea);


        // SAVE/LOAD
        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(100, 30));
        saveButton.setSize(50, 50);
        saveButton.setForeground(Color.black);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame("save.txt");
            }
        });


        JButton loadButton = new JButton("Load");
        loadButton.setPreferredSize(new Dimension(100, 30));
        loadButton.setSize(50, 50);
        loadButton.setForeground(Color.black);
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData("save.txt");
            }
        });

        dungeon = new Dungeon(3, 4);
        // create rooms and set up exits
        Room startRoom = new Room("You find yourself standing at the entrance of the dungeon.\n" +
                "Dim torchlight flickers against the cold stone walls, casting long shadows across the floor. \n" +
                "The air is thick with a musty scent, and a sense of anticipation hangs in the air as you prepare to embark on your perilous journey.\n\n\n" +
                "Where will you go ?", true);

        Room bottomLeft = new Room("The room is small and cramped, with barely enough space to maneuver.\n" +
                "Cobwebs cling to the corners, and the floor is littered with debris from fallen stones.\n" +
                "You can hear the distant echo of dripping water, adding to the eerie atmosphere of the dungeon.\n\n\n" +
                "Where will you go ?", true);

        Room bottomRight = new Room("As you enter, a gust of stale air rushes past you, carrying the scent of decay.\n" +
                "The room is shrouded in darkness, illuminated only by faint beams of moonlight filtering in through narrow cracks in the ceiling.\n" +
                "You can sense danger lurking in the shadows, and your instincts tell you to proceed with caution.\n\n\n" +
                "Where will you go ?", true);

        Room middleLeft = new Room("This room is larger than the others you've encountered so far, with high ceilings and expansive walls adorned with faded tapestries.\n" +
                "The floor is smooth and uneven, worn down by centuries of footprints.\n" +
                "You can hear the faint sound of footsteps echoing in the distance, hinting at the presence of other creatures within the dungeon\n\n\n"+
                "The door behind you closes, and you realize that there's no going back to the room before\n" +
                "Where will you go ?", true);


        Room middleMiddle = new Room(" You step into the heart of the dungeon, surrounded by towering pillars of stone that stretch up towards the ceiling.\n" +
                "The air is heavy with a sense of foreboding, and a chill runs down your spine as you gaze into the darkness ahead.\n" +
                "You can't help but feel a sense of trepidation as you press forward into the unknown.\n\n\n"+
                "The door behind you closes, and you realize that there's no going back to the room before\n" +
                "Where will you go ?", true);

        Room middleRight = new Room("The room is bathed in a soft, golden light that emanates from a crack in the wall.\n" +
                "You can see intricate carvings etched into the stone, depicting scenes of ancient battles and forgotten heroes.\n" +
                "Despite the beauty of the artwork, a sense of unease washes over you, as if the walls themselves are watching your every move..\n\n\n"+
                "The door behind you closes, and you realize that there's no going back to the room before\n" +
                "Where will you go ?", true);


        Room topLeft = new Room("As you enter, you are greeted by the sound of rushing water and the faint scent of damp earth.\n" +
                " The room is dominated by a large, ornate fountain, its waters shimmering in the dim light.\n" +
                " Moss clings to the walls, adding to the sense of tranquility that permeates the space.\n\n\n"+
                "The door behind you closes, and you realize that there's no going back to the room before. You feel an eerie presence in the air.\n" +
                "You only hope you can survive whatever is ahead.\n" +
                "Where will you go ?", true);


        Room topMiddle = new Room("This room is filled with the sound of chirping birds and rustling leaves, creating a stark contrast to the darkness of the dungeon outside.\n" +
                "Sunlight streams in through a hole in the ceiling, illuminating the space with a warm, golden glow.\n" +
                "You can't help but feel a sense of relief as you bask in the light, if only for a moment.\n\n\n"+
                "The door behind you closes, and you realize that there's no going back to the room before. You feel an eerie presence in the air.\n" +
                "You only hope you can survive whatever is ahead.\n" +
                "Where will you go ?", true);

        Room topRight = new Room("You stand on the threshold of the final chamber, your heart pounding in your chest as you prepare to face the ultimate challenge.\n" +
                "The room is vast and cavernous, with towering pillars of stone rising up towards the heavens.\n" +
                " At the center of the chamber stands a massive throne, its occupant cloaked in shadow.\n" +
                "You can feel the raw power emanating from the figure, and you steel yourself for the battle ahead\n\n\n"+
                "The door behind you closes, and you realize that there's no going back to the room before. You feel an eerie presence in the air.\n" +
                "You only hope you can survive whatever is ahead.\n" +
                "Where will you go ?", true);


        Room bossRoom = new Room("You are in the boss room.\n", true);

        //Bottom row
        startRoom.addExit("north", middleMiddle);
        startRoom.addExit("east", bottomRight);
        startRoom.addExit("west", bottomLeft);

        bottomLeft.addExit("north", middleLeft);
        bottomLeft.addExit("east", startRoom);

        bottomRight.addExit("north", middleRight);
        bottomRight.addExit("west", startRoom);

        //Middle row
        middleLeft.addExit("north", topLeft);
        middleLeft.addExit("east", middleMiddle);

        middleMiddle.addExit("north", topMiddle);
        middleMiddle.addExit("west", middleLeft);
        middleMiddle.addExit("east", middleRight);

        middleRight.addExit("north", topRight);
        middleRight.addExit("west", middleMiddle);

        //Top row
        topLeft.addExit("north", bossRoom);
        topLeft.addExit("east", topMiddle);

        topMiddle.addExit("north", bossRoom);
        topMiddle.addExit("east", topRight);
        topMiddle.addExit("west", topLeft);

        topRight.addExit("north", bossRoom);
        topRight.addExit("west", topMiddle);


        // set the rooms in the dungeon
        dungeon.setRoom(0, 0, topLeft);
        dungeon.setRoom(0, 1, bossRoom);
        dungeon.setRoom(0, 2, topRight);
        dungeon.setRoom(1, 0, middleLeft);
        dungeon.setRoom(1, 1, middleMiddle);
        dungeon.setRoom(1, 2, middleRight);
        dungeon.setRoom(2, 0, bottomLeft);
        dungeon.setRoom(2, 1, startRoom);
        dungeon.setRoom(2, 2, bottomRight);
        // Add action listeners to arrow buttons

        currentRoom = startRoom;
        textArea.setText(currentRoom.getDescription());
        leftArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Left arrow clicked");
                Room exitRoom = currentRoom.getExit("west");
                if (exitRoom!= null) {
                    currentRoom = exitRoom;
                    textArea.setText(currentRoom.getDescription());
                } else {
                    System.out.println("You can't go that way.");
                }
            }
        });

        // Add action listener to up arrow button
        upArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Up arrow clicked");
                Room exitRoom = currentRoom.getExit("north");
                if (exitRoom!= null) {
                    currentRoom = exitRoom;
                    textArea.setText(currentRoom.getDescription());
                } else {
                    System.out.println("You can't go that way.");
                }
            }
        });

        // Add action listener to right arrow button
        rightArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Right arrow clicked");
                Room exitRoom = currentRoom.getExit("east");
                if (exitRoom!= null) {
                    currentRoom = exitRoom;
                    textArea.setText(currentRoom.getDescription());
                } else {
                    System.out.println("You can't go that way.");
                }
            }
        });

        // MONSTER ENCOUNTER
        monsterEncounter = new MonsterEncounter(dungeon,currentRoom);


        // I added this to the arrow panel
        // this can be changed and moved, but set up to test function
        // attack button for player
        //playerAttackButton.setBorder(BorderFactory.createTitledBorder(arrowBorder));
        playerAttackButton.setPreferredSize(new Dimension(100, 30));
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
        container.add(arrowPanel, BorderLayout.SOUTH);
        container.add(leftPanel, BorderLayout.WEST);
        arrowPanel.add(loadButton);
        arrowPanel.add(saveButton);
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