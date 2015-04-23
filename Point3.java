/*
   Point3.java
 
   Created on marts 11, 2010 (Morten Rhiger <mir@ruc.dk>)
   This code has been expanded by Thibault Lefebvre and Benoit Bonnet.
*/

/** Objects of this class represents points in 3 dimensions. */
public class Point3 
{

  /** For convenience, the coordinates of a point are stored in public
      fields.  Don't modify them. */
  public double x, y, z;

  /** Constructs a new point. */
  public Point3(double x, double y, double z) 
  {
    this.x = x; this.y = y; this.z = z;
  }


  /** Adding a vector to a point (a translation).  Creates a new point
      by adding a vector to an existing point. */
  public Point3 add(Vector3 v) 
  {
    return new Point3(x + v.x, y + v.y, z + v.z);
  }

  /** Point subtraction.  Creates a new vector by subtracting two
      points. */
  public Vector3 subtract(Point3 p) 
  {
    return new Vector3(x - p.x, y - p.y, z - p.z);
  }

  /** Returns a vector pointing from the origin (0, 0, 0) to this
      point. */
  public Vector3 toVector3() 
  {
    return new Vector3(x, y, z);
  }
}
