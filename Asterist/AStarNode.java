package Asterist;
public class AStarNode {
  
  public XandYCoordinates previous;
  
  public double heuristic;
  
  public double cost;
  
  public double finalValue;
  
  public AStarNode(){
    previous = new XandYCoordinates(-1, -1);
    heuristic = -1;
    cost = -1;
    finalValue = -1;
  }
  
  public AStarNode(XandYCoordinates previous, double cost, double heuristic, double finalValue){
    this.previous = previous;
    this.heuristic = heuristic;
    this.cost = cost;
    this.finalValue = finalValue;
  }
  
  public boolean validNode(double[][] data, XandYCoordinates node){
    if(data.length > 0 && data[0].length > 0){
      if(node.height >= 0 && node.height < data.length){
        if(node.width >= 0 && node.width < data[0].length)
          return true;
      }
    }
    return false;
  }
  
  public boolean isPixelWhite(double[][] data, XandYCoordinates node){
    if(validNode(data, node)){
      if(data[node.height][node.width] == 255){
        return true;
      }
    }
    return false;
  }
  
  public boolean isFinalDestination(XandYCoordinates currentNode, XandYCoordinates destination){
    return(currentNode.equals(destination));
  }
  
  public double calculateHeuristic(XandYCoordinates source, XandYCoordinates destination){
    double heuristic;
    double intermediate1 = Math.pow((source.height - destination.height), 2);
    double intermediate2 = Math.pow((source.width - destination.width), 2);
    heuristic = Math.sqrt(intermediate1 + intermediate2);
    return heuristic;
  }
}
