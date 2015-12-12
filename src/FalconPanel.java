import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class FalconPanel extends JPanel {
		
	private BufferedImage sourceImage, destinationImage;
	private String filename = "KMeansTest1.png";
	
	private Random randGen;
	
	private Scanner keyboard;
	
	/** 
	 * constructor loads the file in filename and creates an identically sized, blank image.
	 */
	public FalconPanel() 
	{
		super();
		
		randGen = new Random();
		
		File folder = new File(".");
		File[] listOfFiles = folder.listFiles();
		
		boolean keepOn = true;
		
		while(keepOn) {
			System.out.println("Please enter the name of the picture file you would like to use (include extension)\n"
								+ "(leave blank for default k-means picture, type 'help' for a list of files in the current directory)");
			keyboard = new Scanner(System.in);
			String myFile = keyboard.nextLine();
			if(myFile.equals("help")) {

				System.out.println("Files currently in the directory:");
				for(int a = 0; a < listOfFiles.length; a++) {
					System.out.println(listOfFiles[a]);
				}
				continue;
			} else if(!myFile.isEmpty()) {
				filename = myFile;
				keepOn = false;
			}
			try
			{
			   File file = new File(this.filename);
	
			   if (file.canRead()) 
			   {
				   sourceImage = ImageIO.read(file);
				   keepOn = false;
			   }
			   else
				   throw new RuntimeException("Could not open file.");
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
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
						int sourceColor = getBrightnessAtPoint(i,j);
						destinationImage.setRGB(i, j, (sourceColor | sourceColor<<8 | sourceColor<<16));
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
			case 8: // Gaussian Blur
				for(int timesOver = 0; timesOver < 8; timesOver++) {
					for(int i = 1; i < destinationImage.getWidth()-1; i++) {
						for(int j = 1; j < destinationImage.getHeight()-1; j++) {
							int rSum = 0;
							int gSum = 0;
							int bSum = 0;
							for(int xx = -1; xx < 2; xx++) {
								for(int yy = -1; yy < 2; yy++) {
									rSum += getRedAtPoint(i+xx, j+yy);
									gSum += getGreenAtPoint(i+xx, j+yy);
									bSum += getBlueAtPoint(i+xx, j+yy);
								}
							}
							rSum /= 9;
							gSum /= 9;
							bSum /= 9;
							
							destinationImage.setRGB(i, j, (rSum<<16)+(gSum<<8)+bSum);
						}
						repaint();
					}
					sourceImage = destinationImage;
				}
				break;
			case 9: // K-means
				ArrayList<VotingPoint> pixelList = new ArrayList<VotingPoint>();
				ArrayList<VotingPoint> meanList = new ArrayList<VotingPoint>();
				ArrayList<Color> colorList = new ArrayList<Color>();
				
				// how many random color reference pixels we want (groups)
				// TODO: user enter randSpots ("Use the Scanner, Buck!")
				System.out.println("How many random spots (k) do you want?  (Recommended = 6)");
				int randSpots = Integer.parseInt(keyboard.nextLine());
				System.out.println("How many times should a re-averaging occur?\n(Recommended = 40, just to be safe, or 10 if you're more of a risk-taker)");
				int timesOver = Integer.parseInt(keyboard.nextLine());
				
				// making the colors. math to art, amazing.
				for(int numColors = 0; numColors < randSpots; numColors++) {
					// creating a color with randomly-generated RGB values
					colorList.add(new Color(randGen.nextInt(255), randGen.nextInt(255), randGen.nextInt(255)));
				}
				
				// voting point for every pixel if brightness lower than 25%
				for(int x = 0; x < destinationImage.getWidth(); x++) {
					for(int y = 0; y < destinationImage.getWidth(); y++) {
						if(getBrightnessAtPoint(x,y) < 63) { // 63 = ~25% brightness
							pixelList.add(new VotingPoint(x,y));
						}
					}
				}
								
				// pixel list with 6 of the random pixels from the blobs
				for(int g = 0; g < randSpots; g++) {
					meanList.add(pixelList.get(randGen.nextInt(pixelList.size())));
//					System.out.println(meanList.get(g));
				}
				
				for(int numTimesOver = 0; numTimesOver < timesOver; numTimesOver++) {
					// putting in bins
					for(VotingPoint pt: pixelList) {
						// keeping track of the record min dist, start off with -1 since there's no such thing as neg dist
						int minDistRecord = -1;
						
						for(int checks = 0; checks < meanList.size(); checks++) {
							// get dist
							int distToBin = pt.distanceSquared(meanList.get(checks).getX(), meanList.get(checks).getY());
							if(minDistRecord == -1 || distToBin < minDistRecord) {
								// update record min dist & set to record dist bin
								minDistRecord = distToBin;
								pt.setWhichBin(checks);
							}
						}
					}
					// resetting the averages
					for(int numBins = 0; numBins < meanList.size(); numBins++) {
						int newAvgX = 0;
						int newAvgY = 0;
						int numPts = 0;
						
						for(VotingPoint px: pixelList) {
							if(px.getWhichBin() == numBins) {
								newAvgX += px.getX();
								newAvgY += px.getY();
								numPts++;
							}
						}
						newAvgX /= numPts;
						newAvgY /= numPts;
						
						// EXPERIMENTAL CODE -- trying to "snap" the points to the closest "blob"
//							VotingPoint spotOnPoint = new VotingPoint(0,0);
//							int minDist = -1;
//							for(VotingPoint pt: pixelList) {
//								int distAway = pt.distanceSquared(newAvgX, newAvgY);
//								if(distAway < minDist || minDist == -1) {
//									spotOnPoint = pt;
//									minDist = distAway;
//								}
//							}
//							// resetting the mean point of the bin to the new avg
//							spotOnPoint.setWhichBin(numBins);
//							meanList.set(numBins, spotOnPoint);
//							// resetting the mean point of the bin to the new avg
						// END EXPERIMENTAL CODE
							meanList.set(numBins, new VotingPoint(newAvgX,newAvgY));
						// EXPERIMENTAL CODE -- if we put more random points out there and "pepper" each blob,
						// we're more likely to not have 1 pt going over 2 blobs, and then we can eliminate pts on same blob
						// this code isn't working for some reason
//							for(VotingPoint dt: meanList) {
//								boolean isConnected = true; //if 2 points are on the same blob
//									if(!(dt == meanList.get(numBins))) {
//									int xChange = Math.abs(spotOnPoint.getX() - dt.getX());
//									int yChange = Math.abs(spotOnPoint.getY() - dt.getY());
//									double slope = yChange/xChange;
//									for(int x = 5; x < xChange; x+=5) {
//										for(VotingPoint ot: pixelList) {
//											if(!(ot.getX() == x + spotOnPoint.getX()) || !(ot.getY() == (int)x*slope + spotOnPoint.getY())) {
//												isConnected = false;
//											}
//										}
//									}
//									if(isConnected == true) {
//										meanList.remove(dt);
//									}
//								}
//							}
						// END EXPERIMENTAL CODE
					}
				}
				
				// setting bg (all) to white
				for(int x = 0; x < destinationImage.getWidth(); x++) {
					for(int y = 0; y < destinationImage.getWidth(); y++) {
						destinationImage.setRGB(x, y, (1<<24) - 1);
					}
				}
				
				for(VotingPoint pt: pixelList) {
					destinationImage.setRGB(pt.getX(), pt.getY(), colorList.get(pt.getWhichBin()).getRGB());
				}
				
				for(VotingPoint pt: meanList) {
					// drawing a little TIE-fighter for each anchor/avg pt, in the mood of the Star Wars season
					destinationImage.setRGB(pt.getX(), pt.getY(), 0);
					destinationImage.setRGB(pt.getX()-1, pt.getY()-1, 0);
					destinationImage.setRGB(pt.getX()+1, pt.getY()-1, 0);
					destinationImage.setRGB(pt.getX()-1, pt.getY(), 0);
					destinationImage.setRGB(pt.getX()+1, pt.getY(), 0);
					destinationImage.setRGB(pt.getX()-1, pt.getY()+1, 0);
					destinationImage.setRGB(pt.getX()+1, pt.getY()+1, 0);
				}
				
				break;
			case 10: // transfer (dest to source)
				for(int i = 0; i < destinationImage.getWidth(); i++) {
					for(int j = 0; j < destinationImage.getHeight(); j++) {
						sourceImage.setRGB(i, j, (destinationImage.getRGB(i,j)));
					}
				}
			case 11:
				ArrayList<VotingPoint> pixelList2 = new ArrayList<VotingPoint>();
				for(int i = 1; i < destinationImage.getWidth()-1; i++) {
					for(int j = 1; j < destinationImage.getHeight()-1; j++) {
						int diffBelow = (int) Math.pow(getBrightnessAtPoint(i,j)-getBrightnessAtPoint(i,j+1), 2);
						int diffRight = (int) Math.pow(getBrightnessAtPoint(i,j)-getBrightnessAtPoint(i+1,j), 2);
						if(diffBelow > 1000 || diffRight > 1000) { // 63 = ~25% brightness
							pixelList2.add(new VotingPoint(i,j));
						}
					}
				}
				for(VotingPoint pt: pixelList2) {
					destinationImage.setRGB(pt.getX(), pt.getY(), (1<<24) - 1);
				}
				repaint();
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
		System.out.println("Would you like the source image to be changed too? (useful for applying multiple effects)\n(y/n)");
		String changeSource = keyboard.nextLine();
		if(changeSource.toUpperCase().equals("Y") && b != 10) {
			for(int i = 0; i < destinationImage.getWidth(); i++) {
				for(int j = 0; j < destinationImage.getHeight(); j++) {
					sourceImage.setRGB(i, j, (destinationImage.getRGB(i,j)));
				}
			}
		}
		repaint();
		System.out.println("Done!\n");
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
//		int color = sourceColor | sourceColor<<8 | sourceColor<<16;
		return sourceColor;
	}
	
}
