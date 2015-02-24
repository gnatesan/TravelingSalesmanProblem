/* Gokul Natesan
 * This program is used to analyze two different ways to solving the Traveling Salesman Problem.
 * Looks at the Nearest-Neighbor approach and the Exhaustive Search approach, and shows the 
 * run time of both solutions. 
 */
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.lang.Math;

public class Search {
	private int numPoints;
	private int xPoint;
	private int yPoint;
	private double minDistance;
	private double distanceTraveled;
	private double optimalTotalDistance;
	private ArrayList <Point> list = new ArrayList<Point>();
	private ArrayList <ArrayList<Point>> allOrders = new ArrayList <ArrayList<Point>>();
	
	//function to create random points and store it in a text file.
	//Only needed if there is no input text file.
	public void createFile() {
		Random r = new Random();
		numPoints = 10000; 
		FileWriter fw;
		try {
			fw = new FileWriter(new File("points.txt"));
			fw.write(Integer.toString(numPoints));
			fw.write(System.getProperty("line.separator"));
			for (int i = 0; i < numPoints; i++) {
				xPoint = r.nextInt(50);
				yPoint = r.nextInt(50);
				fw.write(Integer.toString(xPoint) + " " + Integer.toString(yPoint));
				fw.write(System.getProperty("line.separator"));
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//Read through input file and take each pair of x and y coordinates and create a Point object.
	//Then, store the point object in an array list. 
	public void readFile(String readFile){
		File fr = new File(readFile);
		Scanner in;
		try {
			in = new Scanner(fr);
			numPoints = in.nextInt();
			while (in.hasNextLine() && in.hasNextInt()) {
				xPoint = in.nextInt();
				yPoint = in.nextInt();
				list.add(new Point(xPoint, yPoint));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Begins with the starting point and looks through the rest of the points not visited
	//and finds the nearest point to it. Updates the distance traveled simultaneously. 
	public void nearestNeighborSearch() {
		System.out.println("NEAREST NEIGHBOR SEARCH: ");
		for (int i = 0; i < list.size() - 1; i++) {
			minDistance = 999;
			int store = i+1;
			for (int j = i+1; j < list.size(); j++) {
				if (pointDistance(list.get(i), list.get(j)) < minDistance) {
					minDistance = pointDistance(list.get(i), list.get(j));
					store = j;
				}
			}
			Point temp = list.get(i+1);
		    list.set(i+1, list.get(store));
			list.set(store, temp);
			distanceTraveled += minDistance;
		}
		distanceTraveled += pointDistance(list.get(0), list.get(list.size() -1));
		list.add(list.get(0));
		for (Point p : list) {
			System.out.println(p.getX() + " " + p.getY());
		}
		System.out.println("distance of path is: " + distanceTraveled);
	}
	
	//Looks at all possible combinations of paths and prints out the most ideal path and its distance traveled.
	public void exhaustiveSearch() {
		int index = this.indexOfBestArrangement();
		System.out.println("EXHAUSTIVE SEARCH: ");
		allOrders.get(index).add(allOrders.get(index).get(0));
		for (Point p : allOrders.get(index)) {
			System.out.println(p.getX() + " " + p.getY());
		}
		System.out.println("distance of path is: " + optimalTotalDistance);
	}
	
	//Calculates the distance between two points. 
	public double pointDistance(Point p1, Point p2) {
		double xRoot = Math.pow((p1.getX() - p2.getX())*1.0, 2.0);
		double yRoot = Math.pow((p1.getY() - p2.getY())*1.0, 2.0);
		return Math.sqrt(xRoot + yRoot);
	}
	
	//Calculates all possible permutations of the points in the array list. Stores each different
	//permutation as its own array list in one big array list. 
	public ArrayList<ArrayList<Point>> permute(ArrayList <Point> pt) {
		ArrayList<ArrayList<Point>> result = new ArrayList<ArrayList<Point>>();
	 
		//start from an empty list
		result.add(new ArrayList<Point>());
	 
		for (int i = 0; i < pt.size(); i++) {
			//list of list in current iteration of the array pt
			ArrayList<ArrayList<Point>> current = new ArrayList<ArrayList<Point>>();
	 
			for (ArrayList<Point> l : result) {
				// # of locations to insert is largest index + 1
				for (int j = 0; j < l.size()+1; j++) {
					// + add pt[i] to different locations
					l.add(j, pt.get(i));	 
					ArrayList<Point> temp = new ArrayList<Point>(l);
					current.add(temp);
					l.remove(j);
				}
			}
	 
			result = new ArrayList<ArrayList<Point>>(current);
		}
		return result;
	}
	
	//Goes through each possible permutation finding out the order of points with the shortest 
	//distance traveled and returns the index of that permutation in the array list from which
	//the permutations are stored. Used for exhaustive search algorithm. 
	public int indexOfBestArrangement() {
		optimalTotalDistance = 9999.9;
		double currentDistance;
		int location = -1;
		int i;
		allOrders = this.permute(list);
		for (ArrayList<Point> check : allOrders) {
			currentDistance = 0;
			for (i = 0; i < check.size() - 1; i++) {
				currentDistance += pointDistance(check.get(i), check.get(i+1));
			}
			currentDistance += pointDistance(check.get(check.size() - 1), check.get(0));
			if (currentDistance < optimalTotalDistance) {
				optimalTotalDistance = currentDistance;
				location = i;
			}
		}
		return location;
	}
	
	//Read set of points from an input file, and call an algorithm to see its results and run time.
	public static void main (String[] args) {
		Search test = new Search();
		//test.createFile();
		//test.readFile("input2copy.txt");
		test.readFile("input1copy.txt");
		long startTime = System.currentTimeMillis();
		test.nearestNeighborSearch();
		//test.exhaustiveSearch();
		long duration = System.currentTimeMillis() - startTime;
		System.out.println("Time in milliseconds: " + duration);
		
	}
}

