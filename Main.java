/*
   Main.java

   Created on marts  7, 2010 (Morten Rhiger <mir@ruc.dk>)
   This code has been expanded by Thibault Lefebvre and Benoit Bonnet.
 */
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*																																					////
 * This is the documentation of our code, and the explanation of the different features we implemented. Code is commented to explain how it works.  ////
 * This code has been expanded by Thibault Lefebvre and Benoit Bonnet.																				////
 * You can find every scene showing each of our specific implemented features in the folder called "Rendered".										////
 */																																					////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.awt.Color;

public class Main {
	static int window_width  = 800;
	static int window_height = 800;
	/*
	 * Position of the pixel we are working on.
	 */
	static int x = 0;
	static int y = 0;
	static boolean pointLight = true; // This can be set to false in order to have a vectoriel (directed) light instead of a pointing light
	/*
	 * next variables are used to handle the multiple lights.
	 */
	static Point3 pLight = null;
	static Vector3 vLight = null;
	static Vector3 vLight2 = null;
	static Point3 pLights = null;
	static Vector3 vLights = null;
	static Vector3 vLights2 = null;
	static boolean multiplelights = true; // This can be set to true in order to activate a second light on the scene.
	static boolean lightSaved = false; //Simple check to know if we are on a reflection or on the original shape
	static Color firstLight = null; //Saves the color of the first shape hit to handle color mix in a reflection.
	static double reflectionIndice = 0; // This variable allows user to have different intensity of reflection

	
	/*
	 * Main method called from the main, this is where we handle the main part of the computations of our pixel.
	 * This function is a recursive method due to the multiple reflection handling, for example if a pixel reflects another one witch also has a reflection.
	 * We need to give those parameters, in order to be able to call this method with a different pixel than the initial one. 
	 */
	public static Color trace(Ray ray, Scene scene, boolean inReflection, Hit hitReflexion, int x, int y)
	{
		Color c = null; // Initialization of the final color of our pixel.
		Hit hit; 
		/*
		 * The intersect method called with a null as the second parameter will return null if the shape intersect with itself, is we are in an inReflection mode, 
		 * this means that the shape we are working on is not the original one, so we have to give the new shape as the second parameter.
		 */
		if (inReflection == true) 
			hit = scene.intersect(ray, hitReflexion.shape);
		else
			hit = scene.intersect(ray, null);
		/*
		 * If hit equal null, is means that the ray intersect with no object, so we display the sky
		 */
		if (hit == null) 
		{
			/*
			 * This part is our handling of the sky, it is not a physical object in the scene, just some computed colored depending on the y. 
			 */
			return new Color((int)(85*y/300),(int)(85*y/300),(int)150);
		}
		else 
		{	
			/*
			 *  Next checks are used to handle the display of a directional light or a pointing light.
			 * 	If pointLight is true, we create a new point in space, which is going to be our light. In order to be able to check if our hit point touch the light and should be lighten or not,
			 *  we have to create a vector. This vector goes from the hit point to the light. We have to multiply it by -1 so the vector goes in direction of the light.
			 *  Here, we also check if we have multiple lights, and only if we do, we also set the corresponding lights with there vectors in the same way than the first one.
			 */
			if (pointLight == true)
			{
				pLight = new Point3(3, 5, 0);
				vLight2 = hit.point.subtract(pLight).unitize().multiply(-1);	 
				if (multiplelights == true)
				{
					pLights = new Point3(-3, 8, 8);
					vLights2 = hit.point.subtract(pLights).unitize().multiply(-1);	 
				}
			}
			else 
			{
				/*
				 *  Nearly the same as above but this time, we want a directional light. The main difference is that now, the light is a vector directly, so it is not a fixed position in space.
				 *  This can mainly impact shadows displays.
				 */
				vLight = new Vector3(-3, -1.5, 1).unitize();
				vLight2 = vLight.multiply(-1);
				if (multiplelights == true)
				{
					vLights = new Vector3(-5, -5, -1).unitize();
					vLights2 = vLights.multiply(-1);
				}
			}
			double z = 0; // This is the indicator computed to handle shading.
			Ray shadow = new Ray(hit.point, vLight2); // ray from hit point to the light
			Hit hit2 = scene.intersect(shadow, hit.shape); //ray to check if there is an object between the shape and the light
			/*
			 *  the getLightCoef will return the first parameter if the hit is a part of the ground or the second parameter if it is a part of a sphere.
			 *  This compute the angle between the hit dot normal and the light vector, to know how darker the color should be if it is on a side of the sphere for example.
			 *  So if we have a plan, since for a directional light, the angle will always be the same, so we compute our coefficient using the angle between the light source,
			 *  and the camera vector. This makes our ground a "real" infinite one.
			 */
			if (hit2 == null) z += hit.shape.getLightCoef(hit.normal.dot(vLight2), hit.normal.dot(ray.direction.multiply(-1))); //If no, we modify our indicator
			Vector3 normal = hit.normal; // vector normal
			Vector3 direction = ray.direction; // vector direction	
			Vector3 reflected = direction.subtract(normal.cross(direction).multiply(2).cross(normal)).multiply(-1); //Vector to check the reflection.
			Ray reflection = new Ray(hit.point, reflected); // ray from hit point to the surface normal
			Hit hit3 = scene.intersect(reflection, hit.shape); //Here we check if the reflection vector created above hits another shape of the scene.
			if (hit.shape.getReflection() > 0 && hit3 != null) // if it does, and also if we set this specific shape to reflect, we handle reflection.
			{
				if (lightSaved == false) // First time, before calling the method again with a new ray, we save the color of the initial shape, and its reflection degree.
				{
					firstLight = hit.shape.getColor();
					reflectionIndice = hit.shape.getReflection();
					lightSaved = true;
				}
				/*
				 * Here we call the trace method with the new reflected ray.
				 */
				c = trace(reflection, scene, true, hit, x, y);
			} 
			else //No reflection or and of reflection
			{
				if(lightSaved == true) // True only if we had reflection before since this variable is reset to false each time we switch pixel.
				{
					Color mix = null; // New color to mix
					mix = hit.shape.getColor(); // This color is set to the color of the final shape we reflect. and we still have the first object color saved.
					/*
					 * With our to colors and the reflection value, we manage to compute a new color to display.
					 * This means that if an object reflects a lot, (value close to 1), the color will mainly be the one of the reflected object,
					 * in the opposite way, if an object has a small reflection value (close to 0), the color will mainly be the original one.
					 */
					c = new Color((int)(reflectionIndice * mix.getRed() + ((1 - reflectionIndice) * firstLight.getRed())), (int)(reflectionIndice * mix.getGreen() + ((1 - reflectionIndice) * firstLight.getGreen())), (int)(reflectionIndice * mix.getBlue() + ((1 - reflectionIndice) * firstLight.getBlue())));
				}
				else
				{
					// Here we don't reflect anything so we just load the color set in parameter of the hit shape.
					c = hit.shape.getColor();
				}
			}
			/*
			 *  If we have a second light, we change the "Z" indicator. For example if a dot is lighten by the first light, it is set above, but the second light can also impact the color.
			 *  If a dot is only touched by 1 of the two lights, this indicator allows us to make the dot darker. 
			 */
			if(multiplelights == true) 
			{
				Ray shadow2 = new Ray(hit.point, vLights2); // ray from hit point to the light
				Hit secondlight = scene.intersect(shadow2, hit.shape);	
				if (secondlight == null) z += hit.shape.getLightCoef(hit.normal.dot(vLights2), hit.normal.dot(ray.direction.multiply(-1)));
			}	
			z = z > 1 ? 1 : z; // Basic check, if our indicator is over 1, we want it to be set to 1 as a maximal value.
			if (z < 0) // This is the case when there is no more light on the dot, so we simply return a black color.
				return Color.black;
			/*
			 * else, we finally call our method to compute the color we want to display.
			 */
			return applicateLight(c, z);
		}
	}

