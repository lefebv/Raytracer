/*
   Main.java
 
   Created on marts 12, 2010 (Morten Rhiger <mir@ruc.dk>)
   This code has been expanded by Thibault Lefebvre and Benoit Bonnet.
*/

import java.util.ArrayList;

/** Objects of this class represents scenes to be rendered.  A scene
    is simply a list of shapes. */

public class Scene 
{
  /** The list of shapes in this scene. */
  private ArrayList<Shape> shapes;

  /** Constructs a new scene. */
  public Scene() 
  {
    shapes = new ArrayList<Shape>();
  }

  /** Adds a shape to this scene. */
  public void add(Shape shape) 
  {
    shapes.add(shape);
  }

  /** Intersect a ray with the scene.  Returns the closest hit (or
      null if no shape intersects with the ray). */
  public Hit intersect(Ray ray, Shape shape2) 
  {
    Hit best = null;
    for (Shape shape : shapes) 
    {
      Hit hit = shape.intersect(ray, shape2);
      if (best == null)
        best = hit;
      else if (hit != null && hit.distance < best.distance)
        best = hit;
    }
    return best;
  }
}

