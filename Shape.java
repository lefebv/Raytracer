import java.awt.Color;

/*
   Shape.java
 
   Created on marts  7, 2010 (Morten Rhiger <mir@ruc.dk>)
   This code has been expanded by Thibault Lefebvre and Benoit Bonnet.
*/

/*
 * This is our abstract class used for all our shapes, every shape in the scene is extanding this class, in order to be sure not to forget anything.
 */
public abstract class Shape 
{
	/** Reflection of the shape */
	public Color color;

	/** Reflection of the shape */
	public double reflection;
	
	/** value of the coefficient to be apply to compute shading. */
	public double lightCoef;
		
	// This returns the reflection value set in parameter in all our shapes.
	public double getReflection()
	{
		return this.reflection;
	}

	// This returns the color value set in parameter in all our shapes.		
	public Color getColor()
	{
		return this.color;
	}

 /** Returns a hit (if the given ray hits this shape) or null (if
      not). */
  public abstract Hit intersect(Ray ray, Shape shape); // Mandatory to create any shape
  public abstract double getLightCoef(double z1,double z2); // Mandatory to create any shape.
}
