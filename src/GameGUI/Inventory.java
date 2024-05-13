package GameGUI;

import edu.cvtc.varr.Items;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Inventory extends JButton {

    private static final String ITEMS_FILE = "sword.txt";

    ArrayList<Items> inventory = new ArrayList<>();

    public Inventory() {
        super("Inventory");
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInventory();
            }
        });
    }

    public void addItemFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String rarity = br.readLine();
            String name = br.readLine();
            int damage = Integer.parseInt(br.readLine());
            Items item = new Items(rarity, name, damage);
            inventory.add(item);
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
            e.printStackTrace();
        }
    }

    public void showInventory() {
        // Clear the inventory before populating it
        getInventory().clear();

        // Add items to the inventory
        addItemFromFile(ITEMS_FILE);


        // Display the inventory in a dialog box
        StringBuilder inventoryString = new StringBuilder();
        for (Items item : getInventory()) {
            inventoryString.append(item.getName()).append(" (").append(item.getRarity()).append("), Damage: ").append(item.getDamage()).append("\n");
        }
        JOptionPane.showMessageDialog(this, inventoryString.toString(), "Inventory", JOptionPane.PLAIN_MESSAGE);
    }

    public ArrayList<Items> getInventory() {
        return inventory;
    }
}
