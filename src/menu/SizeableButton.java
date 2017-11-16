package menu;

import java.awt.Dimension;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class SizeableButton extends JButton {
	public int w, h;
	public SizeableButton(String title) {
		super(title);
	}
	
	public SizeableButton(String title, int w, int h) {
		super(title);
		this.w = w;
		this.h = h;
	}
	
	public Dimension getMinimumSize() {
		return new Dimension(w, h); 
	}
	
    public Dimension getPreferredSize() {
    	return getMinimumSize(); 
    }
}
