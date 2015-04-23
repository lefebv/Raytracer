/*
   Camera.java
 
   Created on marts  8, 2010 (Morten Rhiger <mir@ruc.dk>)
   This code has been expanded by Thibault Lefebvre and Benoit Bonnet.
*/

/** Represents a camera used for rendering.  A camera consists of a
    view plane and a virtual 'eye' behind this screen.  Each pixel on
    the display corresponts to a point on the view plane and a ray
    passin from the eye through that point into the scene.

    A camera has a position, a direction of its view, and a direction
    of its upside, all of which are relative to the scene.  */

public class Camera {

  /** The location of the camera 'eye' within the scene. */
  private Point3 eye;

  /** The location of the center of the view plane within the scene.
      This is a derived value. */
  private Point3 center;

  /** The direction in which the 'eye' points.  This is the direction
      of the center of the view plane within the scene.

      The direction vector is perpendicular to both the up vector and
      the right vector. */
  private Vector3 direction;

  /** The width of the view plane. */
  private double width;

  /** The height of the view plane. */
  private double height;

  /** 
  /** The direction of the cameras up side.  The left and right sides
      of the view plane point in this direction.  This is a normalized
      vector.

      The up vector is perpendicular to both the direction vector and
      the right vector. */
  private Vector3 up; 

  /** The direction of the cameras right side.  The top and bottom
      sides of the camera view plane in this direction.  This is a
      normalized vector.  It is derived from the direction and up
      vectors.

      The right vector is perpendicular to both the direction vector and
      the up vector. */
  private Vector3 right;

  /** Constructs a camera at a certian position, pointing in a certain
      direction, tilted a certain amount, and with a view plane of a
      certain dimension. */
  public Camera(Point3 eye, Vector3 direction, Vector3 up, double width, double height) 
  {
    this.eye = eye;
    this.direction = direction;
    this.width = width;
    this.height = height;
    this.right = direction.cross(up).unitize();
    this.up = this.right.cross(direction).unitize();
    this.center = eye.add(direction);
  }

  /** Contructs a camera which is not tilted (its top points
      upwards). */
  public Camera(Point3 eye, Vector3 direction, double width, double height) 
  {
    this(eye, direction, new Vector3(0, -1, 0), width, height);
  }

  /** Contructs a camera which is not tilted (its top points upwards)
      and which points towards the origin of the scene. */
  public Camera(Point3 eye, double width, double height) 
  {
    this(eye, eye.toVector3().unitize().multiply(-0.2), new Vector3(0, -1, 0), width, height);
  }

  /** Compute a ray that starts out at the location of the camera
      'eye' and that passes through the view plane at the view-plane
      coordinate (x, y). */
  public Ray getRay(double x, double y) {

    // Map (x, y) (which is a view plane coordinate) to a point in
    // (3d) world coordinates.  Then create a ray going from the
    // location of the camera toward this point.

    Point3 p = center.add(right.multiply(x - width / 2).add(up.multiply(y - height / 2)));
    Vector3 dir = p.subtract(eye).unitize();
    return new Ray(eye, dir);
  }

  /** Compute a ray that starts out at the location of the camera
      'eye' and that passes through the view plane at the view-plane
      coordinate that corresponds to the pixel (x, y) in a window of
      width window_width and height window_height. */
  public Ray getRay(int x, int y, int window_width, int window_height) 
  {
    return getRay(x * width / window_width, y * height / window_height);
  }

  /** Dimension of the display associated with this camera. */
  private int window_width, window_height;

  /** Sets the dimension of the display associated with this camera.
      (Needed onyl if invoking 'getRay(int, int)') */
  public void setWindowDimensions(int width, int height) 
  {
    this.window_width = width;
    this.window_height = height;
  }

  /** Compute a ray that starts out at the location of the camera
      'eye' and that passes through the view plane at the view-plane
      coordinate that corresponds to the pixel (x, y) in a window of
      the dimension associated with this camera.  (See method
      'setWindowDimensions(int, int)'.  */
  public Ray getRay(int x, int y) 
  {
	return getRay(x * width / window_width, y * height / window_height);
  }
}
