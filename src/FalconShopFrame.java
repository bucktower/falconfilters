import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.util.Scanner;

import javax.swing.JFrame;

public class FalconShopFrame extends JFrame {

	// toolbarHeight -- since we're trying to fill the screen, we've got to account for the height of default toolbars,
	// which I think are ~ 30px on OSX.
	// TODO: change toolbarHeight based on OS & look up more accurate measurement
	private int toolbarHeight = 30;

	private FalconPanel thePanel;
	
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
				"techno-noise-warm"};
		fullOptions = new String[options.length][5];
		for(int a = 0; a < fullOptions.length; a++) {
			fullOptions[a][0] = options[a];
		}
		// Extra Options (alternate commands) are manually added below
		fullOptions[5][2] = "bw";
		fullOptions[6][2] = "tnd";
		fullOptions[7][2] = "tnw";
		// END Extra Options
				
		Scanner keyboard = new Scanner(System.in);
		boolean keepRunning = true;
		
		int userSelect = -1;
		while(keepRunning) {
			System.out.println("Please enter the mode you would like to use:");
			String myInput = keyboard.nextLine();
			if(myInput.equals("help")) {
				System.out.println("These are the available commands:");
				for(String opt: options) {
					System.out.println(opt.toUpperCase());
				}
				System.out.println();
			} else if(myInput.equals("exit") | myInput == ":pq" | myInput == "q" | myInput == "quit") {
				System.out.println("Goodbye!");
				keepRunning = false;
			} else {
				for(int b = 0; b < fullOptions.length; b++) {
					for(int a = 0; a < fullOptions[0].length; a++) {
						if(myInput.toUpperCase().equals(fullOptions[b][a].toUpperCase())) {
							userSelect = b;
						}
					}
				}
				thePanel.analyzeSource(userSelect);
			}
		}
	}
}
