import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable {
    public static final int WIDTH = 1000, HEIGHT = 563;
    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;
    private Camera camera;
    private Menu menu;
    private boolean inMenu = true;
    private SpriteSheet ss;
    private SpriteSheet ss_wall;
    private SpriteSheet ss_wizard;
    private SpriteSheet ss_enemy;
    private SpriteSheet ss_crate;

    private BufferedImage level = null;
    private BufferedImage sprite_sheet = null;
    private BufferedImage sprite_sheet_wall = null;
    private BufferedImage sprite_sheet_wizard = null;
    private BufferedImage sprite_sheet_enemy = null;
    private BufferedImage sprite_sheet_crate = null;
    private BufferedImage floor = null;
    
    public int ammo = 50;
    private int timeRemaining = 50; // 3 minutes in seconds
    private int score = 0;

    public Game() {
        new ProjectGame(WIDTH, HEIGHT, "Wizard VS Enemy", this);
        handler = new Handler();
        camera = new Camera(0, 0);
        menu = new Menu(this);

        this.addMouseListener(menu);
        this.addKeyListener(new KeyInput(handler));

        BufferedImageloader loader = new BufferedImageloader();
        sprite_sheet = loader.loadImage("/Tiles.png");
        sprite_sheet_wizard = loader.loadImage("/AnimationSheet_Character.png");
        sprite_sheet_enemy = loader.loadImage("/enemy01.png");
        sprite_sheet_crate = loader.loadImage("/items1.png");
        sprite_sheet_wall = loader.loadImage("/Map01.png");

        ss = new SpriteSheet(sprite_sheet);
        ss_wizard = new SpriteSheet(sprite_sheet_wizard);
        ss_enemy = new SpriteSheet(sprite_sheet_enemy);
        ss_crate = new SpriteSheet(sprite_sheet_crate);
        ss_wall = new SpriteSheet(sprite_sheet_wall);
        floor = ss.grabImage(8, 1, 32, 32);
        this.addMouseListener(new MouseInput(handler, camera, this, ss));
        start();
    }

    private void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                updates++;
                delta--;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                timeRemaining--; // Decrease time every second
                System.out.println("FPS: " + frames + " TICKS: " + updates);
                frames = 0;
                updates = 0;
            }

            if (timeRemaining <= -1) {
                // End the game when the time runs out
//                inMenu = true;
                System.out.println("Time's up! Game Over.");
                stop();
            }
        }
        stop();
    }

    public void tick() {
        if (!inMenu) {
            handler.tick();
            for (int i = 0; i < handler.object.size(); i++) {
                GameObject tempObject = handler.object.get(i);
                if (tempObject.getId() == ID.Player) {
                    camera.tick(tempObject);
                }
                // Check for collision between Wizard and Enemy
                if (tempObject.getId() == ID.Player) {
                    for (int j = 0; j < handler.object.size(); j++) {
                        GameObject otherObject = handler.object.get(j);
                        if (otherObject.getId() == ID.Enemy && tempObject.getBounds().intersects(otherObject.getBounds())) {
                            timeRemaining -= 5; // Decrease time by 5 seconds if collision occurs
                        }
                    }
                }
                // Check if enemy is defeated
                if (tempObject.getId() == ID.Enemy) {
                    Enemy enemy = (Enemy) tempObject;
                    if (enemy.getHp() <= 0) {
                        handler.removeObject(tempObject);
                        score += 5; // Increase score by 5 when an enemy is defeated
                    }
                }
            }
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        if (inMenu) {
            menu.render(g);
        } else {
            g.setColor(Color.black);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g2d.translate(-camera.getX(), -camera.getY());

            for (int xx = 0; xx < 30 * 72; xx += 32) {
                for (int yy = 0; yy < 30 * 72; yy += 32) {
                    g.drawImage(floor, xx, yy, null);
                }
            }

            handler.render(g);
            g2d.translate(camera.getX(), camera.getY());

            // Stamina display
            g.setColor(Color.white);
            g.drawString("Stamina: " + ammo, 5, 50);
            
            // Score display
            g.drawString("Score: " + score, 5, 70);

            // Time remaining display
            
            g.setFont(new Font("Calibri", Font.BOLD, 20));
            g.drawString("Time Remaining: " + timeRemaining + " seconds", getWidth() / 2 - 100, 30);

            // Game state messages
            if (timeRemaining <= 0) {
                g.setColor(Color.red);
                g.setFont(new Font("Algerian", Font.BOLD, 50));
                g.drawString("Game Over!", WIDTH / 2 - 150, HEIGHT / 2);
            } else if (enemiesRemaining() <= 0) {
                g.setColor(Color.green);
                g.setFont(new Font("Algerian", Font.BOLD, 50));
                g.drawString("You Win", WIDTH / 2 - 150, HEIGHT / 2);
            }
        }
        g.dispose();
        bs.show();
    }

    public void startGame() {
        inMenu = false;
        loadLevel(level);
    }

    public void setLevel(String path) {
        BufferedImageloader loader = new BufferedImageloader();
        level = loader.loadImage(path);
    }

    private void loadLevel(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                // Block
                if (red == 255) {
                    handler.addObject(new Block(xx * 32, yy * 32, ID.Block, ss_wall));
                }

                // Wizard
                if (blue == 255 && green == 0) {
                    handler.addObject(new Wizard(xx * 32, yy * 32, ID.Player, handler, this, ss_wizard));
                }

                // Enemy
                if (green == 255 && blue == 0) {
                    handler.addObject(new Enemy(xx * 32, yy * 32, ID.Enemy, handler, ss_enemy));
                }
                // Crate
                if (green == 255 && blue == 255) {
                    handler.addObject(new Crate(xx * 32, yy * 32, ID.Crate, ss_crate));
                }
            }
        }
    }

    public boolean isInMenu() {
        return inMenu;
    }

    public int enemiesRemaining() {
        return (int) handler.object.stream().filter(obj -> obj.getId() == ID.Enemy).count();
    }

    public static void main(String[] args) {
        new Game();
    }
}
