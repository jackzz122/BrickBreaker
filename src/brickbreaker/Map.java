package brickbreaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public class Map implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int map [][], brickwidth, brickheight;
	private int margin;	// phan cach ra so voi 
	private int margintop;
	
	public Map (int row, int col, int gamewidth, int gameheight, int boundweight, int northweight)
	{
		map = new int [row][col];
		for (int i=0;i<map.length;i++)
		{
			for (int j=0; j < map[i].length;j++)
			{
				map[i][j] = 1;  
			}
		}
		margin = boundweight;
		margintop = northweight;
		brickwidth = (gamewidth - boundweight * 2) / col;
		brickheight = (gameheight/4 ) / row; 
	}
	
	public void draw (Graphics2D g)
	{
		for (int i=0;i<map.length;i++)
		{
			for (int j=0; j < map[i].length;j++)
			{
				if (map[i][j] > 0)
				{
				g.setColor(Color.WHITE);
//				g.fillRect(j * brickwidth + 80, i*brickheight + 50 , brickwidth, brickheight);
				g.fillRect(j * brickwidth + margin , i*brickheight + margintop , brickwidth, brickheight);
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.BLACK);
//				g.drawRect(j * brickwidth + 80, i*brickheight + 50 , brickwidth, brickheight);
				g.drawRect(j * brickwidth + margin, i*brickheight +  margintop , brickwidth, brickheight);	
				}
			}
		}
		g.setStroke(new BasicStroke(0));
	}
	
	public void resetMap ()
	{
		for (int i=0;i<map.length;i++)
		{
			for (int j=0; j < map[i].length;j++)
			{
				map[i][j] = 1;
			}
		}
	}
	
	public void setMapvalue (int i, int j)
	{
		map [i][j] = 0;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public int getBrickwidth() {
		return brickwidth;
	}

	public void setBrickwidth(int brickwidth) {
		this.brickwidth = brickwidth;
	}

	public int getBrickheight() {
		return brickheight;
	}

	public void setBrickheight(int brickheight) {
		this.brickheight = brickheight;
	}
	
}