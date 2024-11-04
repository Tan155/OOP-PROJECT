import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends GameObject{
	
	private Handler handler;
	private BufferedImage Enemy_image;
	Random r = new Random();
	int choose = 0;
	int hp = 100;
	int score = 0;
	public Enemy(int x, int y, ID id,Handler handler, SpriteSheet ss) {
		super(x, y, id, ss);
		this.handler = handler;
		
		Enemy_image = ss.grabImage(1, 1, 32, 32);
	}
	public int getHp() {
		return hp;
	}
	
	@Override
	public void tick() {
		x += velX;
		y += velY;
		
		choose = r.nextInt(10);
		
		for (int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Block) {
				if(getBoundsBig().intersects(tempObject.getBounds())) {
					x += (velX * 6) * -1;
    				y += (velX * 6) * -1;
					velX *= -1;
					velY *= -1;
				}else if(choose == 0) {
					velX = (r.nextInt(2 - -2) + -2);
					velY = (r.nextInt(2 - -2) + -2);
				}
			}
			
			if(tempObject.getId() == ID.Bullet) {
				if(getBounds().intersects(tempObject.getBounds())) {
					hp -= 50;
					handler.removeObject(tempObject);
				}
			}
		}
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Enemy_image, x,y,null);
		
			
	}

	@Override
	public Rectangle getBounds() {	
		return new Rectangle(x, y, 32, 32);
	}
	
	public Rectangle getBoundsBig() {	
		return new Rectangle(x - 16, y - 16, 64, 64);
	}

}
