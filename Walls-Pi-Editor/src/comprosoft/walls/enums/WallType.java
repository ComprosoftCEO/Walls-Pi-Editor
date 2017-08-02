package comprosoft.walls.enums;

import java.awt.Color;

import comprosoft.walls.editor.MainWindow;

public enum WallType {
	NO_WALL (MainWindow.backColor, "","",0,0),
	WALL (MainWindow.wallColor,"Wall","W",1,30),
	SECRET_WALL (MainWindow.backColor,"Secret","S",2,45),
	EXIT (new Color(255,255,255),"Exit","E",3,15);
	
	//Color to set button when clicked
	private final Color buttonColor;
	
	//Test to draw in buttons
	private final String longName;
	private final String shortName;
	private final int index;
	private final int exportValue;
	
	/**
	 * Create a new wall type
	 * 
	 * @param butColor The color of the button for this wall
	 * @param longN The long name (horizontal buttons)
	 * @param shortN (vertical buttons)
	 * @param export Integer export to use when exporting to Python code
	 */
	WallType(Color butColor, String longN, String shortN, int index, int export) {
		this.buttonColor = butColor;
		this.longName = longN;
		this.shortName = shortN;
		this.index = index;
		this.exportValue = export;
	}
	
	
	
	/**
	 * Get the button color for this wall type
	 * 
	 * @return Button color
	 */
	public Color getColor() {
		
		if (this.index == 0) {
			return MainWindow.backColor;
		} else if (this.index == 1) {
			return MainWindow.wallColor;
		} else if (this.index == 2){
			Color wallColor = MainWindow.wallColor;
			return new Color(wallColor.getRed(), wallColor.getGreen(), wallColor.getBlue(), 128);
		}else {
			return this.buttonColor;
		}
	}
	
	
	/**
	 * Get the test to display in the button
	 * 
	 * @param isShort Return shortened text (for vertical buttons)
	 * @return Door name
	 */
	public String getName(boolean isShort) {
		if (isShort) {
			return this.shortName;
		} else {
			return this.longName;
		}
	}
	
	
	/**
	 * Get the index associated with this wall
	 * @return Index
	 */
	public int getIndex() {
		return this.index;
	}
	
	
	/**
	 * Convert an integer index to a wall type
	 * 
	 * @param index The integer index
	 * @return Wall Type
	 */
	public static WallType fromIndex(int index) {
		switch (index) {
		case 0:
			return WallType.NO_WALL;
		case 1:
			return WallType.WALL;
		case 2:
			return WallType.SECRET_WALL;
		case 3:
			return WallType.EXIT;
		default:
			return WallType.NO_WALL;
		}
	}
	
	
	
	
	/**
	 * Get the next wall type in the sequence
	 * 
	 * @param t Current WallType
	 * @param isLocked Is this wall locked or not. A locked wall ignores secret or no wall. 
	 * @return New Walltype
	 */
	public static WallType getNext(WallType t, boolean isLocked) {
		switch (t) {
		case NO_WALL:
			return WallType.WALL;
		case WALL:
			return (isLocked) ? WallType.EXIT : WallType.SECRET_WALL;
		case SECRET_WALL:
			return WallType.EXIT;
		case EXIT:
			return (isLocked) ? WallType.WALL : WallType.NO_WALL;
		default:
			return WallType.NO_WALL;
		}
	}
	
	
	
	/**
	 * Get the integer to use when exporting to Python code
	 * 
	 * @return Export Value
	 */
	public int getExportValue() {
		return this.exportValue;
	}


	/**
	 * Test if a given index is a valid wall type or not
	 * 
	 * @param index The integer index to test
	 * @return Valid or not?
	 */
	public static boolean validateIndex(int index) {
		if (index >= 0 && index <= 3) {return true;} else {return false;}
	}

}
