package patrick.rts;

public class Collider 
{
	public int x, y, width, height;
	public int originX, originY;
	public Collider(int x, int y, int width, int height, int originX, int originY)
	{
		this.x =x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.originX = originX;
		this.originY = originY;
	}
	
	public boolean collidesWidth(Collider c)
	{
		if (y+height-originY<c.y-c.originY) return false;
		if (y-originY>c.y+c.height-c.originY) return false;
		if (x+width-originX<c.x-c.originX) return false;
		if (x-originX>c.x+c.width-c.originX) return false;
		return true;
	}
	
	
}