	/*
	 * This method returns the final color of the pixel, just before displaying it.
	 * This allows us to modify each RGB value of the color with our indicator set in our trace method.
	 * Basically, this make color more or less darker.
	 */
	public static Color applicateLight(Color c, double z){
		int r = c.getRed(), g = c.getGreen(), b = c.getBlue();
		Color lighted = null;
		lighted = new Color((int)(r * z), (int)(g * z), (int)(b * z));
		return lighted;
	}

	public static void main(String[] args) {

		double camera_width = 0.15;
		double camera_height = camera_width * (window_height / (double) window_width);

		// Setup scene

		Scene scene = new Scene(); //First, we need to create our scene.
		/*
		 * Here you can see how to create a new object, a sphere or a ground (plan). We changed the modeler part, seting the shape class from the class handling sphere to an abstract class containing general
		 * information of any shape.
		 * So we created a specific class for sphere object, and another one for our ground.
		 * Sphere takes as constructor parameters: the 3D centre point of the sphere, the radius of the sphere, the reflection mode (beetwin 0 meaning no reflection for this sphere, and 1 meaning mirror reflection) 
		 * and finally the color (handled with the java class Color)/
		 * Ground is constructed nearly the same way, with a color, and a 3D point plus two vectors in order to position the ground properly. 
		 */
		scene.add(new Sphere(new Point3(-1,   -0.2, -2),    0.7, 0.9, Color.red));
		scene.add(new Sphere(new Point3(1, 1.3, -1.2),    0.5, 0.8, Color.blue));
		//scene.add(new Sphere(new Point3(0.5,  0.9, 3),    0.2, 0.4, Color.pink));
		scene.add(new Sphere(new Point3(1.8, 0.1, -4),    0.7, 0.2, Color.magenta));
		//scene.add(new Sphere(new Point3(0.5,   0, 1),    0.7, 0.6, Color.white));
		scene.add(new Sphere(new Point3(-0.5,  -0.1, 2.8),    0.7, 0.6, Color.CYAN));
		//scene.add(new Sphere(new Point3(-1.5,   0.8, 1.2),    0.5, 0.8, Color.green));
		//scene.add(new Sphere(new Point3(1.5, 1.2, 1.1), 0.4, 0.9, Color.orange));
		scene.add(new Sphere(new Point3(-2.5, 0, -1),    0.7, 0.9, Color.yellow));

		scene.add(new Ground(Color.blue, new Point3(0, -1, 0), new Vector3(0, 0, 1), new Vector3(1, 0, 0), 0.9));
		
		// Setup display
		Display display = new Display(window_width, window_height);
		Camera camera = new Camera(new Point3(5, 1, 8), camera_width, camera_height); // Camera constructor.
		camera.setWindowDimensions(window_width, window_height);

		// Render: This is where we switch pixel by pixel to compute each one of them.
		for ( y = 0; y < window_height; y++) 
		{
			for ( x = 0; x < window_width; x++) 
			{
				Ray ray = camera.getRay(x, y);
				lightSaved = false; // we need to set this to false to know that we switch pixel in our reflection computation.
				Color color = trace(ray, scene, false, null, x, y);
				display.plot(x, y, color); //Finally display the computed color on the pixel selected.
			}
			display.refresh(); // Refresh screen.
		}
		// Handle local save of our scenes.
		if (args.length == 1)
			display.save(args[0]);

	}
}
