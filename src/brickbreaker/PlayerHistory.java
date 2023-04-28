package brickbreaker;

public class PlayerHistory {

	private int id;
	private String name;
	private int score;
	
	public PlayerHistory(String name, int score) {
		this.name = name;
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public PlayerHistory(int id, String name, int score) {
		this.id = id;
		this.name = name;
		this.score = score;
	}
	@Override
	public String toString() {
		return "PlayerHistory [id=" + id + ", name=" + name + ", score=" + score + "]";
	}

	
}