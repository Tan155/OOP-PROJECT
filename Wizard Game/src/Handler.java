
import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
    LinkedList<GameObject> object = new LinkedList<GameObject>();
    private boolean up = false, down = false, right = false, left = false;
    public void tick(){
        for (int i = 0; i < object.size(); i++){
            GameObject tempObject = object.get(i);
            
            tempObject.tick();
        }
    }
    
    // up
    public void setUp(boolean up){
        this.up = up;
    }
    public boolean getUp(){
        return this.up;
    }
    public boolean isUp(){
        return up;
    }
    
    //down
    public void setDown(boolean down){
        this.down = down;
    }
    public boolean getDown(){
        return this.down;
    }
    public boolean isDown(){
        return down;
    }
    
    //right
    public void setRight(boolean right){
        this.right = right;
    }
    public boolean getRight(){
        return this.right;
    }
    public boolean isRight(){
        return right;
    }
    
    //left
    public void setLeft(boolean left){
        this.left = left;
    }
    public boolean getLeft(){
        return this.left;
    }
    public boolean isLeft(){
        return left;
    }
    
    
    public void render(Graphics g){
        for (int i = 0; i < object.size(); i++){
            GameObject tempObject = object.get(i);
            
            tempObject.render(g);
        }
    }
    
    //add
    public void addObject(GameObject tempObject){
        object.add(tempObject);
    }
    
    //remove
    public void removeObject(GameObject tempObject){
        object.remove(tempObject);
    }
}