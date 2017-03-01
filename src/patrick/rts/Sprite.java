package patrick.rts;

import java.awt.Color;

public class Sprite
{
	public int width;
	public int height;
	public int originX, originY;
	public Color[][] pixels;
	public Sprite(Color[][] pixels, int width, int height, int originX, int originY)
	{
		this.pixels = pixels;
		this.width = width;
		this.height = height;
		this.originX = originX;
		this.originY = originY;
	}
	
	public void draw(int x, int y)
	{
		for(int xx=0;xx<width;xx++)
		{
			for(int yy=0;yy<height;yy++)
			{
				int xxx = xx + x - originX;
				int yyy = yy + y - originY;
				if (xxx >= 0 && xxx < Engine.width)
					if (yyy >= 0 && yyy < Engine.height)
						if (pixels[yy][xx] != null)
							DrawHelper.drawPixel(xxx, yyy, pixels[yy][xx]);
			}
		}
	}
	
	public void draw(int x, int y, Color blend)
	{
		for(int xx=0;xx<width;xx++)
		{
			for(int yy=0;yy<height;yy++)
			{
				int xxx = xx + x - originX;
				int yyy = yy + y - originY;
				if (xxx >= 0 && xxx < Engine.width)
					if (yyy >= 0 && yyy < Engine.height)
						DrawHelper.drawPixel(xxx, yyy, pixels[yy][xx] == null ? null : blend);
			}
		}
	}
}