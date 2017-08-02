package comprosoft.walls.enums;
import java.awt.Color;
import java.util.Random;

import comprosoft.walls.editor.MainWindow;

public enum LiquidType {
	NO_LIQUID (0, MainWindow.backColor, 192,192,192,192,192,192, "", 0, 0),
	WATER (1,Color.cyan, 0,0,0,64,128,255, "Water", 5, 1),
	LAVA (2, Color.orange, 128,255,0,64,0,0, "Lava",10,2);
	
	
	private final int index;
	
	//Used when picking random colors
	private final int redMin;
	private final int redMax;
	private final int greenMin;
	private final int greenMax;
	private final int blueMin;
	private final int blueMax;
	
	private final Color defaultColor;
	private final String name;
	
	//Variables used when exporting to Python
	private final int sideExport;
	private final int centerExport;
	
	private Random r = new Random();
	
	
	
	/**
	 * Create a new liquid type
	 * 
	 * @param index The index in the combo box
	 * @param c The color to be drawn
	 */
	LiquidType(int index, Color defaultColor,
			int rmin, int rmax, int gmin, int gmax, int bmin, int bmax,
			String name,
			int sideExport,
			int centerExport) {
		this.defaultColor = defaultColor;
		this.index = index;
		this.redMin = rmin;
		this.redMax = rmax;
		this.greenMin = gmin;
		this.greenMax = gmax;
		this.blueMin = bmin;
		this.blueMax = bmax;
		this.name= name;
		this.sideExport = sideExport;
		this.centerExport = centerExport;
	}
	
	
	/**
	 * Convert an integer index (combo box index) into a liquid type
	 * 
	 * @param index The index to convert
	 * @return Associated liquid type
	 */
	public static LiquidType fromIndex(int index) {
		switch (index) {
		case 0:
			return LiquidType.NO_LIQUID;
		case 1:
			return LiquidType.WATER;
		case 2:
			return LiquidType.LAVA;
		default:
			return LiquidType.NO_LIQUID;
		}
	}
	
	
	/**
	 * Get combo box index associated with this liquid type
	 * 
	 * @return Index
	 */
	public int getIndex() {
		return this.index;
	}
	
	
	/**
	 * Get color that changes every time the method is called
	 * 
	 * @return Color
	 */
	public Color getDnyamicColor() {
		Color c = new Color(
			r.nextInt((redMax - redMin) + 1) + redMin,
			r.nextInt((greenMax - greenMin) + 1) + greenMin,
			r.nextInt((blueMax - blueMin) + 1) + blueMin);
		
		return c;
	}
	
	
	/**
	 * Get a static color
	 * 
	 * @return Color
	 */
	public Color getColor() {
		return this.defaultColor;
	}
	
	
	/**
	 * Get the name for this liquid
	 * 
	 * @return Name
	 */
	public String getName() {
		return this.name;
	}
	
	
	/**
	 * Get color array to use for custom combo box rendering
	 * 
	 * @return Color array
	 */
	public static Color[] getColorArray() {
		return new Color[]{Color.BLACK, Color.blue, new Color(200,100,0)};
	}
	
	
	/**
	 * Get the string array to use for combo boxes
	 * 
	 * @return String array
	 */
	public static String[] getStringArray() {
		return new String[]{"(No Liquid)","Water","Lava"};
	}
	
	
	/**
	 * Get the value to use when exporting to Python code
	 * 
	 * @param isCenter Is this the liquid in the center of the room?
	 * @return Export integer value
	 */
	public int getExportValue(boolean isCenter) {
		if (isCenter) {
			return this.centerExport;
		} else {
			return this.sideExport;
		}
	}
	
	
	/**
	 * Figure out the dominant liquid between two liquid types
	 * @param one The first liquid to compare
	 * @param two The second liquid to compare
	 * @return Dominant Liquid
	 */
	public static LiquidType getDominantLiquid(LiquidType one, LiquidType two) {
		
		if (one == two) {
			return one;
		} else if (one == LiquidType.NO_LIQUID) {
			return two;
		} else if (two == LiquidType.NO_LIQUID) {
			return one;
		} else {
			
			//Lava is dominant liquid
			return LiquidType.LAVA;
			
		}
	}
	
	
	
	/**
	 * Test if a given index is a valid wall type or not
	 * 
	 * @param index The integer index to test
	 * @return Valid or not?
	 */
	public static boolean validateIndex(int index) {
		if (index >= 0 && index <= 2) {return true;} else {return false;}
	}
}
