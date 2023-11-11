package Asterist;

public class AStarNode extends XandYCoordinates {
  
  public XandYCoordinates previous;
  
  public double heuristic;
  
  public double cost;
  
  public double finalValue;
  
  public AStarNode(){
    super(-1, -1);
    heuristic = -1;
    cost = -1;
    finalValue = -1;
  }
  
  public AStarNode(XandYCoordinates previous, double cost, double heuristic, double finalValue){
    this();
    this.previous = previous;
    this.heuristic = heuristic;
    this.cost = cost;
    this.finalValue = finalValue;
  }
  
  public boolean validNode(double[][] data, XandYCoordinates node){
    if(data.length > 0 && data[0].length > 0){
      if(node.X >= 0 && node.X < data.length){
        if(node.Y >= 0 && node.Y < data[0].length)
          if(data[node.X][node.Y] == 255){
            return true;
          }
      }
    }
    return false;
  }
  
//  public boolean isNotFull(double[][] data, XandYCoordinates node){
//    if(validNode(data, node)){
//      if(data[node.X][node.Y] == 255){
//        return true;
//      }
//    }
//    return false;
//  }
  
  public boolean isFinalDestination(XandYCoordinates currentNode, XandYCoordinates destination){
    return(currentNode.equals(destination));
  }
  
  public double calculateHeuristic(XandYCoordinates source, XandYCoordinates destination){
    double heuristic;
    double intermediate1 = Math.pow((source.X - destination.X), 2);
    double intermediate2 = Math.pow((source.Y - destination.Y), 2);
    heuristic = Math.sqrt(intermediate1 + intermediate2);
    return heuristic;
  }
}
