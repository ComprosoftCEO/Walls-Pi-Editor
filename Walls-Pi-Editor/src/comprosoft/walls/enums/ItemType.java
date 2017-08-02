package comprosoft.walls.enums;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public enum ItemType {
	NO_ITEM (0,"Item/NoItem.png",0),
	RED_KEY (1,"Item/RedKey.png",3),
	YELLOW_KEY (2,"Item/YellowKey.png",6),
	GREEN_KEY (3,"Item/GreenKey.png",9),
	BLUE_KEY (4,"Item/BlueKey.png",12),
	BOOTS (5,"Item/Boots.png",15),
	TORCH (6,"Item/Torch.png",18),
	STAIR_UP (7,"Item/StairUp.png",21),
	STAIR_DOWN (8,"Item/StairDown.png",24),
	STAIR_UP_DOWN (9,"Item/StairUpDown.png",27),
	EXIT_ENERGY(10,"Item/Energy.png",30);
	
	
	//Index in combo boxes
	private final int index;
	private final Image itemImage;
	private final int exportValue;
	
	
	/**
	 * Create a new Item Type
	 * 
	 * @param index Index in the combo boxes
	 * @param image Small icon that represents the item
	 * @parm export Integer value to use when exporting to Python code
	 */
	ItemType(int index, String image, int export) {
		this.index = index;
		this.exportValue = export;
		
		//Load image
		BufferedImage img = null;
		try {
			URL path = ItemType.class.getResource("/"+image);
			img = ImageIO.read(path);
		} catch (IOException e) {
			System.out.println("Invalid image file " +image);
		}
		this.itemImage = img;

	}
	
	
	
	
	/**
	 * Convert a combo box index into an item type
	 * 
	 * @param index The index to convert
	 * @return Associated item type
	 */
	public static ItemType fromIndex(int index) {
		switch (index) {
		case 0:
			return ItemType.NO_ITEM;
		case 1:
			return ItemType.RED_KEY;
		case 2:
			return ItemType.YELLOW_KEY;
		case 3:
			return ItemType.GREEN_KEY;
		case 4:
			return ItemType.BLUE_KEY;
		case 5:
			return ItemType.BOOTS;
		case 6:
			return ItemType.TORCH;
		case 7:
			return ItemType.STAIR_UP;
		case 8: 
			return ItemType.STAIR_DOWN;
		case 9:
			return ItemType.STAIR_UP_DOWN;
		case 10:
			return ItemType.EXIT_ENERGY;
		default:
			return ItemType.NO_ITEM;
		}
	}
	
	
	
	/**
	 * Get index associated with this item
	 * 
	 * @return Index
	 */
	public int getIndex() {
		return this.index;
	}

	
	
	/**
	 * Get image associated with this item
	 * @return Image
	 */
	public Image getImage() {
		return this.itemImage;
	}
	
	
	/**
	 * Get the integer value to use when exporting to Python code
	 * 
	 * @return Export Value
	 */
	public int getExportValue() {
		return this.exportValue;
	}
	
	
	
	/**
	 * Test if a given index is a valid item type or not
	 * 
	 * @param index The integer index to test
	 * @return Valid or not?
	 */
	public static boolean validateIndex(int index) {
		if (index >= 0 && index <= 10) {return true;} else {return false;}
	}
	
	
	
	
	/**
	 * Get the color array to use when rendering combo boxes
	 * 
	 * @return Color Array
	 */
	public static Color[] getColorArray() {
		return new Color[]{Color.black, new Color(200,0,0) ,new Color(200,200,0), new Color(0,200,0), new Color(0,64,255),
							new Color(0,100,100), new Color(200,100,0), 
							new Color(150,75,75), new Color(75,150,150), new Color(128,128,128),
							new Color(200,200,0)};
	}
}
