package patrick.rts;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;

public class UnitSoldier extends GameUnit 
{
	String dir = "Right";
	public UnitSoldier(int x, int y, Faction fctn) 
	{
		super(x, y, fctn);
		Color f = faction.color;
		Color d = new Color(32f/255, 32f/255, 32f/255);
		Color g = new Color(64f/255, 64f/255, 64f/255);
		Color l = Color.LIGHT_GRAY;//new Color(192/255, 192f/255, 192f/255);
		Color b = Color.black;
		Color e = null;
		
		Sprite image1 = new Sprite(new Color[][] {
				new Color[] {e, e, e, f, f, f, f, e, e, e},
				new Color[] {e, e, f, d, d, g, g, f, e, e},
				new Color[] {e, f, d, d, g, g, g, g, f, e},
				new Color[] {e, f, d, g, g, g, b, g, f, e},
				new Color[] {e, f, d, g, g, g, l, g, f, e},
				new Color[] {e, e, f, b, g, l, g, f, e, e},
				new Color[] {e, f, b, g, l, g, g, b, f, e},
				new Color[] {e, e, f, b, g, g, g, f, e, e},
				new Color[] {e, e, f, g, f, f, g, f, e, e},
				new Color[] {e, e, e, f, e, e, f, e, e, e}
		}, 10, 10, 5, 8);
		image = image1;
		Sprite image2 = new Sprite(new Color[][] {
				new Color[] {e, e, e, f, f, f, f, e, e, e},
				new Color[] {e, e, f, d, d, d, g, f, e, e},
				new Color[] {e, f, d, d, g, g, g, g, f, e},
				new Color[] {e, f, d, g, g, g, b, g, f, e},
				new Color[] {e, f, d, g, g, g, b, g, f, e},
				new Color[] {e, f, d, g, g, g, g, g, f, e},
				new Color[] {e, e, f, b, g, g, g, b, b, f},
				new Color[] {e, f, b, g, g, g, g, f, f, e},
				new Color[] {e, f, b, l, l, l, g, f, e, e},
				new Color[] {e, f, g, f, f, f, f, g, f, e},
				new Color[] {e, e, f, e, e, e, e, f, e, e}
		}, 10, 10, 5, 8);
		
		Color[][] s = (Color[][])Game.copyColor2d(image1.pixels);
		for(int i=0;i<s.length;i++)
			s[i] = (Color[])Game.reverse(s[i]);
		Sprite image3 = new Sprite(s, 10, 10, 5, 8);
		
		s = (Color[][])Game.copyColor2d(image2.pixels);
		for(int i=0;i<s.length;i++)
			s[i] = (Color[])Game.reverse(s[i]);
		Sprite image4 = new Sprite(s, 10, 10, 5, 8);
		
		collider = new Collider(x, y, 8, 8, 4, 7);
		
		Animation standRight = new Animation(new Sprite[] {image1}, 1);
		animations.put("standRight", standRight);
		Animation standLeft = new Animation(new Sprite[] {image3}, 1);
		animations.put("standLeft", standLeft);
		Animation walkRight = new Animation(new Sprite[] {image1, image2}, 8);
		animations.put("walkRight", walkRight);
		Animation walkLeft = new Animation(new Sprite[] {image3, image4}, 8);
		animations.put("walkLeft", walkLeft);
		
		currentAnimation = "walkRight";
	}
	
	@Override
	public void update()
	{
		super.update();
		String pref = "stand";
		if (moving)
		{
			pref = "walk";
			if (targetX < x)
			{
				dir = "Left";
			}
			if (targetX > x)
			{
				dir = "Right";
			}
		}
		currentAnimation = pref + dir;
	}
}
