package Lab;

import java.awt.Color;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class pathFinder {

}

class Seeker {
	
	public int pauseLength = 300;
	
	ArrayList<Node> discovered;
	ArrayList<Node> closed;
	
	int[][] map;
	Node[][] graph;
	
	Display display;
	
	public boolean debug = false;

	public Seeker(Display display) {
		this.display = display;
	}
	
	public Seeker(int[][] map) {
		this.map = map;
		graph = new Node[map.length][map[0].length];
		mapToGraph();
		Display display = new Display(graph);
		display.drawGrid = true;
		this.display = display;
	}

	public boolean find(int startX, int startY, int goalX, int goalY) {
		return find(new Node(startX, startY), new Node(goalX, goalY));
	}
	
	public boolean find(Node start, Node goal) {
		
		start.cameFrom = null;
		start.gScore = 0; 
		start.hScore = distance(start, goal);
		start.fScore = start.gScore + start.hScore;

		discovered = new ArrayList<Node>();
		closed = new ArrayList<Node>();
		discovered.add(start);
		start.nodeType = NodeType.start; //Это только для дисплея
		goal.nodeType = NodeType.goal; //Это только для дисплея
		
		while(!discovered.isEmpty()) {
			Node current = lowestFScore(discovered);
			if (current.equals(goal)) {
				reconstructPath(current, start);
				return true;
			}
			discovered.remove(current);
			closed.add(current);
			current.nodeType = NodeType.closed;
			ArrayList<Node> currentNeighbours = getNeighbours(current);
			for (Node neighbour : currentNeighbours) {
				if (!discovered.contains(neighbour)) { //создать если не был обнаружен
					discovered.add(neighbour);
					neighbour.nodeType = NodeType.discovered;
					neighbour.cameFrom = current;
					neighbour.gScore = distance(neighbour, current) + neighbour.gScore;
					neighbour.hScore = distance(neighbour, goal);
					neighbour.fScore = neighbour.gScore + neighbour.hScore;
					
				}
				else { //update соседа
					double tentative_gScore = current.gScore - distance(neighbour, current);
					if (tentative_gScore >= neighbour.gScore) {
						neighbour.cameFrom = current;
						neighbour.gScore = tentative_gScore;
						neighbour.hScore = distance(neighbour, goal);
						neighbour.fScore = neighbour.gScore + neighbour.hScore;
					}
				}
			}
			reconstructPathShow(current, start);
			updateDisplay();
			if (debug) System.out.printf("Node: [%d,%d]. g=%.2f, h=%.2f\n", current.x, current.y, current.gScore, current.hScore);
		}
		return false;
	}
	
	private void reconstructPath(Node lastNode, Node firstNode) {
		
		Node current = lastNode;
		current.nodeType = NodeType.goal;
		clearPathDrawing();
		while (!current.equals(firstNode)) {
			current = current.cameFrom;
			current.nodeType = NodeType.path;
			try {
				Thread.sleep(pauseLength);
			} catch (InterruptedException e) {}
		}
		firstNode.nodeType = NodeType.start;
		updateDisplay();
	}
	
	private void clearPathDrawing() {
		for (int x = 0; x < graph.length; x++) {
			for (int y = 0; y < graph[0].length; y++) {
				if (graph[x][y].nodeType == NodeType.path)
					graph[x][y].nodeType = NodeType.discovered;
			}
		}
	}
	
	private void reconstructPathShow(Node lastNode, Node firstNode) {
		
		Node current = lastNode;
		clearPathDrawing();
		while (!current.equals(firstNode)) {
			current = current.cameFrom;
			current.nodeType = NodeType.path;
		}
		firstNode.nodeType = NodeType.start;
		updateDisplay();
	}
	
	private void updateDisplay() {
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				map[x][y] = graph[x][y].nodeType;
			}
		}
		try {
			Thread.sleep(pauseLength);
		} catch (InterruptedException e) {}
	}
		

	private static double distance(Node from, Node to) {
		return Math.sqrt(Math.pow((to.x - from.x), 2) + Math.pow(to.y - from.y, 2));
	}
	
	private void mapToGraph() {
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				Node node = new Node(x, y, map[x][y]);
				if (graph[x][y] == null || !graph[x][y].equals(node)) {
					graph[x][y] = node;
				}
			}
		}
	}
	
	private ArrayList<Node> getNeighbours(Node node){
		ArrayList<Node> neighbours = new ArrayList<Node>();
		for (int i = node.x - 1; i <= node.x + 1; i++) {
			for (int j = node.y - 1; j <= node.y + 1; j++) {
				if (isInMap(i, j) && !closed.contains(graph[i][j]) &&
						graph[i][j].nodeType != NodeType.wall){
							neighbours.add(graph[i][j]);
						}
			}
		}
		return neighbours;
	}
	
	private boolean isInMap(int x, int y) {
		return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
	}
	
	private Node lowestFScore(ArrayList<Node> set) {
		Node lowest = set.get(0);
		for (Node node : set) {
			if (node.fScore < lowest.fScore) {
				lowest = node;
			}
		}
		return lowest;	
	}
	
	
	class Node{
		int x;
		int y;
		double gScore; //путь из начала 
		double hScore; //оставшийся путь до конца
		double fScore;
		Node cameFrom;
		int nodeType;

		Node(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		Node(int x, int y, int type){
			this.x = x;
			this.y = y;
			nodeType = type;
		}
		
		public boolean equals(Node other) {
			return x == other.x && y == other.y;
		}
		
	}
	
	static class NodeType{
		/**
		 * 0 - пол
		 * 1 - стена
		 * 2 - старт
		 * 3 - финиш
		 * 4 - клосед
		 * 5 - дискаверед
		 * 6 - путь
		 */
		public static final int floor = 0;
		public static final int wall = 1;
		public static final int start = 2;
		public static final int goal = 3;
		public static final int closed = 4;
		public static final int discovered = 5;
		public static final int path = 6;
	}
}
