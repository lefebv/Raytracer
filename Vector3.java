/*
   Vector3.java

   Created on marts 11, 2010 (Morten Rhiger <mir@ruc.dk>)
   This code has been expanded by Thibault Lefebvre and Benoit Bonnet.
*/

/** Objects of this class represents vectors in 3 dimensions. */
public class Vector3 
{

  /** For convenience, the coordinates of a vector are stored in
      public fields.  Don't modify them. */
  public double x, y, z;

  /** Constructs a new vector. */
  public Vector3(double x, double y, double z) 
  {
    this.x = x; this.y = y; this.z = z;
  }

  /** Scalar multiplication.  Creates a new vector by multiplying an
      existing vector by a scalar. */
  public Vector3 multiply(double scalar) 
  {
    return new Vector3(scalar * x, scalar * y, scalar * z);
  }
  
  /** Computes the dot product of this vector and another one. */
  public double dot(Vector3 v) 
  {
    return x * v.x + y * v.y + z * v.z;
  }

  /** Cross product.  Creates a new vector by computing the cross
      product of two existing vectors. */
  public Vector3 cross(Vector3 v) 
  {
    return new Vector3(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
  }

  /** Normalization of this vectors length.  Creates a new vector by
      normalizing the length of an existing vector. */
  public Vector3 unitize() 
  {
    return this.multiply(1 / Math.sqrt(this.dot(this)));
  }

  /** Return the length of a vector */
  public double length()
  {
	return Math.sqrt(x*x+y*y+z*z);  
  }
  
  /** Vector addition.  Creates a new vector by adding two existing
      vectors. */
  public Vector3 add(Vector3 w) 
  {
    return new Vector3(x + w.x, y + w.y, z + w.z);
  }

 /** Vector addition.  Creates a new point by adding a point to a vector. */
  public Point3 add(Point3 p) 
  {
    return new Point3(x + p.x, y + p.y, z + p.z);
  }
  
  /** Vector subtraction.  Creates a new vector by subtracting two
      existing vectors. */
  public Vector3 subtract(Vector3 w) 
  {
    return new Vector3(x - w.x, y - w.y, z - w.z);
  }

  /** Return a textual representation of a vector. */
  public String toString() 
  {
    return String.format("(%2.4f, %2.4f, %2.4f)", x, y, z);
  }
  
  /** Used to compute the angle between two vectors */
	public double angleTo(Vector3 v)
	{
		return 1.0 / Math.cos(this.dot(v) / (this.length() * v.length()));
	}
}
