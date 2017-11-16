package menu;
import java.awt.*;
import javax.swing.*;
import java.net.*;

public class AnimatedSprite  {
	protected JFrame frame;
	protected Graphics2D g2d;
	public Image image;
	public boolean alive;
	
	public Point position;
	public Point velocity;
	
	public double rotationRate;
	
	public int currentState;
	public int animationDirection;
	public int currentFrame, totalFrames;
	public int frameCount, frameDelay;
	public int frameWidth, frameHeight, columns;
	public double moveAngle, faceAngle;
	
	String baseFilename;
	boolean byImage = false;
	
	public AnimatedSprite(JFrame _frame, Graphics2D _g2d) {
		frame = _frame;
		g2d = _g2d;
		image = null;
		alive = true;
		position = new Point(0, 0);
		velocity = new Point(0, 0);
		rotationRate = 0.0;
		currentState = 0;
		currentFrame = 0;
		totalFrames = 1;
		animationDirection = 1;
		frameCount = 0;
		frameDelay = 0;
		frameWidth = 0;
		frameHeight = 0;
		columns = 1;
		moveAngle = 0.0;
		faceAngle = 0.0;
	}
	

	public AnimatedSprite(JFrame _frame, Graphics2D _g2d, String file) {
		frame = _frame;
		g2d = _g2d;
		image = null;
		alive = true;
		position = new Point(0, 0);
		velocity = new Point(0, 0);
		rotationRate = 0.0;
		currentState = 0;
		currentFrame = 0;
		totalFrames = 1;
		animationDirection = 1;
		frameCount = 0;
		frameDelay = 0;
		frameWidth = 0;
		frameHeight = 0;
		columns = 1;
		moveAngle = 0.0;
		faceAngle = 0.0;
		baseFilename = file;
		byImage = true;
	}
	

	public JFrame getJFrame() { return frame; }
	public Graphics2D getGraphics() { return g2d; }
	public void setGraphics(Graphics2D _g2d) {
		g2d = _g2d;
	}
	
	public int getWidth() {
		if(image != null) {
			return image.getWidth(frame);
		} else {
			return 0;
		}
	}
	
	public int getHeight() {
		if(image != null) {
			return image.getHeight(frame);
		} else {
			return 0;
		}
	}
	
	public double getCenterX() {
		return position.x + getWidth()/2;
	}
	
	public double getCenterY() {
		return position.y + getHeight()/2;
	}
	
	public Point getCenter() {
		int x = (int)getCenterX();
		int y = (int)getCenterY();
		return(new Point(x, y));
	}
	
	private URL getURL(String filename) {
		URL url = null;
		try {
			url = this.getClass().getResource(filename);
		} catch(Exception e) { }
		return url;
	}
	
	public Rectangle getBounds() {
		return (new Rectangle((int)position.x, (int)position.y, getWidth(), getHeight()));
	}
	Image[] images;
	public void load(String filename, int _columns, int _totalFrames, int _width, int _height) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		if(!byImage) {
		image = tk.getImage(getURL(filename));
		while(image.getWidth(frame) <= 0) { }
			columns = _columns;
			totalFrames = _totalFrames;
			frameWidth = _width;
			frameHeight = _height;
		}
	if(byImage) {
		images = new Image[totalFrames+1];
		for(int i = 0;i<=totalFrames;i++) {
			if(i < 10) {
				images[i] = tk.getImage(getURL(baseFilename + "0" + i + ".png"));
			} else {
				images[i] = tk.getImage(getURL(baseFilename + i + ".png"));
			}
		}
	}
	
	}
	
	protected void update() {
		if(!byImage) {
		position.x += velocity.x;
		position.y += velocity.y;
		
		if(rotationRate > 0.0) {
			faceAngle += rotationRate;
			if(faceAngle < 0) {
				faceAngle = 360 - rotationRate;
			} else if(faceAngle > 360) {
				faceAngle = rotationRate;
			}
		}
		} 
		
		if(totalFrames > 1) {
			frameCount++;
			if(frameCount > frameDelay) {
				frameCount = 0;
				currentFrame += animationDirection;
				if(currentFrame > totalFrames -1) {
					currentFrame = 0;
				} else if(currentFrame < 0) {
					currentFrame = totalFrames - 1;
				}
			}
		}
	
	
	}
	
	public void drawBounds(Color c) {
		g2d.setColor(c);
		g2d.draw(getBounds());
	}
	
	
	public void draw() {
		update();
		if(!byImage) {
		int frameX = (currentFrame % columns) * frameWidth;
		int frameY = (currentFrame / columns) * frameHeight;
		g2d.drawImage(image, position.x, position.y, position.x + frameWidth, position.y + frameHeight, frameX, frameY, frameX + frameWidth, frameY + frameHeight, getJFrame());
		} else {
			g2d.drawImage(images[currentFrame], position.x, position.y, getJFrame());
		}
	
	}
	
	public boolean collidesWith(Rectangle rect) {
		return (rect.intersects(getBounds()));
	}
	
	public boolean collidesWith(AnimatedSprite sprite) {
		return (getBounds().intersects(sprite.getBounds()));
	}

	public boolean collidesWith(Point point) {
		return (getBounds().contains(point.x, point.y));
	}
}
