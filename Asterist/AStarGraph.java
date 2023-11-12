package Asterist;

import edu.princeton.cs.introcs.GrayscalePicture;

import java.util.PriorityQueue;
import java.util.Stack;

public class AStarGraph extends AStarNode {
  
  public static void main(String[] args) {
    
    // args[0] = filename
    // args[1] = src co y
    // args[2] = src x
    // args[3] = dest co y
    // args[4] = dest x
    
    // 0: The node is black colored - wall
    // 255: The node is white colored - open node

//    String filename = "twoRooms.jpg";
    
    String filename = args[0];
    GrayscalePicture source = new GrayscalePicture(filename);
    
    double[][] grid = new double[source.height()][source.width()];
    
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        grid[i][j] = source.getGrayscale(j, i);
      }
    }
    
    // Start is the left-most upper-most corner
    int sourceX = Integer.parseInt(args[2]);
    int sourceY = Integer.parseInt(args[1]);
    int destinationX = Integer.parseInt(args[4]);
    int destinationY = Integer.parseInt(args[3]);
    
    XandYCoordinates src = new XandYCoordinates(sourceX, sourceY);
    
    XandYCoordinates dest = new XandYCoordinates(destinationX, destinationY);
    
    Asterist.AStarGraph app = new Asterist.AStarGraph();
    app.searchPath(grid, src, dest);
    
  }
  
  public void printPath(AStarNode[][] currentNode, XandYCoordinates destination) {
    System.out.println("The Path: ");
    Stack<XandYCoordinates> path = new Stack<>();
    
    int Xcoordinate = destination.height;
    int Ycoordinate = destination.width;
    
    XandYCoordinates nextNode;
    
    do {
      path.push(new XandYCoordinates(Xcoordinate, Ycoordinate));
      nextNode = currentNode[Xcoordinate][Ycoordinate].previous;
      Xcoordinate = nextNode.height;
      Ycoordinate = nextNode.width;
    }
    while (currentNode[Xcoordinate][Ycoordinate].previous != nextNode);
    
    while (!path.empty()) {
      XandYCoordinates point = path.peek();
      path.pop();
      System.out.print("(" + point.height + "," + point.width + "), ");
    }
  }
  
  public void searchPath(double[][] data, XandYCoordinates source, XandYCoordinates destination) {
    // invalid node
    if (!validNode(data, source)) {
      System.out.println("Invalid source point.");
      return;
    }
    
    // invalid destination
    if (!validNode(data, destination)) {
      System.out.println("Invalid destination point.");
      return;
    }
    
    // isolated source
    if (!isPixelWhite(data, source)) {
      System.out.println("It is a blocked source");
      return;
    }
    
    
    // isolated destination
    else if (!isPixelWhite(data, destination)) {
      System.out.println("It is a blocked destination.");
      return;
    }
    
    // pixel is destination
    if (isFinalDestination(source, destination)) {
      System.out.println("This is the destination.");
    }
    
    // else, check neighbours
    boolean[][] visitedArray = new boolean[data.length][data[0].length];
    AStarNode[][] runner = new AStarNode[data.length][data[0].length];
    
    // initializing height and width
    int height = source.height;
    int width = source.width;
    runner[height][width] = new AStarNode(new XandYCoordinates(height, width), 0.0, 0.0, 0.0);
    
    // add each neighbouring edge to a priority queue
    PriorityQueue<ValueInformation> toBeVisitedList = new PriorityQueue<>((number1, number2) ->
        (int) Math.round((number1.value - number2.value)));
    
    toBeVisitedList.add(new ValueInformation(0.0, height, width));
    
    
    // while priority queue has nodes
    while (!toBeVisitedList.isEmpty()) {
      ValueInformation pointInformation = toBeVisitedList.peek();
      int pointHeight = pointInformation.i;
      int pointWidth = pointInformation.j;
      
      // remove head
      toBeVisitedList.poll();
      visitedArray[pointHeight][pointWidth] = true;
      
      // Loop to go through the neighbours that are on the four directions
      for (int xNeigh = -1; xNeigh <= 1; xNeigh++) {
        for (int yNeigh = -1; yNeigh <= 1; yNeigh++) {
          
          // Getting the neighbour coordinates
          XandYCoordinates neighbour =
              new XandYCoordinates(xNeigh + pointHeight, yNeigh + pointWidth);
          
          // Checking if the neighbour is valid in accordance with the data
          if (validNode(data, neighbour)) {
            // if the currentNode
            if (runner[neighbour.height] == null) {
              runner[neighbour.height] = new AStarNode[data[0].length];
            }
            if (runner[neighbour.height][neighbour.width] == null) {
              runner[neighbour.height][neighbour.width] = new AStarNode();
            }
            
            if (isFinalDestination(neighbour, destination)) {
              runner[neighbour.height][neighbour.width].previous =
                  new XandYCoordinates(pointHeight, pointWidth);
              System.out.println("Reached the destination");
              printPath(runner, destination);
              return;
            }
            else if (!visitedArray[neighbour.height][neighbour.width] &&
                isPixelWhite(data, neighbour)) {
              double costNew = runner[pointHeight][pointWidth].cost + 1.0;
              double newHeuristic = calculateHeuristic(neighbour, destination);
              double newFinalValue = costNew + newHeuristic;
              
              if (runner[neighbour.height][neighbour.width].finalValue == -1
                  || runner[neighbour.height][neighbour.width].finalValue > newFinalValue) {
                
                toBeVisitedList.add(new ValueInformation(newFinalValue, neighbour.height,
                    neighbour.width));
                
                runner[neighbour.height][neighbour.width].cost = costNew;
                runner[neighbour.height][neighbour.width].finalValue = newFinalValue;
                runner[neighbour.height][neighbour.width].previous =
                    new XandYCoordinates(pointHeight, pointWidth);
              }
            }
          }
        }
      }
    }
    System.out.println("Failed to find the Destination");
  }
}
