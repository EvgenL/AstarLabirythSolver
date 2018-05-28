package Lab;


public class LabyrithSolver {

	public static void main(String[] args) {
		//Labyrith labyrith = new Labyrith(85, 160);
		Labyrith labyrith = new Labyrith(40, 40);
		labyrith.generateLabyrith();
		int[][] map = labyrith.to2DArray();

		int startX = 0;
		int startY = 0;
		int goalX = map.length-2;
		int goalY = map[0].length-2;
		
		map[startX][startY] = 2;
		map[goalX][goalY] = 3;

		Seeker seeker = new Seeker(map);
		
		seeker.pauseLength = 9;
		
		seeker.display.drawGrid = false;
		
		seeker.find(startX, startY, goalX, goalY);
		try {
			Thread.sleep(99900);
		} catch (InterruptedException e) {}
		
	}

}
