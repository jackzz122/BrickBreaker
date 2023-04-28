package brickbreaker;

import java.awt.BorderLayout;

import javax.swing.JFrame;


public class Game {
	
	private JFrame window;
	private Panel panel;
	private Thread gamethread;
	
	public Game ()
	{	
		 window = new JFrame();
		 window.setTitle("Phan Anh - Duc Luong");
		 window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 window.setResizable(false);
		 panel = new Panel ();
		 window.add(panel);
		 window.pack();
		 window.setLocationRelativeTo(null); 
		 panel.setFocusable(true);
		 panel.requestFocus();
		 startgameloop();
		 window.setVisible(true);
	
	}
	
	public void startgameloop ()
	{
		 gamethread = new Thread (panel);
		 gamethread.start();			
	}
	
	public static void main(String[] args) {
		Game g = new Game ();

	}
	
	
	
}