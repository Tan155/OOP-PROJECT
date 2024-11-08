import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Block extends GameObject{
	private BufferedImage block_image;
	 private BufferedImage Block_image;
    public Block(int x, int y, ID id, SpriteSheet ss) {
        super(x, y, id, ss);
        Block_image = ss.grabImage(1, 1, 32, 32);
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
    	g.drawImage(Block_image, x, y, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,32,32);
    }
    
}