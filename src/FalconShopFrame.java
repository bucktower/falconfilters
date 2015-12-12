import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.File;
import java.util.Scanner;

import javax.swing.JFrame;

public class FalconShopFrame extends JFrame {

	// toolbarHeight -- since we're trying to fill the screen, we've got to account for the height of default toolbars,
	// which I think are ~ 30px on OSX.
	// TODO: change toolbarHeight based on OS & look up more accurate measurement
	private int toolbarHeight = 30;

	private FalconPanel thePanel;
	
	public static String versionString = "0.0.1";
	
	private String[][] fullOptions;
	
	public FalconShopFrame() 
	{
		
		super("FalconShop");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    pack();
	    setSize(screenSize.width,screenSize.height-toolbarHeight);
	    
		thePanel = new FalconPanel();
		getContentPane().setLayout(new GridLayout(1,1));
		getContentPane().add(thePanel);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		// OPTIONS FOR INPUT
		/*
		 * Here, I'm creating an array of options that people can use for FalconShop. This makes it so
		 * I can easily list them if you were to do a command like "help". However, there are many
		 * "shortcuts" or alternate commands for each, so I'm creating a 2D array of fullOptions to have
		 * support for that, although the [x][0] cell of each array will have the proper, full command.
		 */
		String[] options = {"red", "green", "blue", "flip-horiz", "flip-vert", "grayscale", "techno-noise-dark",
				"techno-noise-warm", "gaussian-blur", "k-means", "transfer"};
		fullOptions = new String[options.length][5];
		for(int a = 0; a < fullOptions.length; a++) {
			fullOptions[a][0] = options[a];
		}
		// Descriptions
		fullOptions[0][1] = "Take out all other colors but red";
		fullOptions[1][1] = "Take out all other colors but green";
		fullOptions[2][1] = "Take out all other colors but blue";
		fullOptions[3][1] = "Flip the image horizontally";
		fullOptions[4][1] = "Flip the image vertically";
		fullOptions[5][1] = "Turn to black & white";
		fullOptions[6][1] = "Craziness with a darker color pallette";
		fullOptions[7][1] = "Craziness with a warmer color palette";
		fullOptions[8][1] = "Blur. Like a Gauss.";
		fullOptions[9][1] = "K-Means blob recognition";
		fullOptions[10][1] = "Transfer the destination image to the source image";
		// END Descriptions
		
		// Extra Options (alternate commands) are manually added below
		fullOptions[5][2] = "bw";
		fullOptions[6][2] = "tnd";
		fullOptions[7][2] = "tnw";
		fullOptions[8][2] = "gaus";
		fullOptions[8][3] = "blur";
		fullOptions[9][2] = "kmeans";
		fullOptions[9][3] = "k";
		fullOptions[10][2] = "t";
		// END Extra Options
				
		Scanner keyboard = new Scanner(System.in);
		boolean keepRunning = true;
		
		int effectNum = 1;
		
		int userSelect = -1;
		while(keepRunning) {
			System.out.println("FALCONFILTERS (EFFECT " + effectNum + ")\n"
								+ "Please enter the mode you would like to use:"
								+ "\n(type 'help' for a list of modes)");
			String myInput = keyboard.nextLine();
			if(myInput.equals("help")) {
				System.out.println("These are the available commands:");
				for(String opt: options) {
					System.out.println(opt.toUpperCase());
				}
				System.out.println();
			} else if(myInput.equals("exit") | myInput.equals(":pq") | myInput.equals("q") | myInput.equals("quit") | myInput.equals("stop")) {
				System.out.println("Goodbye!");
				keepRunning = false;
			} else if(myInput.equals("v") | myInput.equals("version")) {
				System.out.println("You are running FalconFilters " + versionString);
			} else {
				for(int b = 0; b < fullOptions.length; b++) {
					for(int a = 0; a < fullOptions[0].length; a++) {
						if(fullOptions[b][a] != null) {
							if(myInput.toUpperCase().equals(fullOptions[b][a].toUpperCase())) {
								userSelect = b;
								thePanel.analyzeSource(userSelect);
							}
						}
					}
				}
				effectNum++;
			}
		}
	}
}
