package patrick.rts;
import java.awt.Color;
import java.util.HashMap;

public class World 
{
	public int width, height;
	public HashMap<Integer, GameUnit> units = new HashMap<Integer, GameUnit>();
	public HashMap<String, Faction> factions = new HashMap<String, Faction>();
	public int cameraX = 0, cameraY = 0;
	public boolean[] collisionGrid;
	public String playerFaction = "Patrick";
	
	public World()
	{
		Faction player = new Faction("Patrick", Color.red);
		Faction faggot = new Faction("Drew", Color.cyan);
		for(int i=0;i<15;i++)
			units.put(getUnusedID(), new UnitSoldier((int)(Math.random()*(Engine.width/Engine.boxSize)), (int)(Math.random()*(Engine.height/Engine.boxSize)), player));
		for(int i=0;i<5;i++)
			units.put(getUnusedID(), new UnitSoldier((int)(Math.random()*(Engine.width/Engine.boxSize)), (int)(Math.random()*(Engine.height/Engine.boxSize)), faggot));
	}
	
	public void update()
	{
		Object[] u = units.keySet().toArray();
		for(Object i : u)
		{
			if (units.get(i) != null) units.get(i).update();
		}
	}
	
	public void draw()
	{
		DrawHelper.useCamera = true;
		for(Integer i : units.keySet())
		{
			units.get(i).draw();
		}
	}
	
	public int getUnusedID()
	{
		int id = 0;
		while(units.containsKey(id))
		{
			id++;
		}
		return id;
	}
	
	public void add(GameUnit gu)
	{
		units.put(getUnusedID(), gu);
	}
	
	public void remove(GameUnit gu)
	{
		int id = -1;
		for(Integer i : units.keySet())
			if (units.get(i) == gu)
				id = i;
		if (id != -1)
			units.remove(id);
	}
}
