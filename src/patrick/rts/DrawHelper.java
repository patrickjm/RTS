package patrick.rts;

import java.awt.Color;
import java.awt.Rectangle;

public class DrawHelper 
{
	public static boolean useCamera = true;
	
	public static void drawPixel(int x, int y, Color c)
	{
		x += useCamera ? -Game.singleton.world.cameraX : 0;
		y += useCamera ? -Game.singleton.world.cameraY : 0;
		if (x >= 0 && y >= 0 && x < Engine.width && y < Engine.height)
			Engine.colors[x][y] = c;
	}
	
	public static void drawRect(Rectangle rect, Color c, boolean filled, boolean cornerless)
	{
		if (filled)
		{
			if (cornerless)
			{
				drawRect(rect, c, false, true);
				drawRect(new Rectangle(rect.x+1, rect.y+1, rect.width-1, rect.height-1), c, true, false);
			}
			else
			{
				for(int x=rect.x;x<rect.x+rect.width;x++)
				{
					for(int y=rect.y;y<rect.y+rect.height;y++)
					{
						drawPixel(x, y, c);
					}
				}
			}
		}
		else
		{
			if (cornerless)
			{
				for(int i=rect.x+1;i<=rect.x + rect.width-1;i++)
					drawPixel(i, rect.y, c);
				for(int i=rect.x+1;i<=rect.x + rect.width-1;i++)
					drawPixel(i, rect.y + rect.height, c);
				for(int i=rect.y+1;i<=rect.y + rect.height-1;i++)
					drawPixel(rect.x, i, c);
				for(int i=rect.y+1;i<=rect.y + rect.height-1;i++)
					drawPixel(rect.x + rect.width, i, c);
			}
			else
			{
				for(int i=rect.x;i<=rect.x + rect.width;i++)
					drawPixel(i, rect.y, c);
				for(int i=rect.x;i<=rect.x + rect.width;i++)
					drawPixel(i, rect.y + rect.height, c);
				for(int i=rect.y;i<=rect.y + rect.height;i++)
					drawPixel(rect.x, i, c);
				for(int i=rect.y;i<=rect.y + rect.height;i++)
					drawPixel(rect.x + rect.width, i, c);
			}
		}
	}
	
	public static void drawLine(int x1, int y1, int x2, int y2, Color c)
	{
		if (x1 == x2)
		{
			for(int i=0;i<x2-x1;i++)
			{
				drawPixel(x1, y1 + i, c);
			}
		}
		else
		{
			int e = 0, y = y1;
			double m = (y2 - y1)/(x2 - x1);
			for(int x = x1; x <= x2; x++)
			{
				drawPixel(x, y, c);
				if (e + m > -0.5)
					e += m;
				else
				{
					y--;
					e += m + 1;
				}
			}
		}
	}
	
	public static void drawCircle(int x0, int y0, int radius, boolean filled, Color c)
	{
		if (filled)
		{
			for(int x=-radius;x<radius;x++)
			{
				int height = (int)Math.sqrt(radius*radius-x*x);
				for(int y=-height;y<height;y++)
				{
					drawPixel(x + x0, y + y0, c);
				}
			}
		}
		else
		{
			int f =  1 - radius;
			int ddF_x = 1;
			int ddF_y = -2*radius;
			int x=0;
			int y=radius;
			drawPixel(x0, y0+radius, c);
			drawPixel(x0, y0-radius, c);
			drawPixel(x0+radius, y0, c);
			drawPixel(x-radius, y0, c);
			while(x<y)
			{
				if(f>=0)
				{
					y--;
					ddF_y+=2;
					f+=ddF_y;
				}
				x++;
				ddF_x+=2;
				f+=ddF_x;
				drawPixel(x0, y0, c);
				drawPixel(x0 + x, y0 + y, c);
				drawPixel(x0 + x, y0 - y, c);
				drawPixel(x0 - x, y0 + y, c);
				drawPixel(x0 - x, y0 - y, c);
				drawPixel(x0 + y, y0 + x, c);
				drawPixel(x0 + y, y0 - x, c);
				drawPixel(x0 - y, y0 + x, c);
				drawPixel(x0 - y, y0 - x, c);
			}
		}
	}
	
	
}
