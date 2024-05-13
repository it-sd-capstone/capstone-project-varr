package GameGUI;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;

import edu.cvtc.varr.*;
import edu.cvtc.varr.Character;


public class GameGUI extends JFrame {
    private static Character player = new Character();

    private Dungeon dungeon;
    private Room currentRoom;
    private JTextArea textArea;

    private Monsters monsters = new Monsters();

    private Monsters bossMonster = new Monsters();
    private static Monsters initialOrc;
    private static Monsters initialBoss;

    boolean bossCombat = false;

    boolean monsterCombat = false;
    private ArrowButton leftArrow;
    private ArrowButton upArrow;
    private ArrowButton rightArrow;


    private static boolean bleed = false;
    private static int bleedCount = 0;
    private static final int playerBleedDamageValue = 1;
    private static final double playerCriticalDamageValue = 1.5;

    private static int currentRow = 2;
    private static int currentColumn = 1;

    JPanel statsPanel;
    JButton playerAttackButton = new JButton("Attack");
    JLabel gameTextLabel;
    JLabel playerTextLabel;
    private static final ArrayList<String> statusEffect = new ArrayList<>();
    int playerHealth = player.getHealth();
    int monsterHealth;
    int bossMonsterHealth;

    private static String status = "";

    private boolean inCombat = false;

    Room startRoom;
    Heal healPlayer = new Heal();
    private int maxHeal = 3;
    private int healCount = 0;
    private static final int MAX_LIVES = 3;
    private int remainingLives = MAX_LIVES;
    private int deaths = 0;

    private LevelUpEnemies levelUpEnemies = new LevelUpEnemies();

    private static String REGULAR_ENEMY_FILE = "Orc.txt";

    private String BOSS_ENEMY_FILE = "Level1Boss.txt";

    private static String INITIAL_ENEMY_FILE = "Orc.txt";
    private static String INITIAL_BOSS_FILE = "Level1Boss.txt";

    private static String WEAPON_FILE = "sword.txt";

    Monsters updatedBoss = levelUpEnemies.reloadEnemyStats(BOSS_ENEMY_FILE);


