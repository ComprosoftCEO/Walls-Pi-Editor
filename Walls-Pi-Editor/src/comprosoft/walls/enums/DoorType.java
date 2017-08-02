package comprosoft.walls.enums;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public enum DoorType {
	NO_DOOR (0, new Color(192,192,192),"",0),
	RED_DOOR (1, new Color(255,0,0),"Red Door",1 ),
	YELLOW_DOOR (2, new Color(255,255,0),"Yellow Door",2),
	GREEN_DOOR (3, new Color(0,255,0),"Green Door",3),
	BLUE_DOOR (4, new Color(0,0,255), "Blue Door",4);


	private final int index;
	private final Color color;
	
	private final String name;
	private final int exportValue;
	
	private static final Image noLock;
	private static final Image topLock;
	private static final Image bottomLock;
	private static final Image leftLock;
	private static final Image rightLock;
	
	
	
	static {
		noLock = loadImage("Item/NoItem.png");
		topLock = loadImage("Lock/Lock-Top.png");
		bottomLock = loadImage("Lock/Lock-Bottom.png");
		leftLock = loadImage("Lock/Lock-Left.png");
		rightLock = loadImage("Lock/Lock-Right.png");
	}
	
	
	
	/**
	 * Load an image from a file
	 * 
	 * @param path The text path of the image
	 * @return Imate
	 */
	private static Image loadImage(String path) {
		//Load image
		BufferedImage img = null;
		try {
			URL p = ItemType.class.getResource("/"+path);
			img = ImageIO.read(p);
		} catch (IOException e) {
			System.out.println("Invalid image file " +path);
		}
		return img;	
	}
	
	
	/**
	 * Create a new door type enum object
	 * 
	 * @param index The index in the combo box
	 * @param color Color of the door
	 * @param name Name of the door
	 * @param exportValue Integer to use when exporting to python code
	 */
	DoorType(int index, Color c, String name, int exportValue) {
		this.index = index;
		this.color = c;
		this.name = name;
		this.exportValue = exportValue;
	}
	
	
	
	/**
	 * Convert an integer index (From a combo box) into a door type
	 * 
	 * @param index The index to convert
	 * @return Associated door type
	 */
	public static DoorType fromIndex(int index) {
		switch (index) {
		case 0:
			return DoorType.NO_DOOR;
		case 1:
			return DoorType.RED_DOOR;
		case 2:
			return DoorType.YELLOW_DOOR;
		case 3:
			return DoorType.GREEN_DOOR;
		case 4:
			return DoorType.BLUE_DOOR;
		default:
			return DoorType.NO_DOOR;
		}
	}

	
	/**
	 * Get the index in the combo boxes for this liquid
	 * 
	 * @return Index
	 */
	public int getIndex() {
		return this.index;
	}
	
	
	/**
	 * Get the color for this door
	 * 
	 * @return Color
	 */
	public Color getColor() {
		return this.color;
	}
	
	
	/**
	 * Get array of colors to use in combo box custom renderers
	 * @return Color Array
	 */
	public static Color[] getColorArray() {
		return new Color[]{Color.BLACK, new Color(200,0,0) ,new Color(200,200,0), new Color(0,200,0), new Color(0,64,255)};
	}
	
	
	/**
	 * Get the string array to use in combo boxes
	 * @return String array
	 */
	public static String[] getStringsArray() {
		return new String[]{"(No Door)","Red Door","Yellow Door","Green Door","Blue Door"};
	}
	
	
	/**
	 * Get the name of this door type
	 * @return Name
	 */
	public String getName() {
		return this.name;
	}
	
	
	/**
	 * Get the export value to use to generate Python code
	 * @return Export value
	 */
	public int getExportValue() {
		return this.exportValue;
	}
	
	
	
	
	/**
	 * Get the lock image for drawing the door
	 * 
	 * @return Lock Image
	 */
	public Image getLock(Direction dir) {
		
		if (this == DoorType.NO_DOOR) {return noLock;}
		
		switch (dir) {
		case TOP:
			return topLock;
		case BOTTOM:
			return bottomLock;
		case LEFT:
			return leftLock;
		case RIGHT:
			return rightLock;
		default:
			return noLock;
		}
		
	}
	
	
	/**
	 * Test if a given index is a valid door type or not
	 * 
	 * @param index The integer index to test
	 * @return Valid or not?
	 */
	public static boolean validateIndex(int index) {
		if (index >= 0 && index <= 4) {return true;} else {return false;}
	}
}
