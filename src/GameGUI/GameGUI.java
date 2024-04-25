package GameGUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
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
    private ArrowButton leftArrow;
    private ArrowButton upArrow;
    private ArrowButton rightArrow;
    private final int PADDING = 50;



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
        leftPanel.setPreferredSize(new Dimension(150 + PADDING, getHeight()));

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

        leftArrow = new ArrowButton(50, 90, 350, 180);
        upArrow = new ArrowButton(50, 45, 300, 270);
        rightArrow = new ArrowButton(50, 0, 350, 0);
        JPanel arrowPanel = new JPanel(new GridLayout(1, 3));

        Border arrowBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        arrowPanel.setBorder(BorderFactory.createTitledBorder(arrowBorder));
        arrowPanel.setPreferredSize(new Dimension(150, 150));

        arrowPanel.add(leftArrow);
        arrowPanel.add(upArrow);
        arrowPanel.add(rightArrow);

        container.add(leftPanel, BorderLayout.WEST);
        container.add(arrowPanel, BorderLayout.CENTER);

        // Add action listeners to arrow buttons
        leftArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {System.out.println("Left arrow clicked");
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