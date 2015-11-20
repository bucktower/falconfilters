import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class FalconPanel extends JPanel {
		
	private BufferedImage sourceImage, destinationImage;
	private String filename = "USGS_Prairie_Falcon.jpg";
	/** 
	 * constructor loads the file in filename and creates an identically sized, blank image.
	 */
	public FalconPanel() 
	{
		super();

		try
		{
		   File file = new File(this.filename);

		   if (file.canRead()) 
		   {
			   sourceImage = ImageIO.read(file);
		   }
		   else
			   throw new RuntimeException("Could not open file.");
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		destinationImage = new BufferedImage(sourceImage.getWidth(),sourceImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
		
		
	}
	/**
	 * draws both copies of the image, side by side.
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(sourceImage, 0, 0, null);
		g.drawImage(destinationImage, sourceImage.getWidth(), 0, null);
	}
	/**
	 * looks at the data in the sourceImage, does some magic to it, and uses it to generate an image 
	 * in destinationImage.
	 */
	public void analyzeSource(int b)
	{
		// TODO: you need to write this. 
		//       You may find you want to make different versions for different effects.
		switch(b) {
			case 0: // "red":
				System.out.println("Red Filter");
				for(int i = 0; i < destinationImage.getWidth(); i++) {
					for(int j = 0; j < destinationImage.getHeight(); j++) {
						destinationImage.setRGB(i, j, (getRedAtPoint(i,j))<<16);
					}
				}
				break;
			case 1: // "green":
				System.out.println("Red Filter");
				for(int i = 0; i < destinationImage.getWidth(); i++) {
					for(int j = 0; j < destinationImage.getHeight(); j++) {
						destinationImage.setRGB(i, j, (getGreenAtPoint(i,j))<<8);
					}
				}
				break;
			case 2: // "blue":
				System.out.println("Red Filter");
				for(int i = 0; i < destinationImage.getWidth(); i++) {
					for(int j = 0; j < destinationImage.getHeight(); j++) {
						destinationImage.setRGB(i, j, (getBlueAtPoint(i,j)));
					}
				}
				break;
			case 3: // "flip-horiz":
				for(int i = 0; i < destinationImage.getWidth(); i++) {
					for(int j = 0; j < destinationImage.getHeight(); j++) {
						destinationImage.setRGB(i, j, (sourceImage.getRGB(sourceImage.getWidth()-i-1,j)));
					}
				}
				break;
			case 4: // "flip-vert":
				for(int i = 0; i < destinationImage.getWidth(); i++) {
					for(int j = 0; j < destinationImage.getHeight(); j++) {
						destinationImage.setRGB(i, j, (sourceImage.getRGB(i,sourceImage.getHeight()-j-1)));
					}
				}
				break;
			case 5: //"grayscale":
				for(int i = 0; i < destinationImage.getWidth(); i++) {
					for(int j = 0; j < destinationImage.getHeight(); j++) {
						destinationImage.setRGB(i, j, (getBrightnessAtPoint(i,j)));
					}
				}
				break;
			case 6: // "techno-noise-dark":
				for(int i = 0; i < destinationImage.getWidth(); i++) {
					for(int j = 0; j < destinationImage.getHeight(); j++) {
						destinationImage.setRGB(i, j, (getRedAtPoint(i,j)<<16 + getGreenAtPoint(i,j)<<8 + getBlueAtPoint(i,j)) / 3);
					}
				}
				break;
			case 7: // "techno-noise-warm":
				for(int i = 0; i < destinationImage.getWidth(); i++) {
					for(int j = 0; j < destinationImage.getHeight(); j++) {
						destinationImage.setRGB(i, j, sourceImage.getRGB(i, j) / 3);
					}
				}
				break;
			case -1:
			default:
				System.out.println("Default Print Mode");
				for(int i = 0; i < destinationImage.getWidth(); i++) {
					for(int j = 0; j < destinationImage.getHeight(); j++) {
						destinationImage.setRGB(i, j, (sourceImage.getRGB(i,j)));
					}
				}
				break;
		}
		repaint();
	}
	
	/**
	 * returns the 8 red bits from the RRRRRRRRGGGGGGGGBBBBBBBB color value found at (x,y)
	 * @param x
	 * @param y
	 * @return the red value (0-255) found at (x,y) in sourceImage
	 */
	public int getRedAtPoint(int x, int y)
	{
		int color = sourceImage.getRGB(x, y);
		color = (color & (((1<<24) - 1) - ((1<<16) - 1)))>>16;
		return color;
	}
	
	/**
	 * returns the 8 green bits from the RRRRRRRRGGGGGGGGBBBBBBBB color value found at (x,y)
	 * @param x
	 * @param y
	 * @return the green value (0-255) found at (x,y) in sourceImage
	 */
	public int getGreenAtPoint(int x, int y)
	{
		int color = sourceImage.getRGB(x, y);
		color = (color & (((1<<16) - 1) - ((1<<8) - 1)))>>8;
		return color;
	}
	
	/**
	 * returns the 8 blue bits from the RRRRRRRRGGGGGGGGBBBBBBBB color value found at (x,y)
	 * @param x
	 * @param y
	 * @return the blue value (0-255) found at (x,y) in sourceImage
	 */
	public int getBlueAtPoint(int x, int y)
	{
		int color = sourceImage.getRGB(x, y);
		color = color & ((1<<8) - 1);
		return color;
	}
	/**
	 * returns the average of the red, green and blue values at location (x,y).
	 * @param x
	 * @param y
	 * @return the brightness (0-255) of the point (x,y) in sourceImage;
	 */
	public int getBrightnessAtPoint(int x, int y)
	{
		int sourceColor = (getRedAtPoint(x,y) + getBlueAtPoint(x,y) + getBlueAtPoint(x,y))/3;
		int color = sourceColor | sourceColor<<8 | sourceColor<<16;
		return color;
	}
	
}
