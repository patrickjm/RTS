package patrick.rts;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Game 
{
	public static final double DEG = -180 / Math.PI;
    public static final double RAD = Math.PI / -180;
	
	Rectangle selection = null;
	Color selectionColor = new Color(0, .6f, 0);
	public World world;
	public static Game singleton;
	ArrayList<GameUnit> selected = new ArrayList<GameUnit>();
	HashMap<ETargetType, Sprite> cursors = new HashMap<ETargetType, Sprite>();
	ETargetType currentTargetType = ETargetType.Move;
	
	public Game()
	{
		singleton = this;
		world = new World();
		
		cursors.put(ETargetType.Move, new Sprite(new Color[][] {
				new Color[] {Color.green, null, null, null},
				new Color[] {Color.green, Color.green, null, null},
				new Color[] {Color.green, Color.green, Color.green, null},
				new Color[] {Color.green, Color.green, Color.green, Color.green},
		}, 4, 4, 0, 0));
		
		cursors.put(ETargetType.Attack, new Sprite(new Color[][] {
				new Color[] {Color.red, null, null, null},
				new Color[] {Color.red, Color.red, null, null},
				new Color[] {Color.red, Color.red, Color.red, null},
				new Color[] {Color.red, Color.red, Color.red, Color.red},
		}, 4, 4, 0, 0));
	}
	
	public void update()
	{
		world.update();
		if(Input.mouse.buttonDownOnce(1))
		{
			selection = new Rectangle(Input.mouse.getPosition().x, Input.mouse.getPosition().y, 1, 1);
			if (!Input.keyboard.keyDown(KeyEvent.VK_SHIFT))
				for(Integer i : world.units.keySet())
				{
					world.units.get(i).selected = false;
					selected.remove(world.units.get(i));
				}
		}
		if (!Input.mouse.buttonDown(1))
		{
			selection = null;
		}
		if (selection != null)
		{
			selection.width = Input.mouse.getPosition().x - selection.x - 2;
			selection.height = Input.mouse.getPosition().y - selection.y - 5;
			for(Integer i : world.units.keySet())
			{
				GameUnit unit = world.units.get(i);
				if (unit.insideRect(selection))
				{
					if (unit.faction.name.equals(world.playerFaction))
					{
						if (!selected.contains(unit))
							selected.add(unit);
						unit.selected = true;
					}
				}
				else if (!Input.keyboard.keyDown(KeyEvent.VK_SHIFT))
				{
					unit.selected = false;
					selected.remove(unit);
				}
			}
		}
		if (Input.mouse.buttonDownOnce(3))
		{
			selection = null;
			for(GameUnit u : selected)
			{
				u.targetX = Input.mouse.getPosition().x;
				u.targetY = Input.mouse.getPosition().y;
				u.moving = true;
				u.currentTargetType = currentTargetType;
			}
		}
		if(Input.keyboard.keyDown(KeyEvent.VK_CONTROL))
		{
			if(Input.keyboard.keyDown(KeyEvent.VK_LEFT))
			{
				for(GameUnit unit : selected)
				{
					unit.moving = true;
					unit.targetX = unit.x - 1;
				}
			}
			if(Input.keyboard.keyDown(KeyEvent.VK_RIGHT))
			{
				for(GameUnit unit : selected)
				{
					unit.moving = true;
					unit.targetX = unit.x + 1;
				}
			}
			if(Input.keyboard.keyDown(KeyEvent.VK_UP))
			{
				for(GameUnit unit : selected)
				{
					unit.moving = true;
					unit.targetY = unit.y - 1;
				}
			}
			if(Input.keyboard.keyDown(KeyEvent.VK_DOWN))
			{
				for(GameUnit unit : selected)
				{
					unit.moving = true;
					unit.targetY = unit.y + 1;
				}
			}
			if(Input.keyboard.keyDown(KeyEvent.VK_DELETE))
			{
				for(GameUnit unit : selected)
				{
					world.remove(unit);
				}
				selected = new ArrayList<>();
			}
			if(Input.keyboard.keyDown(KeyEvent.VK_SPACE) && selected.size() != 0)
			{
				int meanX = 0;
				int meanY = 0;
				int num = 0;
				for(GameUnit unit : selected)
				{
					num++;
					meanX += unit.x;
					meanY += unit.y;
				}
				meanX /= num;
				meanY /= num;
				for(GameUnit unit : selected)
				{
					unit.moving = true;
					unit.targetX = meanX > unit.x ? unit.x + 1 : unit.x - 1;
					unit.targetY = meanY > unit.y ? unit.y + 1 : unit.y - 1;
				}
			}
		}
		
		if(Input.keyboard.keyDownOnce(KeyEvent.VK_A))
		{
			currentTargetType = ETargetType.Attack;
		}
		if(Input.keyboard.keyDownOnce(KeyEvent.VK_M))
		{
			currentTargetType = ETargetType.Move;
		}
	}
	
	public void draw()
	{
		world.draw();
		if (selection != null)
		{
			Rectangle rect = (Rectangle)selection.clone();
			if (rect.width < 0)
			{
				rect.x += rect.width;
				rect.width = Math.abs(rect.width);
			}
			if (rect.height < 0)
			{
				rect.y += rect.height;
				rect.height = Math.abs(rect.height);
			}
			
			DrawHelper.drawRect(rect, selectionColor, false, true);
		}
		
		for(Integer i : world.units.keySet())
		{
			GameUnit u = world.units.get(i);
			//if((Input.keyboard.keyDown(KeyEvent.VK_ALT) && u.faction.name.equals(Game.singleton.world.playerFaction)) || u.selected)
			if((Input.keyboard.keyDown(KeyEvent.VK_CONTROL)) || u.selected)
			{
				u.drawHealth();
			}
		}
		
		//draw the cursor
		cursors.get(currentTargetType).draw(Input.mouse.getPosition().x, Input.mouse.getPosition().y);
	}
	
	public static float angleBetween(float x1, float y1, float x2, float y2)
	{
		float a = (float)(Math.atan2(y2 - y1, x2 - x1) * DEG);
		return a < 0 ? a + 360 : a;
	}

    public static float distance(float x1, float y1, float x2, float y2)
	{
		return (float)Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
    
    public static Object[] reverse(Object[] arr) {
        List < Object > list = Arrays.asList(arr);
        Collections.reverse(list);
        return list.toArray();
    }
    
    public static Color[][] copyColor2d(Color[][] colors) 
    {
    	Color[][] copy = new Color[colors.length][];

    	for (int i = 0; i < colors.length; i++) {
    	        copy[i] = new Color[colors[i].length];

    	        for (int j = 0; j < colors[i].length; j++) {
    	                copy[i][j] = colors[i][j] == null ? null : new Color(colors[i][j].getRed(), colors[i][j].getBlue(), colors[i][j].getGreen(), colors[i][j].getAlpha());
    	        }
    	}
    	
    	return copy;
    }
    
    public static Point[] getFormation(Point location, ArrayList<GameUnit> units)
    {
    	Point[] points = new Point[units.size()];
    	
    	return null;
    }
    
    enum ETargetType
    {
    	Move,
    	Attack
    }
}
