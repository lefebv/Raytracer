import java.awt.Color;

/*
   Shape.java
 
   Created on marts  7, 2010 (Morten Rhiger <mir@ruc.dk>)
   This code has been expanded by Thibault Lefebvre and Benoit Bonnet.
*/

/** A representation of ground. */
/*
 * This is the class we created to display a 3D plan on our screen.
 * It extands the shape class in order to benefits from the shape class methods or variables.
 */
public class Ground extends Shape
{
	Vector3 normal; // Plan normal
	/*
	 * As two vectors are not enough to know the position of the plan, we also need a 3D point on the plan to know where can only be the plan.
	 */
	Point3 start; 
	Vector3 v1;
	Vector3 v2;
	
	// Constructor.
	public Ground(Color color, Point3 vertex, Vector3 v1, Vector3 v2, double reflection) 
	{
		this.color = color;
		this.start = vertex;
		this.v1 = v1;
		this.v2 = v2;
		this.reflection = reflection;
		this.normal = v1.cross(v2).unitize();		
	}
	
	//explained in the main.java file in the trace method.
	public double getLightCoef(double z1, double z2)
	{
		return z2;
	}

	// Intersect method specific for the ground.
	public Hit intersect(Ray ray, Shape shape) {
		
		if (shape != null && shape == this)  // In order to disable auto-intersection.
			return null;

		double a = ray.direction.dot(this.normal);
		if (a == 0) 
			return null;

		Vector3 direction = new Vector3(start.x - ray.origin.x, start.y - ray.origin.y, start.z - ray.origin.z);

		double t = this.normal.dot(direction) / a;
		if (t < 0) return null;

		Point3 hitPoint = new Point3(t * ray.direction.x + ray.origin.x, t * ray.direction.y + ray.origin.y, t * ray.direction.z + ray.origin.z);
		
		return new Hit(hitPoint, hitPoint.subtract(ray.origin).length(), normal, this);
	}
}