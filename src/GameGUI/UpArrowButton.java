package GameGUI;

import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpArrowButton extends JButton {
    public UpArrowButton(){
        this.setIcon(new ImageIcon("images/upArrow.jpg"));
//        setBorder(null);
//        setContentAreaFilled(false);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

}
