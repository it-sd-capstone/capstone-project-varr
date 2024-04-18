package GameGUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import edu.cvtc.varr.Character;
import edu.cvtc.varr.Dungeon;
import edu.cvtc.varr.Items;
import edu.cvtc.varr.Monsters;

public class GameGUI extends JFrame {
    private Character player = new Character();

    private Dungeon dungeon = new Dungeon();
    private Items items = new Items();
    private Monsters monsters = new Monsters();
    private ArrayList<Items> inventory = new ArrayList<>();


    public GameGUI() {
        player.setHealth(100);
        player.setMana(50);
        player.setAttack(10);
        player.setArmor(10);
        player.setDefense(10);
        player.setGold(0);
        player.setLevel(1);
        player.setExperience(0);

        Map<Integer, String> dungeonLevels = new HashMap<>();
        dungeonLevels.put(1, "Dwellers of Grave");

        setTitle("Varr Dungeon");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // Create a panel for displaying the stats and inventory button at the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Border bottomBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        bottomPanel.setBorder(BorderFactory.createTitledBorder(bottomBorder));

        Items weapon = createWeapon();
        Items armor = createArmor();
        Items consumables = createConsumables();


        inventory.add(weapon);
        inventory.add(armor);
        inventory.add(consumables);

        // Create a button for displaying stats
        JButton statsButton = new JButton("Stats");
        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStats();
            }
        });

        // Create a button for displaying inventory
        JButton inventoryButton = new JButton("Inventory");
        inventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInventory();
            }
        });

        // Create a button for displaying consumables
        JButton consumablesButton = new JButton("Consumables");
        consumablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConsumables();
            }
        });

        // Add buttons to the bottom panel
        bottomPanel.add(statsButton);
        JPanel inventoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Border inventoryBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        inventoryPanel.setBorder(BorderFactory.createTitledBorder(inventoryBorder));
        bottomPanel.add(inventoryButton);
        container.add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GameGUI();
    }

    private Items createWeapon() {
        Items weapon = new Items();
        weapon.setName("Sword");
        weapon.setRarity("Common");
        weapon.setType("Sword");
        return weapon;
    }

    private Items createArmor() {
        Items armor = new Items();
        armor.setName("Leather Armor");
        armor.setRarity("Common");
        armor.setType("Armor");
        return armor;
    }

    private Items createConsumables() {
        Items consumables = new Items();
        consumables.setName("Potion");
        consumables.setRarity("Common");
        consumables.setType("Consumables");
        return consumables;
    }

    private void showInventory() {
        StringBuilder inventoryMessage = new StringBuilder();
        inventoryMessage.append("Inventory:\n");
        for (Items item : inventory) {
            inventoryMessage.append(item.getRarity()).append(" ").append(item.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(null, inventoryMessage.toString(), "Inventory", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showConsumables() {
        StringBuilder consumablesMessage = new StringBuilder();
        consumablesMessage.append("Consumables:\n");
        for (Items item : inventory) {
            if (item.getType().equals("Consumables")) {
                consumablesMessage.append(item.getRarity()).append(" ").append(item.getName()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, consumablesMessage.toString(), "Consumables", JOptionPane.INFORMATION_MESSAGE);
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
    public static void saveGame() {
        // This could be made a path if we have a folder for saves
        // Comments these out, so we don't create files
        //File saveFile = new File("saveFile");

        // Can do if (saveFile.createNewFile()) to check of the file
        // has already been created

        // Do we want to overwrite files??
        try {
            // Comments these out, so we don't create files
            //BufferedWriter saveToFile = new BufferedWriter(new FileWriter("saveFile"));

            // Might have to update player data from private to public, or
            // we will need to figure a way to link up the data we want to save

            // For example
            //saveToFile.write(player.getHealth());
            //saveToFile.append(player.getExperience());

            // System.out.println("File Saved.")
            //saveToFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This is the outline for the loadGame method
    // Will need to update to save our data

    // May need to serialize data
    public static void loadData() {
        try {

            //BufferedReader readLoadFile = new BufferedReader(new FileReader("saveFile"));

            // Can load data from readLoadFile to player/game data variables

            //player.setHealth(Integer.parseInt(readLoadFile.readLine()));

            // readLoadFile.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}