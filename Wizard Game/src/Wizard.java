import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Wizard extends GameObject{
    Handler handler;
    Game game;
    
    private BufferedImage Wizard_image;
    
    public Wizard(int x, int y, ID id, Handler handler,Game game, SpriteSheet ss) {
        super(x, y, id, ss);
        this.handler = handler;
        this.game = game;
        
        Wizard_image = ss.grabImage(1, 1, 32, 32);
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;
        
        collision();
       
        //Movement
        if(handler.isUp()) velY = -5;
        else if (!handler.isDown()) velY = 0;
        
        if(handler.isDown()) velY = 5;
        else if (!handler.isUp()) velY = 0;
        
        if(handler.isRight()) velX = 5;
        else if (!handler.isLeft()) velX = 0;
        
        if(handler.isLeft()) velX = -5;
        else if(!handler.isRight()) velX = 0;
    }
    
    private void collision() {
    	for (int i = 0; i < handler.object.size(); i++) {
    		
    		GameObject tempObject = handler.object.get(i);
    		
    		if(tempObject.getId() == ID.Block) {
    			
    			if(getBounds().intersects(tempObject.getBounds())) {
    				x += velX * -1;
    				y += velY * -1;
    			}
    		}
    		
    		if(tempObject.getId() == ID.Crate) {
    			
    			if(getBounds().intersects(tempObject.getBounds())) {
    				game.ammo += 10;
    				handler.removeObject(tempObject);
    			}
    		}
    	}
    }

    @Override
    public void render(Graphics g) {
       g.drawImage(Wizard_image, x, y, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,32,48);
    }
    
}