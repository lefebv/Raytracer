/*
   Display.java
 
   Created on marts  7, 2010 (Morten Rhiger <mir@ruc.dk>)
   This code has been expanded by Thibault Lefebvre and Benoit Bonnet.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/** A display is something you can draw on. */
public class Display extends JFrame  
{
  private int width;
  private int height;

  /** Returns the width of the image being drawn. */
  public int getImageWidth() 
  {
    return width;
  }

  /** Returns the height of the image being drawn. */
  public int getImageHeight() 
  {
    return height;
  }
    
  private BufferedImage image;
  private ImagePanel panel;
  private WritableRaster raster;
  private Graphics graphics;

  /** Constructor. */
  public Display(int width, int height) 
  {
    super("Display");

    this.width = width;
    this.height = height;

    int image_width  = width;
    int image_height = height;

    image = new BufferedImage(image_width, image_height, BufferedImage.TYPE_INT_RGB);
    raster = image.getRaster();
    graphics = image.getGraphics();
    panel = new ImagePanel(image);
    Graphics g = image.getGraphics();

    this.add(panel);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(image_width + 20, image_height + 40);
    this.setVisible(true);
  }

  private int[] rgb = { 0, 0, 0 };

  /** Plots a pixel at the given position with the given color. */
  public void plot(int x, int y, Color c) 
  {
    rgb[0] = c.getRed();
    rgb[1] = c.getGreen();
    rgb[2] = c.getBlue();
    raster.setPixel(x, y, rgb);
  }

  /** Plots a rectangular block at the given position with the given
      color. */
  public void plot(int x, int y, Color c, int size) 
  {
    graphics.setColor(c);
    graphics.fillRect(x, y, size, size);
  }

  /** Refreshes this display. */
  public void refresh() 
  {
    panel.repaint();
  }

  private class ImagePanel extends JPanel 
  {
    private BufferedImage image;
    
    public ImagePanel(BufferedImage image) 
    {
      this.image = image;
    }
    
    public void paint(Graphics g) 
    {
      int x = (getWidth() - image.getWidth()) / 2;
      if (x < 0) 
    	  x = 0;
      
      int y = (getHeight() - image.getHeight()) / 2;
      if (y < 0) 
    	  y = 0;
      
      g.drawImage(image, x, y, Color.BLACK, null);
    }
    
    public BufferedImage getImage() 
    {
      return image;
    }
  }    

  /** Save display to file. */

  public void save(String filename) 
  {
    BufferedImage image = panel.getImage();
    try 
    {
      javax.imageio.ImageIO.write(image, "png", new java.io.File(filename));
    } 
    catch (java.io.IOException e) 
    {
      System.err.format("Error: Cannot save image to file \"%s\"\n", filename);
      System.exit(-1);
    }
  }
}
