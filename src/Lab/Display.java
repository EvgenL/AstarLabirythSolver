package Lab;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import Lab.Seeker.*;
public class Display {

	//параметры - размер экрана
	public int screenH = 1080;
	public int screenW = 1920;
	
	int frameWidth;
	int frameHeight;
	public boolean drawInfo = false;
	private Node[][] map;
	
	public void updateMap(Node[][] map) {
		this.map = map;
	}
	
	int mapHeight;
	int mapWidth;
	int blockHeight;
	int blockWidth;
	
	GridDrawing gridDrawing;

	public boolean drawGrid = false;
	public boolean squareGrid = true;
	
	public Display(){
	}
	
	/*public Display(int width, int height, Node[][] map){
		this.map = map;
		frameWidth = width;
		frameHeight = height;
		mapHeight = map.length;
		mapWidth = map[0].length;
		newFrame();
	}*/
	
	public Display(Node[][] map) {
		this.map = map;
		mapHeight = map.length;
		mapWidth = map[0].length;
		frameWidth = mapHeight * 20;
		frameHeight = mapWidth * 20;
		
		if (frameWidth > screenW || frameHeight > screenH) {
			frameWidth = Math.min(screenW, frameWidth);
			frameHeight = Math.min(screenH, screenH);
			blockHeight = frameHeight / mapHeight;
			blockWidth = (squareGrid ? blockHeight : frameWidth / mapWidth);
		}
		else {
			blockHeight = 20;
			blockWidth = (squareGrid ? blockHeight : frameWidth / mapWidth);
		}
		newFrame();
	}
	
	public void newFrame() {
		JFrame frame = new JFrame("Окно");
		frame.setSize(frameWidth+17, frameHeight+40); //Параметры подобраны эксперимантально

		gridDrawing = new GridDrawing();
		frame.getContentPane().add(gridDrawing);
		frame.setVisible(true);
	}
	
	public void update() {
		gridDrawing.draw();
	}
	
	class GridDrawing extends JComponent {

		int blockHeight = frameHeight / mapHeight;
		int blockWidth = (squareGrid ? blockHeight : frameWidth / mapWidth);
		
		Graphics2D g2d;
		Graphics g;
		
		Color[] colors = new Color[]{
				new Color(0.3f, 0.3f, 0.3f), //bg
				Color.BLACK, //wall
				new Color(255, 217, 66), //start
				new Color(190, 225, 120), //end
				new Color(1f, 1f, 1f), //closed //Color.yellow,//new Color(225, 139, 114), //closed
				new Color(1f, 1f, 1f), //discovered
				Color.RED//Color.PINK //path
				};
		
		public GridDrawing() {
		}
		
		protected void paintComponent(Graphics g) {
			this.g = g;
			g2d = (Graphics2D)g;
			g2d.setStroke(new BasicStroke(1.0f));
			
			draw();
		}
		
		private void draw() {
			super.repaint();
			drawRects();
			if (drawGrid) 
				drawGrid();
		}
		
		private void drawGrid() {
			g2d.setColor(Color.BLACK);
			for (int i = 0; i < mapHeight; i++) {
				for (int j = 0; j < mapWidth; j++) {
						g2d.drawRect(j*blockWidth, i*blockHeight, blockWidth, blockHeight);
				}
			}
		}
		
		private void drawRects() {
			for (int i = 0; i < mapHeight; i++) {
				for (int j = 0; j < mapWidth; j++) {
					g2d.setColor(colors[map[i][j].nodeType]);
					g2d.fillRect(j*blockWidth, i*blockHeight, blockWidth, blockHeight);
					if (drawInfo) {
					g2d.setColor(colors[1]);
					g2d.drawString("x "+map[i][j].x+" y "+map[i][j].y, j*blockWidth+10, i*blockHeight+15);
					g2d.drawString("f="+toPointTwo(map[i][j].fScore), j*blockWidth+10, i*blockHeight+26);
					g2d.drawString("g="+toPointTwo(map[i][j].gScore), j*blockWidth+10, i*blockHeight+38);
					g2d.drawString("h="+toPointTwo(map[i][j].hScore), j*blockWidth+10, i*blockHeight+50);
					}
				}
			}
		}
		
		private String toPointTwo(double n) {
			return String.format("%.2f", n);
		}
	}

	
}
 



