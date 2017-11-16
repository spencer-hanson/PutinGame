package menu;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

public class Main extends JFrame implements KeyListener {

	private Graphics2D g2d;
	private BufferedImage backbuffer;
	private Entity player;
	private Entity country;
	private Entity win;
	private int counter;
	private boolean done = false;
	private boolean music = false;
	public static final int WIDTH = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getWidth();
	public static final int HEIGHT = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getHeight();;

	public String[] countries = { "armenia", "azerbaijan", "belarus",
			"estonia", "georgia", "kazakhstan", "kyrgyzstan", "latvia",
			"lithuania", "moldova", "tajikstan", "turkmenistan", "ukraine",
			"uzbekistan" };

	public void init() {
		setSize(WIDTH, HEIGHT);
		setTitle("Putin game");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		addKeyListener(this);
		setVisible(true);
	}

	public Entity getCountry() {
		for (int i = 0; i < countries.length; i++) {
			if (!countries[i].equals("none")) {
				String tmp = countries[i];
				Random rand = new Random();
				countries[i] = "none";
				Entity entity = new Entity(rand.nextInt(WIDTH),
						rand.nextInt(HEIGHT), tmp, false);

				boolean done = false;
				while (!done) {
					entity.update();
					if (!entity.getBounds().intersects(
							(new Rectangle(player.getX(),
									player.getY(),
									player.getWidth(),
									player.getHeight())))
							&& entity.getX() + entity.getWidth() < WIDTH-100
							&& entity.getY() + entity.getHeight() < HEIGHT-100
							&& entity.getX() > 100 && entity.getY() > 100) {
						done = true;
					} else {
						entity.update();
						entity = new Entity(rand.nextInt(WIDTH),
								rand.nextInt(HEIGHT), tmp, false);
					}
				}
				return entity;
			}
		}
		return null;
	}

	public Main() throws IOException {
		init();
		counter = 0;
		backbuffer = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		g2d = backbuffer.createGraphics();
		player = new Entity(250, 250, "putin", true);
		win = new Entity(WIDTH/2 - 1247/2, HEIGHT/2 - 855/2, "win", true);
		country = getCountry();
		new Runner().start();
	}

	public void update() {
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		if (country == null) {
			if(!music) {
				music = true;
				InputStream is = getClass().getClassLoader().getResourceAsStream("menu/win.wav");
				InputStream bs = new BufferedInputStream(is);
				try {
					
					Menu.song.stop();
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
						   bs);
						Clip clip = AudioSystem.getClip();
						clip.open(audioInputStream);
						FloatControl gainControl = 
						    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
						gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
						clip.start();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			done = true;
			win.paint(g2d);
			win.update();
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 72));
			g2d.setColor(Color.RED);
			g2d.drawString("MISSION ACCOMPLISHED", WIDTH/2 - 400, 150);
			g2d.drawString("YOUR TIME: " + counter/20, WIDTH/2 - 400, 220);
			
		} else {
			player.paint(g2d);
			player.update();
			country.update();
			country.paint(g2d);
			g2d.setColor(Color.black);
			g2d.drawString(counter/20 + "", WIDTH - 150, 50);
			g2d.setColor(Color.red);
			//g2d.draw(country.getBounds());
			//g2d.draw(player.getBounds());
			if (player.getBounds().intersects(country.getBounds())) {
				country = getCountry();
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(backbuffer, 0, 0, this);
	}

	class Runner extends Thread implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(20);
					update();
					repaint();
					if(!done) {
						counter++;	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int speed = 10;
		if (e.getKeyCode() == KeyEvent.VK_A) {
			player.setVX(-speed);
			player.setVY(0);
			player.flip(true);
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			player.setVX(speed);
			player.setVY(0);
			player.flip(false);
		}

		if (e.getKeyCode() == KeyEvent.VK_S) {
			player.setVY(speed);
			player.setVX(0);
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			player.setVX(0);
			player.setVY(-speed);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}