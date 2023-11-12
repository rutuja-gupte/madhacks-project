package Asterist;

public class XandYCoordinates {
  
  public int height; // height coordinate of the image
  public int width; // width coordinate of the image
  
  public XandYCoordinates(int height, int width) {
    this.height = height;
    this.width = width;
  }
  
  @Override
  public boolean equals(Object object) {
    if (object instanceof Asterist.XandYCoordinates)
      if (this.height == ((Asterist.XandYCoordinates) object).height && this.width == ((Asterist.XandYCoordinates) object).width)
        return true;
    return false;
  }
}
