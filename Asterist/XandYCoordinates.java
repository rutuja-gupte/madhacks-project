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
    if (object instanceof XandYCoordinates)
      if (this.X == ((XandYCoordinates) object).X && this.Y == ((XandYCoordinates) object).Y)
        return true;
    return false;
  }
}
