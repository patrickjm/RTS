package patrick.rts;

public abstract class Terrain extends GameObject
{
	public Sprite image;
	public Terrain(int x, int y)
	{
		super(x, y);
		this.collider = new Collider(x, y, 8, 8, 4, 4);
	}
}
