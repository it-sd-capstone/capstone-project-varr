package GameGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import edu.cvtc.varr.Items;


public class Inventory extends JButton {

    ArrayList<Items> inventory = new ArrayList<>();
    private CreateCharacterItems items = new CreateCharacterItems();

    public Inventory() {
        super("Inventory");
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInventory();
            }
        });
    }

    public ArrayList<Items> showInventory() {
        Items sword = items.CreateCharacterItems("Common", "Sword");
        inventory.add(sword);
        return inventory;
    }
}