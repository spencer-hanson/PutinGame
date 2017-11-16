package menu;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;

public class Entity {
	private int x;
	private int y;
	private int vx;
	private int vy;
	
	private int height;
	private int width;
	private String name;
	
	private Rectangle bounds;
	private Image image;
	
	private boolean player = false;
	private boolean flip = false;
	
	public Entity(int x, int y, String image, boolean player) {
		name = image;
		image = image + ".png";
		this.player = player;
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = 0;
		this.image = Toolkit.getDefaultToolkit().getImage(getURL(image));
		
		this.width = 0;
		this.height = 0;
		this.bounds = new Rectangle(this.x, this.y, this.width, this.height);
	}
	
	public void update() {
		this.bounds = new Rectangle(this.x, this.y, this.width, this.height);
		this.width = image.getWidth(null);
		this.height = image.getHeight(null);
		if(x + width > Main.WIDTH && vx > 0) {
			return;
		}
		
		if(y + height > Main.HEIGHT && vy > 0) { 
			return;
		}
		
		if(y < 0 && vy < 0) {
			return;
		}
		
		if(x < 0 && vx < 0) {
			return;
		}
		this.x += this.vx;
		this.y += this.vy;
	}
	
	public void setVX(int vx) {
		this.vx = vx;
	}
	
	public void setVY(int vy) {
		this.vy = vy;
	}

	public void addX(int x) {
		this.x += x;
	}
	
	public void addY(int y) {
		this.y += y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}

	public void paint(Graphics2D g2d) {
		if(flip) {
			g2d.drawImage(image, x + width, y, -width, height, null);
		} else {
			g2d.drawImage(image, x, y, null);	
		}
		if(!player) {
			g2d.setColor(Color.black);
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 25));
			g2d.drawString(name, x + width/2, y);
		}
	}

	
	public void flip(boolean flip) {
		this.flip = flip;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	private URL getURL(String filename) { 
        URL url = null; 
        try { 
            url = this.getClass().getResource(filename); 
        } catch(Exception e) { 
              
        } 
        return url; 
    } 
}
