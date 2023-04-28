package brickbreaker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Panel extends JPanel implements KeyListener, Runnable, MouseListener {
	
	// menu 
	private boolean menu = true;
	private BufferedImage playbtn , quitbtn, loadbtn;
	private Rectangle playBtnHitbox, quitBtnHitbox, loadBtnHitbox;
	private JTextField input;

	//highscore
	
	private boolean highscore = false;

	//user
	
	private String playername;
	private ArrayList <PlayerHistory> hs;
	private int score = 0;
	
	// Game
	private int gameWidth = 700;
	private int gameHeight = 700;
	private BufferedImage Ball_image;
	private int PlayerX = 300, PlayerY = gameHeight*5/6, PlayerWidth = 100, PlayerHeight = 8 , Move = 2;
	private int ballMoveX = -1, ballMoveY = -2, ballWidth = 20 , ballHeight = 20;
	private boolean right=false,left=false, over = false;
	private volatile boolean pause = false, game = false;
	private Rectangle Player, Ball, EastBound, WestBound, NorthBound;
	private Map map;
	private int NorthBoundweight = 50;
	private int BoundWeight = 3;
	private int pointPerBreak = 5;
	private int row = 5, col = 7;
	private int timeupdate = 8;
	private boolean started = false;
	private boolean readConfigFile = false;
	private String linkpath = "";
	
	// Database 
	private String dbName = "test";
	private String dbPort = "3306";
	private String user = "root";
	private String pass = "";
	
	// Save file destination
	private String savefiledestination = "D:\\Cong_nghe_Java\\savegame.txt";
	
	public Panel ()
	{
		input = new JTextField(20);
		this.readConfig();
		PlayerX = gameWidth/2 - PlayerWidth/2;
		PlayerY = gameHeight*5/6;
		map = new Map (row,col, gameWidth, gameHeight,BoundWeight,NorthBoundweight);
		this.addMouseListener(this);
		this.addKeyListener(this);	
		this.setsize();
		this.getImage ();
		this.initRectangle();
	}
	
	private void readConfig ()
	{
		try {
			FileReader fr = new FileReader(System.getProperty("user.dir") + "\\configgame");
			Properties pr = new Properties();
			pr.load(fr);
			
			gameWidth = Integer.parseInt(pr.getProperty("gameWidth"));
			gameHeight = Integer.parseInt(pr.getProperty("gameHeight"));
			PlayerWidth = Integer.parseInt(pr.getProperty("PlayerWidth")); 
			PlayerHeight = Integer.parseInt(pr.getProperty("PlayerHeight"));
			Move = Integer.parseInt(pr.getProperty("Move"));
			row = Integer.parseInt(pr.getProperty("row"));
			col = Integer.parseInt(pr.getProperty("col"));
			dbName = pr.getProperty("dbName");
			dbPort = pr.getProperty("dbPort");
			savefiledestination = pr.getProperty("save");
			timeupdate = Integer.parseInt(pr.getProperty("timeupdate"));
			user = pr.getProperty("user");
			pass = pr.getProperty("pass");
			this.readConfigFile = true;

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initRectangle ()
	{
		Player = new Rectangle(PlayerX, PlayerY, PlayerWidth,PlayerHeight);
		Ball = new Rectangle(Player.x + Player.width/2 - ballWidth/2 , Player.y - ballHeight ,ballWidth,ballHeight);
		EastBound = new Rectangle(0, 0, BoundWeight, gameHeight);
		WestBound = new Rectangle(gameWidth - BoundWeight , 0, BoundWeight, gameHeight);
		NorthBound = new Rectangle (0,0,gameWidth,NorthBoundweight);
		playBtnHitbox = new Rectangle(gameWidth/2 - 140/2 , gameHeight / 2 - 112, 140,56);
		quitBtnHitbox = new Rectangle(gameWidth/2 - 140/2, gameHeight/ 2 + 112, 140,56);
		loadBtnHitbox = new Rectangle(gameWidth/2 - 140/2, gameHeight / 2  , 140,56);
	}
	
	private void getImage ()
	{
		BufferedImage readImg = null;
		playbtn = null;
		quitbtn = null;
		loadbtn = null;
		InputStream in = getClass().getResourceAsStream("/menu2.png");  
		try {
			readImg = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		playbtn = readImg.getSubimage(0, 0, 140, 56);
		loadbtn = readImg.getSubimage(0, 56, 140, 56);
		quitbtn = readImg.getSubimage(0, 112, 140, 56);
		
		
		in = getClass().getResourceAsStream("/ballC.png");
		try {
			readImg = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Ball_image = readImg;
	}
	
	private void setsize ()
	{
		Dimension d = new Dimension(gameWidth, gameHeight);
		this.setPreferredSize(d);
	}
	
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, gameWidth, gameHeight);
		
		
		if (menu)
		{
			g.drawImage(playbtn, playBtnHitbox.x, playBtnHitbox.y, playBtnHitbox.width, playBtnHitbox.height, null);
			g.drawImage(loadbtn, loadBtnHitbox.x, loadBtnHitbox.y, loadBtnHitbox.width, loadBtnHitbox.height, null);
			g.drawImage(quitbtn, quitBtnHitbox.x, quitBtnHitbox.y, quitBtnHitbox.width, quitBtnHitbox.height, null);
			
			
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 20));
			
			g.drawString("Press H to check link configgame status", gameWidth/20, gameHeight*9/10 - 50);
			g.drawString("Status: " + linkpath, gameWidth/20, gameHeight*9/10);
			System.out.println(this.gameWidth);
		}
		
		if (game)
		{
			map.draw((Graphics2D) g);
			
			// draw border 
			
			g.setColor(Color.GREEN);
			g.fillRect(NorthBound.x, NorthBound.y, NorthBound.width, NorthBound.height);
			g.fillRect(EastBound.x, EastBound.y, EastBound.width, EastBound.height);
			g.fillRect(WestBound.x, WestBound.y, WestBound.width, WestBound.height);
			
			// draw score
			
			g.setColor(Color.blue);
			g.setFont(new Font("serif", Font.BOLD, 20));
			FontMetrics fm = g.getFontMetrics(g.getFont());
			Rectangle2D fontBounds = fm.getStringBounds("hello", g);
			g.drawString("" + score, gameWidth*9/10 , (int) ( NorthBoundweight/2 + fontBounds.getHeight()/4));
			
			// draw user 
			
			g.drawString("Name: " + playername, gameWidth/10, (int) ( NorthBoundweight/2 + fontBounds.getHeight()/4));

			
			// Player
			
			g.setColor(Color.GREEN);
			g.fillRect(Player.x,Player.y,Player.width,Player.height);
			
			// Ball
			
			g.drawImage(Ball_image,Ball.x,Ball.y, Ball.width, Ball.height, null);
			
			// pause
			
			if (!pause && started)
			{
				g.setColor(Color.red);
				fontBounds = fm.getStringBounds("You can press S to save if you want", g);
				g.drawString("You can press S to save if you want",(int) ( gameWidth/2 - fontBounds.getWidth()/2), gameHeight/2);
			}
			
			// game over
			
			if (over && score < pointPerBreak * map.getMap().length * map.getMap()[0].length )
			{
				g.setColor(Color.red);
				fontBounds = fm.getStringBounds("Game over", g);
				g.drawString("Game over", (int) ( gameWidth/2 - fontBounds.getWidth()/2), gameHeight/2 - 50);
				fontBounds = fm.getStringBounds("Enter to restart", g);
				g.drawString("Enter to restart", (int) ( gameWidth/2 - fontBounds.getWidth()/2), gameHeight/2);
				fontBounds = fm.getStringBounds("Back_Space to see highscore", g);
				g.drawString("Back_Space to see highscore ",(int) ( gameWidth/2 - fontBounds.getWidth()/2), gameHeight/2 + 50);
			}
			
			// game won
			
			else if (over && score == pointPerBreak * map.getMap().length * map.getMap()[0].length)
			{
				g.setColor(Color.red);
				fontBounds = fm.getStringBounds("You Won !!", g);
				g.drawString("You Won !!", (int) ( gameWidth/2 - fontBounds.getWidth()/2), gameHeight/2 - 50);
				fontBounds = fm.getStringBounds("Enter to restart", g);
				g.drawString("Enter to restart",(int) ( gameWidth/2 - fontBounds.getWidth()/2), gameHeight/2);
			}
			
		}
		
		if (highscore)
		{
			g.setColor(Color.red);
			g.setFont(new Font ("serif", Font.BOLD, 18));
//			g.drawString("id", gameWidth/3,  50);
			g.drawString("NAME", gameWidth/4 , 50);
			g.drawString("SCORE", gameWidth*3/4 ,  50);
			for (int i=0;i<hs.size();i++)
			{
//				g.drawString("" + hs.get(i).getId(), gameWidth/3, (i+2)* 50);
				g.drawString(hs.get(i).getName(), gameWidth/4 , (i+2)*50);
				g.drawString("" +hs.get(i).getScore(), gameWidth*3/4 , (i+2)* 50);
			}
			FontMetrics fm = g.getFontMetrics(g.getFont());
			Rectangle2D fontbounds = fm.getStringBounds("Press Enter to continue", g);
			g.drawString("Press Enter to continue",(int) (gameWidth/2 - fontbounds.getWidth()/2), this.PlayerY);
		}
	}
	
	// event listen 
	// key
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			if (!over)
			{
				left = true;
				if (!started) started = true;
				if (!pause) pause = true;			
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) 
		{
			if (!over)
			{
				right = true;
				if (!started) started = true;
				if (!pause) pause = true;	
			}
			
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			if (!over) pause = !pause;
		}
		
			
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)	
		{
			left = false;
		}		
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			right = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if (over)
			{
				Player.x = PlayerX;
				Player.y = PlayerY;
				Ball.x = Player.x + Player.width/2 - ballWidth/2  ;
				Ball.y = Player.y - ballHeight - 2 ;
				score = 0;
				this.map.resetMap();
				over = false;
				this.repaint();
			}
			if (highscore)
			{
				menu = true;
				started = false;
				highscore = false;
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
		{
			if (over==true)
			{
				System.out.println(dbName);
				StatementHandle.add(new PlayerHistory(playername, score),dbPort,dbName, user , pass);
				Player.x = PlayerX;
				Player.y = PlayerY;
				Ball.x = Player.x + Player.width/2 - ballWidth/2  ;
				Ball.y = Player.y - ballHeight - 2 ;
				score = 0;
				this.map.resetMap();
				over = false;
				game = false;
				highscore = true;
				
				hs = StatementHandle.readAll(10,dbPort,dbName, user , pass);
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_S)
		{
			ObjectOutputStream out = null;
			PlayerData PD = new PlayerData(Ball, map, Player, score, playername,ballMoveX,ballMoveY);
			 try {
				out = new ObjectOutputStream (new FileOutputStream(this.savefiledestination));
				out.writeObject(PD);
				
				if (out!=null) out.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_H)
		{
			if (menu)
			{
				if (!readConfigFile)
				{
					this.linkpath = "Failed. Move the configgame to " + System.getProperty("user.dir");
				}
				else
				{
					linkpath = "You have read the configgame successfully";
				}
			}
		}
	}
	
	// mouse listen
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void mousePressed(MouseEvent e) {
		if (menu)
		{
			if (playBtnHitbox.contains(e.getX(), e.getY()))
			{
				playername = input.getText();
				game=true;
				menu=false;
			}
			else if (quitBtnHitbox.contains(e.getX(), e.getY()))
			{
				System.exit(0);
			}
			else if (loadBtnHitbox.contains(e.getX(), e.getY()))
			{
				File f = new File (this.savefiledestination);
				if (f.exists())
				{
					PlayerData PD = null;
					ObjectInputStream in = null;
					try {
						in = new ObjectInputStream (new FileInputStream(f));
						PD = (PlayerData) in.readObject();
						if (in!=null) in.close ();
					}
					catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					if (PD != null)
					{
						this.Ball = PD.getBall();
						this.Player = PD.getPlayer();
						this.map =PD.getMap();
						this.playername=PD.getPlayerName();
						this.score=PD.getScore();
						this.ballMoveX = PD.getBallMoveX() ;
						this.ballMoveY = PD.getBallMoveY();
					}
					
					game = true;
					menu = false;
					 
				}
			}
		}
	}

	
	// logic handle
	
	private void updatePlayerPos ()
	{
		int PlayerXmove = 0;
		if (!left && !right)
		{
			return;
		}
		if (left)
		{
			PlayerXmove -= Move;
		}
		if (right)
		{
			PlayerXmove += Move;
		}
		if (Player.x + Player.width + PlayerXmove >= gameWidth - this.BoundWeight)
		{
			Player.x = gameWidth - Player.width - BoundWeight;
		}
		else if (Player.x  + PlayerXmove <= BoundWeight)
		{
			Player.x = BoundWeight;
		}	
		else if (Player.x + Player.width + PlayerXmove >= Ball.x && Ball.y + Ball.height  >= Player.y && Ball.y <= Player.y + Player.height && Player.x < Ball.x)
		{
			if (PlayerXmove > 0)
			{				
				Player.x += PlayerXmove;
				if (ballMoveX < 0) ballMoveX = -ballMoveX;
				if (ballMoveY > 0) ballMoveY = -ballMoveY;
			}
		}
		else if (Player.x + PlayerXmove <= Ball.x + Ball.width && Ball.y + Ball.height  >= Player.y && Ball.y <= Player.y + Player.height && Player.x + Player.width > Ball.x + Ball.width)
		{
			if (PlayerXmove < 0 )
			{
				Player.x += PlayerXmove;
				if (ballMoveX > 0) ballMoveX = -ballMoveX;
				if (ballMoveY > 0) ballMoveY = -ballMoveY;
			}
		}
		else
		{
			Player.x += PlayerXmove;
		}
	}
	
	private int counthitbox ()
	{
		int dem=0;
		if (Player.contains(Ball.x, Ball.y))
		{
			dem++;
		}
		if (Player.contains(Ball.x + Ball.width, Ball.y))
		{
			dem++;
		}
		if (Player.contains(Ball.x, Ball.y + Ball.height))
		{
			dem++;
		}
		if (Player.contains(Ball.x + Ball.width, Ball.y + Ball.height))
		{
			dem++;
		}
		return dem;
	}
	
	private void mapUpdate ()
	{
		A:	for (int i=0; i< map.getMap().length;i++)
		{
			for (int j=0;j< map.getMap()[i].length;j++)
			{
				if (map.getMap()[i][j] > 0)
				{
					int brickX = j * map.getBrickwidth() + BoundWeight;
					int brickY = i * map.getBrickheight() + NorthBoundweight;
					
					Rectangle brick = new Rectangle(brickX,brickY,map.getBrickwidth(),map.getBrickheight());
					if (Ball.intersects(brick))
					{
						map.setMapvalue(i, j);
						if (Ball.x + Ball.width - 1 <= brick.x || Ball.x +1 >= brick.x + brick.width)
						{	
							ballMoveX = -ballMoveX; 
						}
						else
						{								
							ballMoveY = -ballMoveY;
						}
						score+= pointPerBreak;
						break A;
					}	
				}
			}
		}
	}
	
	private void updateBall ()
	{
		Ball.x+=ballMoveX;
		Ball.y+=ballMoveY;
	
		if (Ball.intersects(Player))
		{
			ballMoveY = -ballMoveY;
			if (Ball.contains(Player.x,Player.y) && Ball.contains(Player.x,Player.y + Player.height) || Ball.contains(Player.x+Player.width,Player.y) && Ball.contains(Player.x + Player.width,Player.y + Player.height) )
			{		
				ballMoveY = -ballMoveY;
				ballMoveX = -ballMoveX;	
			}
			else if (counthitbox () == 1)
			{	
				ballMoveX = -ballMoveX;
			}
		}
		if (Ball.intersects(EastBound))	// east
		{
			ballMoveX = -ballMoveX;
		}
		if (Ball.intersects(WestBound))	// west
		{
			ballMoveX = -ballMoveX;
		}
		if (Ball.intersects(NorthBound))
		{
			ballMoveY = -ballMoveY;
		}
		if (Ball.y >= gameHeight)
		{
			over = true;
			pause = false;
			started = false;
		}
		if (score == pointPerBreak * map.getMap().length * map.getMap()[0].length)
		{
			over = true;
			pause = false;
			started = false;
		}
	}
	
	
	
	// thread implements 
	
	@Override
	public void run() {
		long now = System.currentTimeMillis();
		while (true)
		{	
				if (System.currentTimeMillis() - now >= timeupdate)
				{	
					if (menu)
					{
						if (input.getParent()==null)
							{
								this.add(input);
							}
					}
					if (game)
					{
						if (input.getParent()!=null) 
							{
								this.remove(input);
							}
						
						if (pause)
						{						
							this.updatePlayerPos(); 
							this.updateBall();
							this.mapUpdate();
						}
					}
					this.repaint();
					now = System.currentTimeMillis();
				}
		}
		}

	
	}