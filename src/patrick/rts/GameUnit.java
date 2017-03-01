package patrick.rts;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public abstract class GameUnit extends GameObject
{
	public EUnitType[] types;
	public String name;
	public HashMap<String, Animation> animations = new HashMap<String, Animation>();
	public String currentAnimation;
	public Faction faction;
	public int depth;
	public int hp = 10;
	public int hpMax = 10;
	public boolean selected = false;
	public int targetX, targetY;
	public int speed = 1;
	public boolean moving = false;
	public int xprev, yprev;
	private int xdraw, ydraw;
	public Game.ETargetType currentTargetType = Game.ETargetType.Move;
	public int sightRange = 50;
	public int attackCooldown = 20;
	public int attackCooldownDefault = 20;
	public int attackRange = 12;
	int attackDamage = 2;
	public GameUnit targetUnit;
	
	public GameUnit(int x, int y, Faction fctn)
	{
		super(x, y);
		targetX = x;
		targetY = y;
		xprev=x; yprev=y; xdraw=x; ydraw=y;
		faction = fctn;
	}
	
	@Override
	public void update()
	{		
		collider.x = x;
		collider.y = y;
		int xp = x, yp = y;
		animations.get(currentAnimation).update();
		if (x != targetX && moving)
		{
			if (Math.abs(targetX-x) < speed)
			{
				x = targetX;
			}
			else
				x += x > targetX ? -speed : speed;
			
		}
		if (y != targetY && moving)
		{
			if (Math.abs(targetY-y) < speed)
			{
				y = targetY;
			}
			else
				y += y > targetY ? -speed : speed;
		}
		if (x == targetX && y == targetY)
			moving = false;
		for(Integer i : Game.singleton.world.units.keySet())
		{
			GameUnit unit = Game.singleton.world.units.get(i);
			if (unit == this) continue;
			
			if (collider.collidesWidth(unit.collider))
			{
				if(Math.sqrt(Math.pow(targetX-x, 2) + Math.pow(targetY-y, 2)) <= 12)
					moving = false;
				x += x > unit.x ? 1 : -1;
				y += y > unit.y ? 1 : -1;
			}
			
			if (!unit.faction.equals(faction))
			{
				if(Math.sqrt(Math.pow(unit.x-x, 2) + Math.pow(unit.y-y, 2)) <= sightRange)
				{
					if (!(moving && currentTargetType == Game.ETargetType.Move))
					{
						targetUnit = unit;
					}
				}
			}
		}
		
		if (targetUnit != null && currentTargetType == Game.ETargetType.Attack)
		{
			targetX = targetUnit.x;
			targetY = targetUnit.y;
			if(Math.sqrt(Math.pow(targetUnit.x-x, 2) + Math.pow(targetUnit.y-y, 2)) <= attackRange)
			{
				if (attackCooldown == 0)
				{
					System.out.println("Attack! " + targetUnit.hp);
					targetUnit.hp -= attackDamage;
					if (targetUnit.hp <= 0)
					{
						Game.singleton.world.remove(targetUnit);
						Game.singleton.selected.remove(targetUnit);
						targetUnit = null;
					}
				}
			}
			attackCooldown = (attackCooldown - 1) % attackCooldownDefault;
		}
		
		xdraw = (xprev+x)/2;
		ydraw = (yprev+y)/2;
		
		xprev = x;
		yprev = y;
	}
	
	@Override
	public void draw()
	{
		if (selected)
		{
			DrawHelper.drawRect(new Rectangle(x - 5, y - 2, 10, 4), Game.singleton.selectionColor, false, true);
			animations.get(currentAnimation).draw(x, y);
		}
		else animations.get(currentAnimation).draw(x, y);
	}
	
	public void drawHealth()
	{
		int yo = 10;
		DrawHelper.drawLine(x - 4, y - yo, x + 4, y - yo, Color.red);
		int hpDraw = (8*hp)/hpMax;
		DrawHelper.drawLine(x - 4, y - yo, x - 4 + hpDraw, y - yo, Color.green);
	}
	
	public boolean insideRect(Rectangle rect)
	{
		rect = (Rectangle)rect.clone();
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
//		return x>=rect.x && y>=rect.y && x <= rect.x + rect.width && y <= rect.y + rect.height;
		return collider.collidesWidth(new Collider(rect.x, rect.y, rect.width, rect.height, 0, 0));
	}
}
