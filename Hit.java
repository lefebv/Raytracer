/*
   Hit.java
 
   Created on marts  7, 2010 (Morten Rhiger <mir@ruc.dk>)
   This code has been expanded by Thibault Lefebvre and Benoit Bonnet.
*/

/** Objects of this class represent the intersection of a ray with an
    object.  */
public class Hit 
{
  /** The location (in the scene) of the intersection point

      This fields is public, to support convinient access to it.
      However, it should not be modified from outside this class. */
  public Point3 point;

  /** The distance from the origin of the ray to the point of
      intersection.

      This fields is public, to support convinient access to it.
      However, it should not be modified from outside this class. */
  public double distance;

  /** The normal to the surface at the point of intersection.

      This fields is public, to support convinient access to it.
      However, it should not be modified from outside this class. */
  public Vector3 normal;
  
  /** The shape the ray originate on
  
      Set to null if originates from camera. */
  public Shape shape;

  /** Construct a new hit. */
  public Hit(Point3 point, double distance, Vector3 normal, Shape shape) 
  {
    this.point = point;
    this.distance = distance;
    this.normal = normal;
    this.shape = shape;
  }
}
