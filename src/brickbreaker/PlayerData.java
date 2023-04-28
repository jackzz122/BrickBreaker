package brickbreaker;

import java.awt.Rectangle;
import java.io.Serializable;

public class PlayerData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Rectangle ball;
	private Map map;
	private Rectangle player;
	private int score;
	private String PlayerName;
	private int BallMoveX, BallMoveY;
	
	public PlayerData(Rectangle ball, Map map, Rectangle player, int score, String playerName, int ballMoveX,
			int ballMoveY) {
			this.ball = ball;
			this.map = map;
			this.player = player;
			this.score = score;
			PlayerName = playerName;
			BallMoveX = ballMoveX;
			BallMoveY = ballMoveY;
		}
	public Rectangle getBall() {
		return ball;
	}
	public void setBall(Rectangle ball) {
		this.ball = ball;
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	public Rectangle getPlayer() {
		return player;
	}
	public void setPlayer(Rectangle player) {
		this.player = player;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getPlayerName() {
		return PlayerName;
	}
	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}
	public int getBallMoveX() {
		return BallMoveX;
	}
	public void setBallMoveX(int ballMoveX) {
		BallMoveX = ballMoveX;
	}
	public int getBallMoveY() {
		return BallMoveY;
	}
	public void setBallMoveY(int ballMoveY) {
		BallMoveY = ballMoveY;
	}
	
	
	
	
	
}