package patrick.rts;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Engine extends JPanel  
{
	private static final long serialVersionUID = 1L;
	static final int boxSize = 2;
	static final int screenWidth = 640;
	static final int screenHeight = 480;
	static final int width = screenWidth / boxSize;
	static final int height = screenHeight / boxSize;
	
	static int frameSkip = 1;
	static int _frameSkipCount = 0;
	
	static Color[][] colors = new Color[width][height];
	
	public boolean running = true;
	public JFrame frame;
	public static Engine singleton;
	
	private BufferedImage bi = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
	private Graphics2D canvas;
	private Rectangle area;
	
	private boolean firstTime = true;

	public Engine() {
		setBackground(Color.black);
		singleton = this;
		Game.singleton = new Game();
		
		for(int x=0;x<width;x++)
		{
			for(int y=0;y<height;y++)
			{
				colors[x][y] = new Color(0, 0, 0, 1);
			}
		}
		
		addKeyListener(Input.keyboard);
		addMouseListener(Input.mouse);
		addMouseMotionListener(Input.mouse);
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");
		setCursor(blankCursor);
	}
	public void paint(Graphics g) {
		update(g);
	}

	public void update(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		if (firstTime) {
			Dimension dim = getSize();
			int w = dim.width;
			int h = dim.height;
			area = new Rectangle(dim);
			bi = (BufferedImage) createImage(w, h);
			canvas = bi.createGraphics();
			firstTime = false;
		}
		
		Input.mouse.poll();
		Input.keyboard.poll();
		Game.singleton.update();
		_frameSkipCount++;
		if(_frameSkipCount >= frameSkip)
		{
			canvas.setColor(Color.black);
			canvas.fillRect(0, 0, area.width+10, area.height+10);
			clear();
			
			Game.singleton.draw();
			_frameSkipCount = 0;
		}
		
		for(int x=0;x<width;x++)
		{
			for(int y=0;y<height;y++)
			{	
				if (colors[x][y] != null)
				{
					canvas.setColor(colors[x][y]);
					canvas.fillRect(x*boxSize, y*boxSize, boxSize, boxSize);
				}
			}
		}
		g2.drawImage(bi, 0, 0, this);
	}

	public static void main(String s[]) {
		JFrame f = new JFrame("RTS");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(new Engine(), "Center");
		f.pack();
		f.setSize(new Dimension(screenWidth, screenHeight));
		f.show();
		f.addKeyListener(Input.keyboard);
		f.addMouseListener(Input.mouse);
		f.addMouseMotionListener(Input.mouse);
		//f.setResizable(false);
		Engine.singleton.frame = f;
		
		new Thread(new Runnable() {
			public void run()
			{
				while(Engine.singleton.running)
				{
					double fps = 100;
					try {
						Thread.sleep((long)(100 / (fps / 10)));
					} catch (InterruptedException e) {}
					Engine.singleton.update(Engine.singleton.getGraphics());
				}
			}
		}).start();
	}
	
	public void clear()
	{
		for(int x=0;x<width;x++)
		{
			for(int y=0;y<height;y++)
			{
				colors[x][y] = null;
			}
		}
	}
}