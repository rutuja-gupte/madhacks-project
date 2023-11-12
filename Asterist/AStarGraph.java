package Asterist;

import Asterist.XandYCoordinates;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Stack;

public class AStarGraph extends AStarNode {
  
  public static void main(String[] args) {
    
    // args[0] = filename
    // args[1] = src co y - height
    // args[2] = src x - width
    // args[3] = dest co y - height
    // args[4] = dest x - width
    
    // 0: The node is black colored - wall
    // 255: The node is white colored - open node
    
    String filename = args[0];
    GrayscalePicture source = new GrayscalePicture(filename);
    
    double[][] grid = new double[source.height()][source.width()];
    
    for (int i = 0; i < source.height(); i++) {
      for (int j = 0; j < source.width(); j++) {
        grid[i][j] = source.getGrayscale(j, i);
      }
    }
    
    // Start is the top left corner
    int sourceY = Integer.parseInt(args[1]); // height
    int sourceX = Integer.parseInt(args[2]); // width
    
    int destinationY = Integer.parseInt(args[3]); // height
    int destinationX = Integer.parseInt(args[4]); // width
    
    
    XandYCoordinates src = new XandYCoordinates(sourceX, sourceY);
    
    XandYCoordinates dest = new XandYCoordinates(destinationX, destinationY);
    
    AStarGraph app = new AStarGraph();
    app.searchPath(grid, src, dest);
    
  }
  
  public void printPath(AStarNode[][] currentNode, XandYCoordinates destination) {
    String directions = "";
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
    
    String[] navigations = new String[path.size()];
    int startCase = 1;
    int numIterations = 0;
    
    while (!path.empty()) {
      XandYCoordinates currPoint = path.peek();
      path.pop();
      if (!path.empty()) {
        XandYCoordinates nextPoint = path.peek();
        
        XandYCoordinates[] pathArr = new XandYCoordinates[path.size()];
        path.copyInto(pathArr);
        
        navigations[numIterations] = directions(pathArr, nextPoint, currPoint);
        
        // for start node
        if (startCase > 0) {
          navigations[startCase - 1] = "Face East and " + firstCase(currPoint, nextPoint);
          startCase--;
        }
        numIterations++;
      }
      
      System.out.print("(" + currPoint.height + "," + currPoint.width + "), ");
    }
    System.out.println();
    System.out.println(Arrays.toString(navigations));
  }
  
  private String firstCase(XandYCoordinates curr, XandYCoordinates next) {
    
    double angle =
        Math.atan2(next.height, next.width);
    
    angle = Math.toDegrees(angle);
    
    return getDirection((int) angle);
  }
  
  private String directions(XandYCoordinates[] coordinatesStack, XandYCoordinates nextPoint,
                            XandYCoordinates currPoint) {
    double angle = 0;
    
    if (coordinatesStack.length >= 2) {
      angle = calculateAngle(currPoint, nextPoint, coordinatesStack[1]);
    }
    
    return getDirection((int) angle);
  }
  
  private String getDirection(int angle) {
    if (Math.abs(angle) >= 0 && Math.abs(angle) <= 10) {
      return "Head Straight";
    }
    else if (Math.abs(angle) >= 20 && Math.abs(angle) <= 70) {
      if (angle >= 0.0) {
        return "Move diagonally right!";
      }
      else {
        return "Move diagonally left!";
      }
    }
    else if (Math.abs(angle) >= 80 && Math.abs(angle) <= 100) {
      if (angle >= 0.0) {
        return "Turn right!";
      }
      else {
        return "Turn left!";
      }
    }
    else {
      return "Head Straight";
    }
  }

//  private double calculateAngle(XandYCoordinates curr, XandYCoordinates next,
//                                XandYCoordinates nextNext) {
//    double[] vecA = {next.width - curr.width, next.height - curr.height};
//    double[] vecB = {nextNext.width - next.width, nextNext.height - next.height};
//
//    double dotProduct = (vecA[0] * vecB[0]) + (vecA[1] * vecB[1]);
//    double magA =
//        Math.sqrt(Math.pow(vecA[0], 2) + Math.pow(vecA[1], 2));
//    double magB =
//        Math.sqrt(Math.pow(vecB[0], 2) + Math.pow(vecB[1], 2));
//
//
//    double alpha = Math.acos(dotProduct / (magA * magB));
//
//    return Math.round(Math.toDegrees(alpha));
//  }
  
  private double calculateAngle(XandYCoordinates curr, XandYCoordinates next,
                                XandYCoordinates nextNext) {
//    double[] vecA = {next.width - curr.width, next.height - curr.height};
//    double[] vecB = {nextNext.width - next.width, nextNext.height - next.height};
    
    double[] vecA = {curr.width, curr.height};
    double[] vecB = {next.width, next.height};
    
    double dotProduct = (vecA[0] * vecB[0]) + (vecA[1] * vecB[1]);
    double magA = Math.hypot(vecA[0], vecA[1]);
    double magB = Math.hypot(vecB[0], vecB[1]);
    
    double alpha = Math.acos(dotProduct / (magA * magB));
    
    return Math.toDegrees(alpha);
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
      System.out.println("The source is blocked");
      return;
    }
    
    
    // isolated destination
    else if (!isPixelWhite(data, destination)) {
      System.out.println("The destination is blocked.");
      return;
    }
    
    // pixel is destination
    if (isFinalDestination(source, destination)) {
      return;
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

