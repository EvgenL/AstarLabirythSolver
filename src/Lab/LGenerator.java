package Lab;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class LGenerator {

	public static void main(String[] args) {
		//здесь параметры
	}

}

class Labyrith{
	
	Cell[][] map;
	
	Random random = new Random();
	
	public boolean debug = false;
	
	public Labyrith(int w, int h) {

		map = new Cell[w][h];
		
		}
	
	void generateLabyrith() {
		Cell[] previousLine = new Cell[map[0].length];
		instanciateCells(previousLine);

		for (int i = 0; i < map.length; i++) { //последнюю строку не рассматрваем
			Cell[] line = previousLine;
			deleteBorders(line);
			assignSets(line);
			randomlyAddRightBorders(line);
			randomlyAddBottomBorders(line);
			
			if (i + 1 >= map.length) {
				finallize(line);
			}
			map[i] = putToMap(line);
			previousLine = line;
			if (debug) debugPrint(line);
		}
	}

	void instanciateCells(Cell[] line) {
		for (int i = 0; i < line.length; i++) {
			line[i] = new Cell();
		}
	}
	
	void debugPrint(Cell[] line) {
		for (int i = 0; i < line.length; i++) {
			System.out.printf("%2d%s",(line[i].set.hashCode() & 99), (line[i].rightBorder == 1 ? " |" : "  "));
		}
		System.out.println();
		for (int i = 0; i < line.length; i++) {
			System.out.print((line[i].bottomBorder == 1 ? "--" : "  ") + "==");
		}
		System.out.println();
	}

	Cell[] putToMap(Cell[] line) {
		Cell[] output = new Cell[line.length];
		Cell temp;
		for (int i = 0; i < line.length; i++) {
			temp = new Cell();
			temp.bottomBorder = line[i].bottomBorder;
			temp.rightBorder = line[i].rightBorder;
			output[i] = temp;
		}
		return output;
	}
	
	void randomlyAddRightBorders(Cell[] line) {
		for (int i = 0; i < line.length; i++) {
			if (random.nextBoolean() || i+1 == line.length || line[i].equalSet(line[i+1])) {
				//можем случайно добавить границу где хотим
				//для последнего элемента есть правая граница
				//для двух одного множества всегда нужно добавить границу во избежание циклов
				line[i].rightBorder = 1;
			}
			else if (i+1 < line.length) {
				//если решили не добавлять, объединим множества
				destroySet(line[i], line[i+1]);
			}
		}
	}
	
	void randomlyAddBottomBorders(Cell[] line) {
		for (int i = 0; i < line.length; i++) {
			if (random.nextBoolean()) {
				line[i].bottomBorder = 1;
			}
		}
		
		for (int i = 0; i < line.length; i++) {
			if (!line[i].doSetHasBottomExit()) {
				line[i].addRandomBottomExit();
			}
		}
	}
	
	void deleteBorders(Cell[] line) {
		for (int i = 0; i < line.length; i++) {
			line[i].rightBorder = 0;
			if (line[i].bottomBorder == 1) {
				line[i].bottomBorder = 0;
				line[i].deleteFromSet();
			}
		}
	}

	void assignSets(Cell[] line) {
		for (int i = 0; i < line.length; i++) {
			if (line[i].set == null) {
				line[i].createSet();
			}
		}
	}
	
	void finallize(Cell[] line) {
		for (int i = 0; i < line.length; i++) {
			if (i + 1 != line.length && !line[i].equalSet(line[i + 1])) {
				line[i].rightBorder = 0;
				destroySet(line[i], line[i+1]);
			}
			line[i].bottomBorder = 1;
		}
	}
	
	void destroySet(Cell cell1, Cell cell2) {
		for (Cell cell2SetMember : cell2.set) {
			cell1.addToThisSet(cell2SetMember);
		}
	}
	
	//TODO walls around: top, left
	public int[][] to2DArray(){
		int[][] labyrith;
		labyrith = new int[map.length*2][map[0].length*2];
		for (int i = 0; i < labyrith.length-1; i+=2) {
			for (int j = 0; j < labyrith[0].length-1; j+=2) {
				int[][] cell = map[i/2][j/2].to2DArray();
				labyrith[i][j] = cell[0][0];
				labyrith[i][j+1] = cell[0][1];
				labyrith[i+1][j] = cell[1][0];
				labyrith[i+1][j+1] = cell[1][1];
			}
		}
		return labyrith;
	}
	
	
	class Cell{
		int rightBorder = 0;
		int bottomBorder = 0;
		ArrayList<Cell> set;
		
		public Cell() {
			set = null;
		}
		
		boolean doSetHasBottomExit() {
			for (Cell cell : set) {
				if (cell.bottomBorder == 0) {
					return true;
				}
			}
			return false;
		}
		
		void addRandomBottomExit() {
			Cell cell = set.get(new Random().nextInt(
					set
					.size()));
			cell.bottomBorder = 0;
		}
		
		public void createSet() {
			set = new ArrayList<Cell>();
			set.add(this);
		}
		
		public boolean equalSet(Cell other) {
			return set.equals(other.set);
		}

		public void addToThisSet(Cell other) {
			set.add(other);
			other.set = this.set;
		}
		
		public void deleteFromSet() {
			set.remove(this);
			set = null;
		}
		
		public boolean isInSet() {
			return set.contains(this);
		}
		
		public int[][] to2DArray() {
			return new int[][] {
				{0, rightBorder},
				{bottomBorder, 1}
			};
		}
	}
	
}
