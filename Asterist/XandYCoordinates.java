package Asterist;

public class XandYCoordinates {
  
  int X; // X coordinate of the image
  int Y; // Y coordinate of the image
  
  public XandYCoordinates(int X, int Y) {
    this.X = X;
    this.Y = Y;
  }
  
  @Override
  public boolean equals(Object object) {
    if (object instanceof Asterist.XandYCoordinates)
      if (this.X == ((Asterist.XandYCoordinates) object).X && this.Y == ((Asterist.XandYCoordinates) object).Y)
        return true;
    return false;
  }
}
