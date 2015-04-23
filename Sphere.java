import java.awt.Color;

/*
   Shape.java
 
   Created on marts  7, 2010 (Morten Rhiger <mir@ruc.dk>)
   This code has been expanded by Thibault Lefebvre and Benoit Bonnet.
*/

/** A representation of shapes. */
public class Sphere extends Shape
{
  /** The center of this shape in the scene. */
  private Point3 center;

  /** The radius of this shape. */
  private double radius;
  
  public double getLightCoef(double z1, double z2)
  {
	  return z1;
  }

  /** Constructs a new shape. */
  public Sphere(Point3 center, double radius, double reflection, Color _color) 
  {
    this.center = center;
    this.radius = radius;
    this.reflection = reflection;
   	this.color = _color;    
  }
   
  /** Returns a hit (if the given ray hits this shape) or null (if
      not). */
  public Hit intersect(Ray ray, Shape shape) {
	    if (shape != null && shape == this) 
	    	return null;
	    Vector3 v = center.subtract(ray.origin);
	    double b = v.dot(ray.direction);
	    double disc = b * b - v.dot(v) + radius * radius;

	    if (disc < 0) 
	    	return null;

	    disc = Math.sqrt(disc);
	    double t2 = b + disc;

	    if (t2 < 0) 
	    	return null;

	    double t1 = b - disc;
	    double dist = (t1 > 0.0) ? t1 : t2;

	    Point3 hit_point = ray.origin.add(ray.direction.multiply(dist));
	    Vector3 dir = hit_point.subtract(center).unitize();
	    // We should reverse the direction of dir (by multiplying it by
	    // -1) if we want to detect in 'inside' hit.
	    return new Hit(hit_point, dist, dir, this);
	}
  }
