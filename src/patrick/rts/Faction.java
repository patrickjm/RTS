package patrick.rts;

import java.awt.Color;
import java.util.HashMap;

public class Faction 
{
	public Color color;
	public HashMap<Faction, ERelationshipType> relations = new HashMap<Faction, ERelationshipType>();
	public String name;
	public Faction(String name, Color color)
	{
		this.name = name;
		this.color = color;
	}
}
