package GameGUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.BorderLayout;
<<<<<<< HEAD
import java.awt.Dimension;
=======
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
>>>>>>> 1aba4cbccf8ed964e7c61315025930dd7b52f1e3
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.cvtc.varr.Character;
import edu.cvtc.varr.Dungeon;
import edu.cvtc.varr.Items;
import edu.cvtc.varr.Monsters;

public class GameGUI extends JFrame {
    private Character player = new Character();

    private Dungeon dungeon = new Dungeon();

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

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new BorderLayout());
        Border leftBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        leftPanel.setBorder(BorderFactory.createTitledBorder(leftBorder, "Stats"));

        JPanel statsPanel = new JPanel(new GridLayout(9, 1));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addStatsToPanel(statsPanel);

        JPanel inventoryPanel = new JPanel(new GridLayout(1, 1));
        inventoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Inventory inventory = new Inventory();
        addInventoryToPanel(inventoryPanel, inventory);


        leftPanel.add(statsPanel, BorderLayout.NORTH);
        leftPanel.add(inventoryPanel, BorderLayout.CENTER);

        UpArrowButton upArrowButton = new UpArrowButton();

        upArrowButton.setPreferredSize(new Dimension(1, 1));
        JPanel directionPanel = new JPanel(new GridLayout(1, 1));
        directionPanel.add(upArrowButton);

        container.add(leftPanel, BorderLayout.WEST);
        container.add(directionPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
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


<<<<<<< HEAD
=======
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
>>>>>>> 1aba4cbccf8ed964e7c61315025930dd7b52f1e3
}