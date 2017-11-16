package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import sun.audio.AudioStream;

@SuppressWarnings("serial")
public class Menu extends JPanel implements ActionListener, Runnable {

	Dimension size = new Dimension(500,520);
	private JFrame frame;
	public Toolkit tk;
	public Image logo;
	public AnimatedSprite matrix;
	public JPanel panel;
	public static Color background = new Color(0, 0, 0);
	public static boolean close = false;
	public static Clip song;
	public boolean startup = true;
	BufferedImage backbuffer;
	Thread gameloop;
	Graphics2D g2d;
	
	private URL getURL(String file) {
		URL url = null;
		try {
			url = this.getClass().getResource(file);
		} catch (Exception e) {
			
		}
		return url;
	}
	
	
	public static void main(String[] args) throws Exception {
		new Menu();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(backbuffer, 0, 0, this);
}
	
	public void gameUpdate() {
		if(startup) {
			//Splash screen painting
			matrix.draw();
			} else {
				//Main Menu painting
				g2d.setColor(background);
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.drawImage(logo, getWidth()/2-365/2, 50, 365, 72, this);
				g2d.setColor(Color.black);
				g2d.fillRect(20, 120, getWidth()-40, getHeight()-140);
				g2d.setColor(background);
				g2d.fillRect(25, 125, getWidth()-50, getHeight()-150);
				
			}
	}
	
	public void run() {
		matrix = new AnimatedSprite(frame, g2d, "logo/logo");
		matrix.totalFrames = 23;
		matrix.load("logo.png", 1, 1, 415, 72);
		matrix.position = new Point((int)(size.getWidth()/2-415/2), (int)(size.getHeight()/2-84/2));
		matrix.frameDelay = 10;
		matrix.velocity = new Point(0,0);
		matrix.rotationRate = 0.0;

		Thread t = Thread.currentThread();
		while(t == gameloop) {
			try {
				Thread.sleep(5); 
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			gameUpdate();
		}
	}
	
	public void createGUI() {
		//gui for menu
		SizeableButton p = parseButton("Play");
		SizeableButton o = parseButton("Options");
		SizeableButton h = parseButton("Help");
		SizeableButton q = parseButton("Quit");
		panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridLayout(4,0));
		panel.add(p);
		panel.add(h);
		panel.add(o);
		panel.add(q);
		
		add(Box.createVerticalStrut(510));
		add(panel);
		frame.add(this);
	}
	
	public SizeableButton parseButton(String str) {
		SizeableButton b = new SizeableButton(str, 600, 60);
		b.setText(str);
		b.setActionCommand(str);
		b.addActionListener(this);
		return b;
	}
	
	SwingWorker<?,?> worker = new SwingWorker<Void,Integer>() {
		protected Void doInBackground() throws InterruptedException {
			long time = System.currentTimeMillis();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		frame.setLocationRelativeTo(null);
    		frame.setResizable(false);
    	    logo = tk.getImage(getURL("logo.png"));
    		createGUI();
    		panel.setVisible(false);
    		frame.setVisible(true);
    		long wait = System.currentTimeMillis() - time;
    		Thread.sleep(1500 + wait);
            startup = false;
            panel.setVisible(true);
            //repaint();
            return null;  
        }  
	};
	
	public Menu() throws Exception {
		InputStream is = getClass().getClassLoader().getResourceAsStream("menu/music.wav");
		InputStream bs = new BufferedInputStream(is);
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bs);
		
		song = AudioSystem.getClip();
		song.open(audioInputStream);
		song.start();
		
		tk = Toolkit.getDefaultToolkit();
		backbuffer = new BufferedImage((int)size.getWidth(), (int)size.getHeight(), BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		
		frame = new JFrame("Menu");
		frame.setSize(size);
		gameloop = new Thread(this);
		gameloop.start();
		worker.execute();
	}
	
	public void actionPerformed(ActionEvent e) {
	String command = e.getActionCommand();
	if(command.equals("Play")) {
		try {
			new Main();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	} else if(command.equals("Quit")) {
		JOptionPane.showMessageDialog(null, "Quitting");
		System.exit(0);
	} else if(command.equals("Help"))  {
		JOptionPane.showMessageDialog(null, "Help here");
	} else if(command.equals("Options")) {
		JOptionPane.showMessageDialog(null, "Options");
	}
	}
}
