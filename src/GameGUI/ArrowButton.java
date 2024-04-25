package GameGUI;
import javax.swing.*;
import java.awt.*;


    public class ArrowButton extends JButton {
        private int size;
        private int x;
        private int y;
        private int angle;

        public ArrowButton(int size, int x, int y, int angle) {
            this.size = size;
            this.x = x;
            this.y = y;
            this.angle = angle;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setPreferredSize(new Dimension(size, size));
            setForeground(Color.WHITE);

        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.BLACK); // set the foreground color to black
            int arrowX = x + size / 2;
            int arrowY = y + size / 2;
            double arrowFactor = 0.5; // reduce the size of the arrowhead
            int[] xPoints = {arrowX - (int) (size * arrowFactor * Math.cos((angle - 30) * Math.PI / 180)), arrowX, arrowX - (int) (size * arrowFactor * Math.cos((angle + 30) * Math.PI / 180))};
            int[] yPoints = {arrowY - (int) (size * arrowFactor * Math.sin((angle - 30) * Math.PI / 180)), arrowY, arrowY - (int) (size * arrowFactor * Math.sin((angle + 30) * Math.PI / 180))};
            g2d.drawPolygon(xPoints, yPoints, 3);
        }
    }