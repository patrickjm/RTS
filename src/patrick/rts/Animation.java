package patrick.rts;

import java.awt.Color;
import java.util.ArrayList;

public class Animation 
{
	public ArrayList<Sprite> frames = new ArrayList<Sprite>();
	public int frame = 0;
	public int delay = 5;
	public int _delay = 5;
	
	public Animation(Sprite[] frames, int delay)
	{
		for(int i=0;i<frames.length;i++)
			this.frames.add(frames[i]);
		this.delay = delay;
		_delay = delay;
	}
	
	public void update()
	{
		_delay--;
		if (_delay <= 0)
		{
			_delay = delay;
			frame++;
			if (frame >= frames.size()) frame = 0;
		}
	}
	
	public void draw(int x, int y)
	{
		frames.get(frame).draw(x, y);
	}
	
	public void draw(int x, int y, Color c)
	{
		frames.get(frame).draw(x, y, c);
	}
}