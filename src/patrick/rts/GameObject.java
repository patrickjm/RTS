package patrick.rts;

public abstract class GameObject
{
	public int x, y;
	public Sprite image;
	public Collider collider;
	public void update() {}
	public void draw() {}
	public GameObject(int x, int y) {
		this.x=x;
		this.y=y;
	}
}