    public GameGUI() {
        Inventory inventory = new Inventory();
        inventory.addItemFromFile(WEAPON_FILE);
        resetEnemiesToInitialState();
        String playerName = JOptionPane.showInputDialog("Enter your name:");
        while (playerName != null && playerName.length() > 20) {
            playerName = JOptionPane.showInputDialog("Name is too long! Please enter a name with a maximum of 20 characters:");
        }
        if (playerName != null && !playerName.isEmpty()) {
            player.setName(playerName);
        } else {
            player.setName("Default Name");
        }
        dungeon = new Dungeon(3, 3);

        setTitle("Varr Dungeon");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Prevents default close operation

        // Add window listener to handle closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close(); // Call close method when window is closing
            }
        });

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new BorderLayout());
        Border leftBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        leftPanel.setBorder(BorderFactory.createTitledBorder(leftBorder, "Stats"));


        // STATS
        statsPanel = new JPanel(new GridLayout(10, 1));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addStatsToPanel(statsPanel);

        // INVENTORY
        JPanel inventoryPanel = new JPanel(new GridLayout(1, 1));
        inventoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addInventoryToPanel(inventoryPanel, inventory);

        leftPanel.add(statsPanel, BorderLayout.NORTH);
        leftPanel.add(inventoryPanel, BorderLayout.CENTER);

        // HEAL
        JButton healButton = new JButton("Heal");
        healButton.setPreferredSize(new Dimension(100, 30));
        healButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (player.getHealth() < player.getMaxHealth()) {
                    if (healCount < maxHeal) {
                        healPlayer.healPlayer(player);
                        healCount++;
                        statsPanel.removeAll();
                        statsPanel.revalidate();
                        statsPanel.repaint();
                        addStatsToPanel(statsPanel);

                    } else {
                        if (textArea.getText().isEmpty()) {
                            textArea.setText("You have used all your healing pots.");

                        } else {
                            textArea.setText("");
                            textArea.append("You have used all your healing pots.");

                        }
                        textArea.append("\n");
                    }
                } else {
                    if (textArea.getText().isEmpty()) {
                        textArea.setText("You are already at full health.");

                    } else {
                        textArea.setText("");
                        textArea.append("You are already at full health.");

                    }
                    textArea.append("\n");
                }

            }
        });

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

        // GAME TEXT PANEL
        JPanel gameTextPanel = new JPanel();
        gameTextPanel.setBackground(Color.WHITE);
        gameTextPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        container.add(gameTextPanel, BorderLayout.CENTER);

        // PLAYER TEXT
        playerTextLabel = new JLabel();
        playerTextLabel.setFont(new Font("Serif", Font.BOLD, 21));
        playerTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerTextLabel.setHorizontalAlignment(JLabel.CENTER);
        playerTextLabel.setVerticalAlignment(JLabel.CENTER);
        gameTextPanel.add(playerTextLabel);

        // GAME TEXT
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setPreferredSize(new Dimension(500, 300));
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameTextPanel.add(textArea);

        gameTextLabel = new JLabel(" ");
        gameTextLabel.setFont(new Font("Serif", Font.BOLD, 24));
        gameTextPanel.add(gameTextLabel);

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
                currentRoom = dungeon.getRoom(currentRow,currentColumn);
                textArea.setVisible(true);
                textArea.setText(currentRoom.getDescription());
                playerAttackButton.setVisible(false);
                leftArrow.setVisible(true);
                upArrow.setVisible(true);
                rightArrow.setVisible(true);
                playerTextLabel.setText(" ");
                bossCombat = false;
                bleedCount = 0;
            }
        });

        dungeon = new Dungeon(3, 3);

        // create rooms and set up exits
         startRoom = new Room("You find yourself standing at the entrance of the dungeon.\n" +
                "Dim torchlight flickers against the cold stone walls, casting long shadows across the floor. \n" +
                "The air is thick with a musty scent, and a sense of anticipation hangs in the air as you prepare to embark on your perilous journey.\n\n\n" +
                 "You are in the bottom middle room.\n" +
                 "Your exits are left, up, or right.\n" +
                "Where will you go?", true);

        Room bottomLeft = new Room("The room is small and cramped, with barely enough space to maneuver.\n" +
                "Cobwebs cling to the corners, and the floor is littered with debris from fallen stones.\n" +
                "You can hear the distant echo of dripping water, adding to the eerie atmosphere of the dungeon.\n\n\n" +
                "You are in the bottom left room.\n" +
                "Your exits are up or right.\n" +
                "Where will you go?", true);

        Room bottomRight = new Room("As you enter, a gust of stale air rushes past you, carrying the scent of decay.\n" +
                "The room is shrouded in darkness, illuminated only by faint beams of moonlight filtering in through narrow cracks in the ceiling.\n" +
                "You can sense danger lurking in the shadows, and your instincts tell you to proceed with caution.\n\n\n" +
                "You are in the bottom right room.\n" +
                "Your exits are left or up.\n" +
                "Where will you go?", true);

        Room middleLeft = new Room("This room is larger than the others you've encountered so far, with high ceilings and expansive walls adorned with faded tapestries.\n" +
                "The floor is smooth and uneven, worn down by centuries of footprints.\n" +
                "You can hear the faint sound of footsteps echoing in the distance, hinting at the presence of other creatures within the dungeon\n\n\n"+
                "The door behind you closes, and you realize that there's no going back to the room before.\n\n" +
                "You are in the middle left room.\n" +
                "Your exits are up or right.\n" +
                "Where will you go?", true);


        Room middleMiddle = new Room(" You step into the heart of the dungeon, surrounded by towering pillars of stone that stretch up towards the ceiling.\n" +
                "The air is heavy with a sense of foreboding, and a chill runs down your spine as you gaze into the darkness ahead.\n" +
                "You can't help but feel a sense of trepidation as you press forward into the unknown.\n\n\n"+
                "The door behind you closes, and you realize that there's no going back to the room before\n\n" +
                "You are in the middle middle room.\n" +
                "Your exits are left, up, or right.\n" +
                "Where will you go?", true);

        Room middleRight = new Room("The room is bathed in a soft, golden light that emanates from a crack in the wall.\n" +
                "You can see intricate carvings etched into the stone, depicting scenes of ancient battles and forgotten heroes.\n" +
                "Despite the beauty of the artwork, a sense of unease washes over you, as if the walls themselves are watching your every move..\n\n\n"+
                "The door behind you closes, and you realize that there's no going back to the room before.\n\n" +
                "You are in the middle right room.\n" +
                "Your exits are left or up .\n" +
                "Where will you go?", true);


        Room topLeft = new Room("As you enter, you are greeted by the sound of rushing water and the faint scent of damp earth.\n" +
                " The room is dominated by a large, ornate fountain, its waters shimmering in the dim light.\n" +
                " Moss clings to the walls, adding to the sense of tranquility that permeates the space.\n\n\n"+
                "You are in the top left room.\n" +
                "Your only exit is to the right.\n" +
                "Where will you go?", true);

        Room bossRoom = new Room("You stand on the threshold of the final chamber, your heart pounding in your chest as you prepare to face the ultimate challenge.\n" +
                "The room is vast and cavernous, with towering pillars of stone rising up towards the heavens.\n" +
                " At the center of the chamber stands a massive throne, its occupant cloaked in shadow.\n" +
                "You can feel the raw power emanating from the figure, and you steel yourself for the battle ahead\n\n\n"+
                "The door behind you closes, and you realize that there's no going back to the room before. You feel an eerie presence in the air.\n" +
                "You only hope you can survive whatever is ahead.\n" +
                "You are in the boss's room.\n", true);

        Room topRight = new Room("This room is filled with the sound of chirping birds and rustling leaves, creating a stark contrast to the darkness of the dungeon outside.\n" +
                "Sunlight streams in through a hole in the ceiling, illuminating the space with a warm, golden glow.\n" +
                "You can't help but feel a sense of relief as you bask in the light, if only for a moment.\n\n\n"+
                "You are in the top topRight.\n" +
                "Your only exit is to the left.\n" +
                "Where will you go?", true);


        //Room bossRoom = new Room("", true);

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

        middleMiddle.addExit("north", bossRoom);
        middleMiddle.addExit("west", middleLeft);
        middleMiddle.addExit("east", middleRight);

        middleRight.addExit("north", topRight);
        middleRight.addExit("west", middleMiddle);

        //Top row
        topLeft.addExit("east", bossRoom);
        topRight.addExit("west", bossRoom);


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

        //currentRoom = startRoom;
        currentRoom = dungeon.getRoom(currentRow, currentColumn);
        textArea.setText(currentRoom.getDescription());
        leftArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameTextLabel.setText(" ");

                // this is tracking which room of the array the player is in
                currentColumn -= 1;
                if (currentColumn < 0) {
                    gameTextLabel.setText("You can't go that way.");
                    currentColumn = 0;
                    return;
                }
                else if (currentRow < 0) {
                    gameTextLabel.setText("You can't go that way.");
                    currentRow = 0;
                    return;
                }
                currentRoom = dungeon.getRoom(currentRow, currentColumn);
                textArea.setText(currentRoom.getDescription());
                checkForEncounter(dungeon,currentRoom);
            }
        });

        // Add action listener to up arrow button
        upArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameTextLabel.setText(" ");

                // this is tracking which room of the array the player is in
                currentRow -= 1;
                if (currentRow < 0) {
                    gameTextLabel.setText("You can't go that way.");
                    currentRow = 0;
                    return;
                }
                currentRoom = dungeon.getRoom(currentRow, currentColumn);
                textArea.setText(currentRoom.getDescription());
                checkForEncounter(dungeon,currentRoom);
            }
        });

        // Add action listener to right arrow button
        rightArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameTextLabel.setText(" ");

                // this is tracking which room of the array the player is in
                currentColumn += 1;
                if (currentColumn > 2) {
                    gameTextLabel.setText("You can't go that way.");
                    currentColumn = 2;
                    return;
                }
                else if (currentRow < 0) {
                    gameTextLabel.setText("You can't go that way.");
                    currentRow = 0;
                    return;
                }
                currentRoom = dungeon.getRoom(currentRow, currentColumn);
                textArea.setText(currentRoom.getDescription());
                checkForEncounter(dungeon,currentRoom);
            }
        });

        // MONSTER ENCOUNTER
        //monsterEncounter = new MonsterEncounter(dungeon,currentRoom);

        playerAttackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bossCombat) {
                    bossCombat();
                }
                else {
                    combat();
                }
            }
        });


        // I added this to the arrow panel
        // this can be changed and moved, but set up to test function
        // attack button for player
        playerAttackButton.setPreferredSize(new Dimension(100, 30));
        playerAttackButton.setSize(50, 50);
        playerAttackButton.setForeground(Color.black);
        playerAttackButton.setVisible(false);

        arrowPanel.add(playerAttackButton);
        arrowPanel.add(healButton);
        container.add(arrowPanel, BorderLayout.SOUTH);
        container.add(leftPanel, BorderLayout.WEST);
        arrowPanel.add(loadButton);
        arrowPanel.add(saveButton);
        setVisible(true);

    }


    public static void main(String[] args) {

        // added monster health for testing
        resetEnemiesToInitialState();

        statusEffect.add("bleedHit");
        statusEffect.add("criticalHit");
        statusEffect.add("normalHit");
        statusEffect.add("missHit");


        player.setName("TestName");
        player.setHealth(20);
        player.setMaxHealth(20);
        player.setAttack(10);
        player.setLevel(1);
        player.setExperience(0);
        player.setExpToLevelUp(5);

        GameGUI game = new GameGUI();
        game.resetEnemiesToInitialState();
        new Dungeon(3,3);

    }

    private void addStatsToPanel(JPanel statsPanel) {
        statsPanel.add(new JLabel("Name: " + player.getName()));
        statsPanel.add(new JLabel("Health: " + player.getHealth()));
        statsPanel.add(new JLabel("Attack: " + player.getAttack()));
        statsPanel.add(new JLabel("Level: " + player.getLevel()));
        statsPanel.add(new JLabel("Experience: " + player.getExperience()));
        statsPanel.add(new JLabel("Level Up: " + player.getExpToLevelUp()));
        statsPanel.add(new JLabel("Dungeon Level: " + dungeon.getLevel()));
        statsPanel.add(new JLabel("Potions: " + (maxHeal - healCount)));
        statsPanel.add(new JLabel("Lives: " + (remainingLives)));
        statsPanel.add(new JLabel("Deaths: " + (deaths)));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private void addInventoryToPanel(JPanel inventoryPanel, Inventory inventory) {
        DefaultListModel<Items> model = new DefaultListModel<>();
        for (Items item : inventory.getInventory()) {
            model.addElement(item);
        }
        JList<Items> inventoryList = new JList<>(model);

        // Customize the cell renderer
        inventoryList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Items item = (Items) value;
                label.setText(item.getName() + " (" + item.getRarity() + "), Damage: " + item.getDamage());
                return label;
            }
        });
        inventoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ListSelectionModel selectionModel = inventoryList.getSelectionModel();
                int selectedIndex = selectionModel.getMinSelectionIndex();
                if (selectedIndex >= 0) {
                    Items selectedItem = inventoryList.getModel().getElementAt(selectedIndex);
                    if (selectionModel.isSelectedIndex(selectedIndex)) {
                        if (player.getEquippedItem() == selectedItem) {
                            player.setAttack(player.getAttack() - selectedItem.getDamage());
                            player.setEquippedItem(null);
                            selectionModel.removeSelectionInterval(selectedIndex, selectedIndex); // Deselect the item
                        } else {
                            Items equippedItem = player.getEquippedItem();
                            if (equippedItem != null) {
                                player.setAttack(player.getAttack() - equippedItem.getDamage());
                            }
                            player.setAttack(player.getAttack() + selectedItem.getDamage());
                            player.setEquippedItem(selectedItem);
                            selectionModel.removeSelectionInterval(selectedIndex, selectedIndex); // Deselect the item
                        }
                        statsPanel.removeAll();
                        statsPanel.revalidate();
                        statsPanel.repaint();
                        addStatsToPanel(statsPanel);
                    } else {
                        player.setAttack(player.getAttack() - player.getEquippedItem().getDamage());
                        player.setEquippedItem(selectedItem);
                        player.setAttack(player.getAttack() + selectedItem.getDamage());
                        selectionModel.addSelectionInterval(selectedIndex, selectedIndex); // Select the item
                    }
                }
            }
        });

        inventoryPanel.add(inventoryList);
    }


    // save player attributes/data
    public void saveGame(String fileNameToSave) {

        try {

            // create file
            BufferedWriter saveToFile = new BufferedWriter(new FileWriter(fileNameToSave));

            saveToFile.write(player.getName());
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getHealth()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getAttack()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getLevel()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getExperience()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(player.getExpToLevelUp()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(dungeon.getLevel()));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(healCount));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(currentRow));
            saveToFile.newLine();
            saveToFile.append(String.valueOf(currentColumn));


            gameTextLabel.setText("File saved successfully.");
            gameTextLabel.revalidate();
            gameTextLabel.repaint();
            saveToFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // load player attributes/data
    public void loadData(String fileNameToLoad) {
        try {

            // Read save file
            BufferedReader readLoadFile = new BufferedReader(new FileReader(fileNameToLoad));

            // Can load data from readLoadFile to player/game data variables
            player.setName(readLoadFile.readLine());
            player.setHealth(Integer.parseInt(readLoadFile.readLine()));
            player.setAttack(Integer.parseInt(readLoadFile.readLine()));
            player.setLevel(Integer.parseInt(readLoadFile.readLine()));
            player.setExperience(Integer.parseInt(readLoadFile.readLine()));
            player.setExpToLevelUp(Integer.parseInt(readLoadFile.readLine()));
            dungeon.setLevel(Integer.parseInt(readLoadFile.readLine()));
            healCount = Integer.parseInt(readLoadFile.readLine());
            currentRow = Integer.parseInt(readLoadFile.readLine());
            currentColumn = Integer.parseInt(readLoadFile.readLine());

            gameTextLabel.setText("Load successful.");
            readLoadFile.close();

            statsPanel.removeAll();
            statsPanel.revalidate();
            statsPanel.repaint();
            addStatsToPanel(statsPanel);

        } catch (Exception e) {
            gameTextLabel.setText("There are no files available for load.");
        }
    }

    public void combat() {
        if (inCombat) {
            textArea.setVisible(false);
            monsterHealth = monsters.getEnemyHealth();
            // if bleed is inflicted, player will take damage on their turn
            // player will only take 3 turns of bleed damage
            if (bleed) {
                bleedCount += 1;

                // message to user
                playerTextLabel.setText("You take bleed damage. You hit the " + monsters.getName() + ".");
                playerTextLabel.revalidate();
                playerTextLabel.repaint();

                // damage to monster
                monsters.setEnemyHealth(monsterHealth -= player.getAttack());

                // player bleed damage
                player.setHealth(player.getHealth() - playerBleedDamageValue);

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

            } else {
                playerTextLabel.setText("You hit the " + monsters.getName() + ".");
                playerTextLabel.revalidate();
                playerTextLabel.repaint();

                monsters.setEnemyHealth(monsterHealth -= player.getAttack());
            }
            if (monsters.getEnemyHealth() <= 0) {

                Monsters updatedOrc = levelUpEnemies.reloadEnemyStats(REGULAR_ENEMY_FILE);

                // EXP
                int expDrop = updatedOrc.getExpDrop();
                player.addExp(expDrop);
                // Check if the player has leveled up
                if (player.getExperience() > player.getExpToLevelUp()) {
                    player.setExperience(player.getExperience() - player.getExpToLevelUp());
                    player.levelUp();
                }

                maxHeal = 3;
                healCount = 0;
                statsPanel.removeAll();
                statsPanel.revalidate();
                statsPanel.repaint();
                addStatsToPanel(statsPanel);

                // message to user
                gameTextLabel.setText("You killed " + monsters.getName());
                gameTextLabel.revalidate();
                gameTextLabel.repaint();
                textArea.setVisible(true);
                playerTextLabel.setText("");
                playerTextLabel.revalidate();
                playerTextLabel.repaint();
                playerAttackButton.setVisible(false);
                rightArrow.setVisible(true);
                upArrow.setVisible(true);
                leftArrow.setVisible(true);
                inCombat = false;

                // reset GUI to update player attributes


                return;
            }

            // random number to decide what type of hit monster will do
            Random rng = new Random();
            int selectedStatusEffect = rng.nextInt((4));

            // using random number to select a status type
            // status types are bleedHit, criticalHit, missHit, and normalHit
            status = statusEffect.get(selectedStatusEffect);

            if (status.equals("bleedHit")) {
                // message to user
                gameTextLabel.setText(monsters.getName() + " inflicts a bleeding hit.");
                gameTextLabel.revalidate();
                gameTextLabel.repaint();

                // player takes hit and will take bleed damage on their turn
                player.setHealth(player.getHealth() - monsters.getEnemyAttack());

                // reset GUI to update player attributes
                statsPanel.removeAll();
                statsPanel.revalidate();
                statsPanel.repaint();
                addStatsToPanel(statsPanel);

                // bleed status
                bleed = true;

            } else if (status.equals("criticalHit")) {
                // message to user
                gameTextLabel.setText(monsters.getName() + " inflicts a critical hit.");
                gameTextLabel.revalidate();
                gameTextLabel.repaint();
                player.setHealth(player.getHealth() - (int) (monsters.getEnemyAttack() * playerCriticalDamageValue));

                // reset GUI to update player attributes
                statsPanel.removeAll();
                statsPanel.revalidate();
                statsPanel.repaint();
                addStatsToPanel(statsPanel);

            } else if (status.equals("missHit")) {
                // message to user
                gameTextLabel.setText(monsters.getName() + "'s hit missed.");
                gameTextLabel.revalidate();
                gameTextLabel.repaint();
            } else {
                // message to user
                gameTextLabel.setText(monsters.getName() + " hit you.");
                gameTextLabel.revalidate();

                player.setHealth(player.getHealth() - monsters.getEnemyAttack());

                // reset GUI to update player attributes
                statsPanel.removeAll();
                statsPanel.revalidate();
                statsPanel.repaint();
                addStatsToPanel(statsPanel);
            }

            if (player.getHealth() <= 0) {
                // Decrement remaining lives
                remainingLives--;

                // Check if remaining lives are zero
                if (remainingLives <= 0) {
                    // Game over, ask if the player wants to restart or exit
                    int restartChoice = JOptionPane.showConfirmDialog(null, "You were killed by " + monsters.getName() + ". You have no remaining lives. Do you want to restart the game?", "Game Over", JOptionPane.YES_NO_OPTION);
                    if (restartChoice == JOptionPane.YES_OPTION) {
                        restartGame();
                    } else {
                        // Exit the game
                        System.exit(0);
                    }
                } else {
                    // Prompt the user with a dialog box
                    int choice = JOptionPane.showConfirmDialog(null, "You were killed by " + monsters.getName() + ". Remaining lives: " + remainingLives + ". Do you want to continue?", "Game Over", JOptionPane.YES_NO_OPTION);
                    deaths++;

                    if (choice == JOptionPane.YES_OPTION) {
                        // Player wants to continue, reset player's health
                        player.setHealth(player.getMaxHealth());
                        player.getHealth();
                        statsPanel.removeAll();
                        statsPanel.revalidate();
                        statsPanel.repaint();
                        addStatsToPanel(statsPanel);
                        playerTextLabel.setText("");
                        playerTextLabel.revalidate();
                        playerTextLabel.repaint();
                        // Add any other actions needed to reset the game state
                    } else {
                        // Player doesn't want to continue, perform game over actions
                        player.setHealth(0);
                        statsPanel.removeAll();
                        statsPanel.revalidate();
                        statsPanel.repaint();
                        addStatsToPanel(statsPanel);
                        playerTextLabel.setText("");
                        playerTextLabel.revalidate();
                        playerTextLabel.repaint();
                        gameTextLabel.setText(monsters.getName() + " killed you.\n +" +
                                "Game Over.");
                        gameTextLabel.revalidate();
                        gameTextLabel.repaint();
                        playerAttackButton.setVisible(false);
                        rightArrow.setVisible(true);
                        upArrow.setVisible(true);
                        leftArrow.setVisible(true);
                    }
                }
            }
        }
    }


    public void bossCombat() {
        if (inCombat) {
            textArea.setVisible(false);
            bossMonsterHealth = bossMonster.getEnemyHealth();
            playerHealth = player.getHealth();

            String playerMessage = "";

            // If bleed is inflicted, player will take damage on their turn
            // Player will only take 3 turns of bleed damage
            if (bleed) {
                bleedCount++;

                // Message to user
                // player is bleeding and hit boss message
                playerMessage += "<html><br><br>You take bleed damage.<br>You hit the " + bossMonster.getName() + ".<br>";
                gameTextLabel.setText(playerMessage);

                // Damage to boss monster
                bossMonster.setEnemyHealth(bossMonsterHealth -= player.getAttack());

                // Player bleed damage
                player.setHealth(player.getHealth() - playerBleedDamageValue);

                // Only 3 turns of bleed damage
                if (bleedCount == 3) {
                    bleedCount = 0;
                    bleed = false;
                }

                // Reset player stats in GUI
                statsPanel.removeAll();
                statsPanel.revalidate();
                statsPanel.repaint();
                addStatsToPanel(statsPanel);

            } else {

                // player hits boss message
                playerMessage = "<html><br><br>You hit the " + bossMonster.getName() + ".<br>";
                gameTextLabel.setText(playerMessage);

                bossMonster.setEnemyHealth(bossMonsterHealth -= player.getAttack());
            }

            // Check if the boss monster is defeated
            if (bossMonster.getEnemyHealth() <= 0) {
                // Update player's experience
                int expDrop = updatedBoss.getExpDrop(); // Use updated boss stats
                player.addExp(expDrop);
                player.addExp(expDrop);

                // Check if the player has leveled up
                while (player.getExperience() >= player.getExpToLevelUp()) {
                    player.setExperience(0);
                    player.levelUp();
                }

                // Reset game state
                maxHeal = 3;
                healCount = 0;
                statsPanel.removeAll();
                statsPanel.revalidate();
                statsPanel.repaint();
                addStatsToPanel(statsPanel);
                // Message to user
                playerTextLabel.setText("");
                playerTextLabel.revalidate();
                playerTextLabel.repaint();
                gameTextLabel.setText("You killed " + bossMonster.getName());
                gameTextLabel.revalidate();
                gameTextLabel.repaint();
                playerAttackButton.setVisible(false);
                rightArrow.setVisible(true);
                upArrow.setVisible(true);
                leftArrow.setVisible(true);
                textArea.setVisible(true);
                inCombat = false;
                bossCombat = false;
                remainingLives = MAX_LIVES;
                bleed = false;
                goToNextLevel();
                return;
            }

            // random number to decide what type of hit monster will do
            Random rng = new Random();
            int selectedStatusEffect = rng.nextInt((4));

            // using random number to select a status type
            // status types are bleedHit, criticalHit, missHit, and normalHit
            status = statusEffect.get(selectedStatusEffect);

            String monsterMessage = "";
            if (status.equals("bleedHit")) {
                // message to user
                monsterMessage = bossMonster.getName() + " inflicts a bleeding hit.<br></html>";
                playerMessage += monsterMessage;
                gameTextLabel.setText(playerMessage);

                gameTextLabel.revalidate();
                gameTextLabel.repaint();

                // player takes hit and will take bleed damage on their turn
                player.setHealth(playerHealth -= bossMonster.getEnemyAttack());

                // reset GUI to update player attributes
                statsPanel.removeAll();
                statsPanel.revalidate();
                statsPanel.repaint();
                addStatsToPanel(statsPanel);

                // bleed status
                bleed = true;

            } else if (status.equals("criticalHit")) {
                // message to user
                monsterMessage = bossMonster.getName() + " inflicts a critical hit.</html>";
                playerMessage += monsterMessage;
                gameTextLabel.setText(playerMessage);

                gameTextLabel.revalidate();
                gameTextLabel.repaint();
                player.setHealth(playerHealth -= (int) (bossMonster.getEnemyAttack() * playerCriticalDamageValue));

                // reset GUI to update player attributes
                statsPanel.removeAll();
                statsPanel.revalidate();
                statsPanel.repaint();
                addStatsToPanel(statsPanel);

            } else if (status.equals("missHit")) {
                // message to user
                monsterMessage = bossMonster.getName() + "'s hit missed you.</html>";
                playerMessage += monsterMessage;

                gameTextLabel.revalidate();
                gameTextLabel.repaint();
            } else {
                // message to user
                monsterMessage = bossMonster.getName() + " hits you.</html>";
                playerMessage += monsterMessage;
                gameTextLabel.setText(playerMessage);

                gameTextLabel.revalidate();
                gameTextLabel.repaint();

                player.setHealth(playerHealth -= bossMonster.getEnemyAttack());

                // reset GUI to update player attributes
                statsPanel.removeAll();
                statsPanel.revalidate();
                statsPanel.repaint();
                addStatsToPanel(statsPanel);
            }


            if (player.getHealth() <= 0) {
                // Decrement remaining lives
                remainingLives--;

                // Check if remaining lives are zero
                if (remainingLives <= 0) {
                    // Game over, ask if the player wants to restart or exit
                    int restartChoice = JOptionPane.showConfirmDialog(null, "You were killed by " + bossMonster.getName() + ". You have no remaining lives. Do you want to restart the game?", "Game Over", JOptionPane.YES_NO_OPTION);
                    if (restartChoice == JOptionPane.YES_OPTION) {
                        restartGame();
                    } else {
                        // Exit the game
                        System.exit(0);
                    }
                } else {
                    // Prompt the user with a dialog box
                    int choice = JOptionPane.showConfirmDialog(null, "You were killed by " + bossMonster.getName() + ". Remaining lives: " + remainingLives + ". Do you want to continue?", "Game Over", JOptionPane.YES_NO_OPTION);
                    deaths++;

                    if (choice == JOptionPane.YES_OPTION) {
                        // Player wants to continue, reset player's health
                        player.setHealth(player.getMaxHealth());
                        player.getHealth();
                        statsPanel.removeAll();
                        statsPanel.revalidate();
                        statsPanel.repaint();
                        addStatsToPanel(statsPanel);
                        playerTextLabel.setText("");
                        playerTextLabel.revalidate();
                        playerTextLabel.repaint();

                    } else {
                        // Player doesn't want to continue, perform game over actions
                        player.setHealth(0);
                        statsPanel.removeAll();
                        statsPanel.revalidate();
                        statsPanel.repaint();
                        addStatsToPanel(statsPanel);
                        playerTextLabel.setText("");
                        playerTextLabel.revalidate();
                        playerTextLabel.repaint();
                        gameTextLabel.setText(monsters.getName() + " killed you.\n +" +
                                "Game Over.");
                        gameTextLabel.revalidate();
                        gameTextLabel.repaint();
                        playerAttackButton.setVisible(false);
                        rightArrow.setVisible(true);
                        upArrow.setVisible(true);
                        leftArrow.setVisible(true);
                    }
                }
            }
        }
    }


    public void goToNextLevel() {
        int level = dungeon.getLevel();
        level++;
        levelUpEnemies.increaseEnemyLevel();
        levelUpEnemies.reloadEnemyStats(String.valueOf(REGULAR_ENEMY_FILE)); // Update monsters with new stats
        levelUpEnemies.reloadEnemyStats(String.valueOf(BOSS_ENEMY_FILE)); // Update monsters with new stats
        if (dungeon != null) {
            dungeon.setLevel(level);

            statsPanel.removeAll();
            statsPanel.revalidate();
            statsPanel.repaint();
            addStatsToPanel(statsPanel);
        }

        gameTextLabel.setText("You have reached level " + dungeon.getLevel() + ".");

        currentRow = 2;
        currentColumn = 1;
        currentRoom = dungeon.getRoom(currentRow,currentColumn);
        textArea.setText(currentRoom.getDescription());
    }


    public void checkForEncounter(Dungeon dungeon, Room currentRoom) {
        this.dungeon = dungeon;
        this.currentRoom = currentRoom;
        int encounterRng = new Random().nextInt(2);
        if (currentRoom == dungeon.getRoom(0,1)) {
            inCombat = true;
            loadBoss();
            bossCombat = true;
            gameTextLabel.setText("<html>You have reach the boss of this level.<br> Prepare to battle " + bossMonster.getName() + ".</html>");
            playerAttackButton.setVisible(true);
            rightArrow.setVisible(false);
            leftArrow.setVisible(false);
            upArrow.setVisible(false);

        } else {
            if (encounterRng == 0) {
                inCombat = true;
                loadMonster();
                monsterCombat = true;
                gameTextLabel.setText("You ran into a stinky " + monsters.getName() + ".\n");
                playerAttackButton.setVisible(true);
                rightArrow.setVisible(false);
                leftArrow.setVisible(false);
                upArrow.setVisible(false);
                textArea.setVisible(false);

            }
        }

    }

    public void loadMonster() {
        try {
            BufferedReader monsterLoader = new BufferedReader(new FileReader(REGULAR_ENEMY_FILE));
            monsters.setName(monsterLoader.readLine());
            monsters.setLevel(monsterLoader.readLine());
            monsters.setEnemyHealth(Integer.parseInt(monsterLoader.readLine()));
            monsters.setEnemyAttack(Integer.parseInt(monsterLoader.readLine()));
            monsters.setExpDrop(Integer.parseInt(monsterLoader.readLine()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadBoss() {
        try {
            BufferedReader monsterLoader = new BufferedReader(new FileReader(BOSS_ENEMY_FILE));
            bossMonster.setName(monsterLoader.readLine());
            bossMonster.setLevel(monsterLoader.readLine());
            bossMonster.setEnemyHealth(Integer.parseInt(monsterLoader.readLine()));
            bossMonster.setEnemyAttack(Integer.parseInt(monsterLoader.readLine()));
            bossMonster.setExpDrop(Integer.parseInt(monsterLoader.readLine()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        // Write the original regular enemy stats back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REGULAR_ENEMY_FILE))) {
            writer.write("Orc\n");
            writer.write("1\n");
            writer.write("10\n");
            writer.write("2\n");
            writer.write("2\n");
        } catch (IOException e) {
            System.out.println("Error writing to regular enemy file: " + e.getMessage());
        }

        // Write the original boss stats back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOSS_ENEMY_FILE))) {
            writer.write("King Orc\n");
            writer.write("1\n");
            writer.write("20\n");
            writer.write("5\n");
            writer.write("5\n");
        } catch (IOException e) {
            System.out.println("Error writing to boss file: " + e.getMessage());
        }
    }

    private static void resetEnemiesToInitialState() {
        // Initialize initial stats for Orc
        initialOrc = Monsters.readMonstersFromFile(INITIAL_ENEMY_FILE);
        initialOrc.setName("Orc");
        initialOrc.setLevel("1"); // Assuming level 1
        initialOrc.setEnemyHealth(10); // Initial health 10
        initialOrc.setEnemyAttack(10); // Initial attack 10
        initialOrc.setExpDrop(2); // Initial exp drop 2

        // Initialize initial stats for Boss
        initialBoss = Monsters.readMonstersFromFile(INITIAL_BOSS_FILE);
        initialBoss.setName("Level1Boss");
        initialBoss.setLevel("1"); // Assuming level 1
        initialBoss.setEnemyHealth(20); // Initial health 100
        initialBoss.setEnemyAttack(10); // Initial attack 50
        initialBoss.setExpDrop(5); // Initial exp drop 50
    }

    private void restartGame() {
        // Reset the player's stats to their initial state
        player.setHealth(player.getMaxHealth());
        player.setAttack(10);
        player.setExperience(0);
        player.setLevel(1);
        inCombat = false;
        remainingLives = MAX_LIVES;
        deaths = 0;
        currentRoom = startRoom;
        textArea.setText(currentRoom.getDescription());
        statsPanel.removeAll();
        statsPanel.revalidate();
        statsPanel.repaint();
        addStatsToPanel(statsPanel);
        textArea.setVisible(true);
        playerTextLabel.setVisible(true);
        playerTextLabel.setText("");
        playerTextLabel.revalidate();
        playerTextLabel.repaint();
        gameTextLabel.setText(" ");
        gameTextLabel.revalidate();
        gameTextLabel.repaint();
        playerAttackButton.setVisible(false);
        rightArrow.setVisible(true);
        upArrow.setVisible(true);
        leftArrow.setVisible(true);

    }
    public static int getWeaponDamage(String weaponFile) {
        int damage = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(WEAPON_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    damage = Integer.parseInt(parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading weapon file: " + e.getMessage());
        }
        return damage;
    }
}