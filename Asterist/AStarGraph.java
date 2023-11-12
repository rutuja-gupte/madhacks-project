package Asterist;

import java.util.PriorityQueue;
import java.util.Stack;
public class AStarGraph extends AStarNode {
  
  public void lookPath(AStarNode[][] currentNode, XandYCoordinates destination) {
    System.out.println("The Path: ");
    Stack<XandYCoordinates> path = new Stack<>();
    
    int Xcoordinate = destination.X;
    int Ycoordinate = destination.Y;
    
    XandYCoordinates nextNode = currentNode[Xcoordinate][Ycoordinate].previous;
    
    do {
      path.push(new XandYCoordinates(Xcoordinate, Ycoordinate));
      nextNode = currentNode[Xcoordinate][Ycoordinate];
      Xcoordinate = nextNode.X;
      Ycoordinate = nextNode.Y;
    }
    while (currentNode[Xcoordinate][Ycoordinate].previous != nextNode);
    
    while (!path.empty()) {
      XandYCoordinates point = path.peek();
      path.pop();
      System.out.println("(" + point.X + "," + point.Y + ") ");
    }
  }
  
  public void searchPath(double[][] data, XandYCoordinates source, XandYCoordinates destination) {
    if (!validNode(data, source)) {
      System.out.println("Invalid source point.");
      return;
    }
    
    if (!validNode(data, destination)) {
      System.out.println("Invalid destination point.");
    }
    
    if (isFinalDestination(source, destination)) {
      System.out.println("This is the destination.");
    }
    
    boolean[][] visitedArray = new boolean[data.length][data[0].length];
    AStarNode[][] currentNode = new AStarNode[data.length][data[0].length];
    
    int i = source.X;
    int j = source.Y;
    currentNode[i][j] = new AStarNode(new XandYCoordinates(i, j), 0.0, 0.0, 0.0);
    
    PriorityQueue<ValueInformation> toBeVisitedList = new PriorityQueue<>((number1, number2) -> Math.round((int) (number1.value - number2.value)));
    
    while (!toBeVisitedList.isEmpty()) {
      ValueInformation pointInformation = toBeVisitedList.peek();
      i = pointInformation.i;
      j = pointInformation.j;
      
      toBeVisitedList.poll();
      visitedArray[i][j] = true;
      
      for (int x = -1; x <= 1; x++) {
        for (int y = -1; y <= 1; y++) {
          if (Math.abs(x) == Math.abs(y)) {
            continue;
          }
          XandYCoordinates neighbour = new XandYCoordinates(x + i, y + j);
          if (validNode(data, neighbour)) {
            if (currentNode[neighbour.X] == null) {
              currentNode[neighbour.X] = new AStarNode[data[0].length];
            } else if (currentNode[neighbour.X][neighbour.Y] == null) {
              currentNode[neighbour.X][neighbour.Y] = new AStarNode();
            } else if (isFinalDestination(neighbour, destination)) {
              currentNode[neighbour.X][neighbour.Y].previous = new XandYCoordinates(i, j);
              System.out.println("Reached the destination");
              lookPath(currentNode, destination);
              return;
            } else if (!visitedArray[neighbour.X][neighbour.Y]) {
              if (validNode(data, neighbour)) {
                double newFinalValue = currentNode[i][j].cost + 1.0 + calculateHeuristic(neighbour, destination);
                
                if (currentNode[neighbour.X][neighbour.Y].finalValue == -1
                    || currentNode[neighbour.X][neighbour.Y].finalValue > newFinalValue) {
                  
                  toBeVisitedList.add(new ValueInformation(newFinalValue, neighbour.X, neighbour.Y));
                  
                  currentNode[neighbour.X][neighbour.Y].cost = currentNode[i][j].cost + 1.0;
                  currentNode[neighbour.X][neighbour.Y].finalValue = newFinalValue;
                  currentNode[neighbour.X][neighbour.Y].previous = new XandYCoordinates(i, j);
                }
              }
            }
          }
        }
      }
    }
    System.out.println("Failed to find the Destination Cell");
  }
  
  public static void main(String[] args) {
    
    
    //0: The cell is blocked
    // 255: The cell is not blocked
    
    double[][] grid = {
        { 255,   0, 255, 255 },
        { 255,   0, 255, 255 },
        { 255, 255, 255, 255 },
        { 255,   0, 255, 255 }
    };
    
    
    // Start is the left-most upper-most corner
    XandYCoordinates src = new XandYCoordinates(0,0);
    //(8, 0);
    
    // Destination is the right-most bottom-most corner
    XandYCoordinates dest = new XandYCoordinates(3, 3);
    
    AStarGraph app = new AStarGraph();
    app.searchPath(grid, src, dest);
    
  }
}
