import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Image;



public class Menu extends MouseAdapter {
	
    private Game game;

    public Menu(Game game) {
        this.game = game;
    }

    public void render(Graphics g) {
        // Set the background color
        g.setColor(Color.gray);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        // Draw the title
        g.setFont(new Font("Algerian", Font.BOLD, 50));
        g.setColor(Color.white);
        g.drawString("Wizard VS Enemy", Game.WIDTH / 2 - 200, 100);

        // Draw the start button
        g.setFont(new Font("Algerian", Font.BOLD, 30));
        g.setColor(Color.white);
        g.drawRect(Game.WIDTH / 2 - 100, 150, 200, 64);
        g.drawString("Start", Game.WIDTH / 2 - 50, 190);

        // Draw the exit button
        g.drawRect(Game.WIDTH / 2 - 100, 250, 200, 64);
        g.drawString("Exit", Game.WIDTH / 2 - 35, 290);
    }

    public void mousePressed(MouseEvent e) {
        if (game.isInMenu()) {
            int mx = e.getX();
            int my = e.getY();

            // Start button
            if (mouseOver(mx, my, Game.WIDTH / 2 - 100, 150, 200, 64)) {
                selectMap();
            }

            // Exit button
            if (mouseOver(mx, my, Game.WIDTH / 2 - 100, 250, 200, 64)) {
                System.exit(0);
            }
        }
    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        return mx > x && mx < x + width && my > y && my < y + height;
    }

    private void selectMap() {
        String[] options = {"Map 1", "Map 2", "Map 3"};
        int choice = JOptionPane.showOptionDialog(null, "Select a Map", "Map Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            game.setLevel("/Wizard_level.png");
        } else if (choice == 1) {
            game.setLevel("/Wizard_level_2.png"); // Add your second map here
        } else if (choice == 2) {
            game.setLevel("/Wizard_level_3.png"); // Add your third map here
        }

        game.startGame();
        game.removeMouseListener(this);
    }
}