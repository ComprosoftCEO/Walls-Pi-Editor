package comprosoft.walls.dungeon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import comprosoft.walls.editor.MainWindow;
import comprosoft.walls.enums.Direction;
import comprosoft.walls.enums.DoorType;
import comprosoft.walls.enums.ItemType;
import comprosoft.walls.enums.LiquidType;
import comprosoft.walls.enums.WallType;

/**
 * Defines a single room in the dungeon
 */
public class Room {

	//Holds doors, walls, and liquids for all directions
	private WallType[] walls = new WallType[4];
	private DoorType[] doors = new DoorType[4];
	private LiquidType[] liquids = new LiquidType[4];
	
	private boolean lockedWalls[] = new boolean[4];
	
	//Also holds information for center
	private ItemType roomItem;
	private LiquidType roomLiquid;
	
	private static Random rand = new Random();
	private Color wallColor = randomColor();
	private boolean notUsed = false;
	
	
	
	/**
	 * Create a new dungeon room.
	 */
	public Room() {
		this.roomItem = ItemType.NO_ITEM;
		this.roomLiquid = LiquidType.NO_LIQUID;
		
		for (int i = 0; i < 4; i++) {
			walls[i] = WallType.NO_WALL;
			doors[i] = DoorType.NO_DOOR;
			liquids[i] = LiquidType.NO_LIQUID;
			lockedWalls[i] = false;
		}
	}
	
	
	
	/**
	 * Create a new dungeon room
	 * 
	 * @param topWall Type of wall to use on top of room
	 * @param bottomWall Type of wall to use on bottom of room
	 * @param leftWall Type of wall to use on left of room
	 * @param rightWall Type of wall to use on right of room
	 * @param topDoor Type of door to use on top of room
	 * @param bottomDoor Type of door to use on bottom of room
	 * @param leftDoor Type of door to use on left of room
	 * @param rightDoor Type of door to use on right of room
	 * @param topLiquid Type of liquid to use on top of room
	 * @param bottomLiquid Type of liquid to use on bottom of room
	 * @param leftLiquid Type of liquid to use on left of room
	 * @param rightLiquid Type of liquid to use on right of room
	 * @param roomItem Item to place in center of room
	 * @param roomLiquid Liquid to place around center of room
	 */
	public Room(WallType topWall, WallType bottomWall, WallType leftWall, WallType rightWall,
				DoorType topDoor, DoorType bottomDoor, DoorType leftDoor, DoorType rightDoor,
				LiquidType topLiquid, LiquidType bottomLiquid, LiquidType leftLiquid, LiquidType rightLiquid,
				ItemType roomItem, LiquidType roomLiquid) {
	
		
		this.walls[0] = topWall;
		this.walls[1] = bottomWall;
		this.walls[2] = leftWall;
		this.walls[3] = rightWall;
		
		this.doors[0] = topDoor;
		this.doors[1] = bottomDoor;
		this.doors[2] = leftDoor;
		this.doors[3] = rightDoor;
		
		this.liquids[0] = topLiquid;
		this.liquids[1] = bottomLiquid;
		this.liquids[2] = leftLiquid;
		this.liquids[3] = rightLiquid;
		
		this.roomItem = roomItem;
		this.roomLiquid = roomLiquid;
	}
	
	
	/**
	 * Configure a wall type for the room
	 * 
	 * @param t The new wall type
	 * @param d The direction in the room
	 */
	public void setWall(WallType t, Direction d) {
		this.walls[d.getIndex()] = t;
	}
	
	
	/**
	 * Enable or disable a wall locked type.
	 * 
	 * A locked wall CANNOT be secret or open
	 * 
	 * @param d The direction in the room
	 * @param isLocked New locked or unlocked state
	 */
	public void setWallLocked(Direction d, boolean isLocked) {
		this.lockedWalls[d.getIndex()] = isLocked;
	}
	
	
	
	/**
	 * Configure a door type in the room
	 * 
	 * @param d The new door type
	 * @param dir Direction in the room
	 */
	public void setDoor(DoorType d, Direction dir) {
		this.doors[dir.getIndex()] = d;
	}
	
	
	/**
	 * Configure a liquid type in the room
	 * 
	 * @param l The new liquid type
	 * @param dir The direction in the room
	 */
	public void setLiquid(LiquidType l, Direction dir) {
		this.liquids[dir.getIndex()] = l;
	}
	
	
	/**
	 * Set the liquid type for the room
	 * 
	 * @param l New liquid type
	 */
	public void setRoomLiquid(LiquidType l) {
		this.roomLiquid = l;
	}
	
	
	/**
	 * Set the item for the room 
	 * 
	 * @param i The new item type
	 */
	public void setRoomItem(ItemType i) {
		this.roomItem = i;
	}
	
	
	
	/**
	 * Get the wall type for a direction in the room
	 * 
	 * @param d Direction
	 * @return Wall Type
	 */
	public WallType getWall(Direction d) {
		return this.walls[d.getIndex()];
	}
	
	
	/**
	 * Get the locked state of a wall
	 * 
	 * @param d The direction in a room
	 * @return Locked or unlocked wall
	 */
	public boolean getLockedWall(Direction d) {
		return this.lockedWalls[d.getIndex()];
	}
	
	
	/**
	 * Get the liquid type for a direction in the room
	 * 
	 * @param d Direction
	 * @return Liquid Type
	 */
	public LiquidType getLiquid(Direction d) {
		return this.liquids[d.getIndex()];
	}
	
	
	/**
	 * Get the door type for a direction in the room
	 * 
	 * @param d Direction
	 * @return Door Type
	 */
	public DoorType getDoor(Direction d) {
		return this.doors[d.getIndex()];
	}
	
	
	/**
	 * Get the liquid type for the center of the room
	 * 
	 * @return Liquid Type
	 */
	public LiquidType getRoomLiquid() {
		return this.roomLiquid;
	}
	
	
	/**
	 * Get the item in the room
	 * 
	 * @return Item Type
	 */
	public ItemType getRoomItem() {
		return this.roomItem;
	}
	
	
	
	/**
	 * Get the random wall color associated for this room
	 * 
	 * @return Wall Color
	 */
	public Color getWallColor() {
		return this.wallColor;
	}
	
	
	/**
	 * Set the wall color to a new, random color
	 */
	public void newWallColor() {
		this.wallColor = randomColor();
	}
	
	
	
	/**
	 * Set the new wall color for the room
	 * 
	 * @param c New Wall Color
	 */
	public void setWallColor(Color c) {
		this.wallColor = c;
	}
	
	
	/**
	 * Test if a room is not used<br>
	 * <br>
	 * Merely hides the room from being visible in the map room
	 * 
	 * @return Not Used
	 */
	public boolean notUsed() {
		return this.notUsed;
	}
	
	
	
	/**
	 * Set if a room is not used.
	 * <br>
	 * Only hides a room from the map, nothing else.
	 * 
	 * @param notUsed
	 */
	public void setUsed(boolean notUsed) {
		this.notUsed = notUsed;
	}
	
	/**
	 * Pick a random color, guaranteed not to be 000
	 * @return Random Color
	 */
	public static Color randomColor() {
		
		int r = 0;
		int g = 0;
		int b = 0;
		
		while (r < 96 && g < 96 && b < 96) {
			r = rand.nextInt(256);
			g = rand.nextInt(256);
			b = rand.nextInt(256);
		}
		
		return new Color(r,g,b);
	}
	
	
	/**
	 * Export this room object to a Python array string
	 * 
	 * @return Export string
	 */
	public String export() {
		
		String retStr="[";
		
		int vals[] = new int[]{0,0,0,0};
		int item = 0;
		
		for (int i = 0; i < 4; i++) {
			vals[i] +=walls[i].getExportValue();
			vals[i] += doors[i].getExportValue();
			vals[i] +=liquids[i].getExportValue(false);
		}
		
		item+=roomItem.getExportValue();
		item+=roomLiquid.getExportValue(true);
		
		//Now build the string
		for (int i = 0; i < 4; i++) {
			retStr+=vals[i] + ",";
		}
		retStr+=item + "]";
		
		return retStr;
	}
	
	
	public String toString() {
		return this.export();
	}
	
	
	
	/**
	 * Build this room as an image
	 * 
	 * @param isStart Is this the starting room or not
	 * @return Room Image
	 */
	public Image buildImage(boolean isStart) {
		
		Image img = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) img.getGraphics().create();
		
		
		//Draw the background
		g2.setColor(MainWindow.backColor);
		g2.fillRect(0, 0, 256, 256);
		
		//Do nothing if this room is not to be used
		if (this.notUsed) {
			return img;
		}
		
		
		//Certain sections of walls are always visible
		g2.setColor(this.wallColor);
		g2.fillRect(0,0,32*3,32);
		g2.fillRect(0, 0, 32, 32*3);
		g2.fillRect(5*32,0,32*3,32);
		g2.fillRect(7*32,0,32,3*32);
		g2.fillRect(0,5*32,32,3*32);
		g2.fillRect(0,7*32,3*32,32);
		g2.fillRect(5*32,7*32,3*32,32);
		g2.fillRect(7*32,5*32,32,3*32);
		
		

		//Draw all of the walls
		g2.drawImage(getWallImage(this.walls[0], false), 2*32, 0, null);
		g2.drawImage(getWallImage(this.walls[1], false), 2*32, 7*32, null);
		g2.drawImage(getWallImage(this.walls[2], true), 0, 2*32, null);
		g2.drawImage(getWallImage(this.walls[3], true), 7*32, 2*32, null);
		
		//Draw all of the liquids
		drawLiquidRectangle(g2,3,1,2,2,this.liquids[0]);
		drawLiquidRectangle(g2,3,5,2,2,this.liquids[1]);
		drawLiquidRectangle(g2,1,3,2,2,this.liquids[2]);
		drawLiquidRectangle(g2,5,3,2,2,this.liquids[3]);
		
		//Calculate corners
		drawLiquidRectangle(g2,1,1,2,2,
				LiquidType.getDominantLiquid(this.liquids[0], this.liquids[2]));
		drawLiquidRectangle(g2,5,1,2,2,
				LiquidType.getDominantLiquid(this.liquids[0], this.liquids[3]));
		drawLiquidRectangle(g2,1,5,2,2,
				LiquidType.getDominantLiquid(this.liquids[1], this.liquids[2]));
		drawLiquidRectangle(g2,5,5,2,2,
				LiquidType.getDominantLiquid(this.liquids[1], this.liquids[3]));
		
		//Draw the doors
		drawDoor(g2, 32*3,32,Direction.TOP,doors[0]);
		drawDoor(g2, 32*3,32*6,Direction.BOTTOM,this.doors[1]);
		drawDoor(g2, 32,32*3,Direction.LEFT,this.doors[2]);
		drawDoor(g2, 32*6,32*3,Direction.RIGHT,this.doors[3]);
	
		
		//Draw item in the center of the room
		g2.drawImage(this.roomItem.getImage(), 3*32,3*32, 64, 64,null);
		
		
		//Finally, draw the center liquid
		if (this.roomLiquid != LiquidType.NO_LIQUID) {
			drawLiquidRectangle(g2,2,2,4,1,this.roomLiquid);
			drawLiquidRectangle(g2,2,5,4,1,this.roomLiquid);
			drawLiquidRectangle(g2,2,3,1,2,this.roomLiquid);
			drawLiquidRectangle(g2,5,3,1,2,this.roomLiquid);
			
		}
		
		//Draw the start text
		if (isStart) {
			g2.setColor(Color.WHITE);
			g2.fillRect(64+24+4, 128+8, 64+8, 32);
			g2.setColor(Color.black);
			g2.drawRect(64+24+4, 128+8, 64+8, 32);
			g2.setFont(new Font(g2.getFont().getFontName(), Font.PLAIN, 24));
			g2.drawString("Start", 64+32+4, 128+32);
		}
		
		return img;
		
	}
	
	
	/**
	 * Build the image for the various walls
	 * 
	 * @param t WallType to draw
	 * @param isVertical Is this vertical or horizontal wall
	 * @return Image
	 */
	private Image getWallImage(WallType t, boolean isVertical) {
		
		Image img;
		Graphics2D temp_g; 
		
		//Offset so wall can be four wide for exit
		int offset = (t == WallType.EXIT) ? 0 : 32;
		
		if (isVertical) {
			img = new BufferedImage(32, 128 ,BufferedImage.TYPE_INT_ARGB);
			temp_g = (Graphics2D) img.getGraphics();
			
			SetWallColor(temp_g,t,isVertical);
			temp_g.fillRect(0, offset, 32, 128 - (offset * 2));
			
		} else {
			img = new BufferedImage(128 ,32,BufferedImage.TYPE_INT_ARGB);
			temp_g = (Graphics2D) img.getGraphics();
			
			SetWallColor(temp_g,t,isVertical);
			temp_g.fillRect(offset, 0, 128 - (offset * 2), 32);
		}
		
		return img;
	}
	
	
	
	
	
	
	
	/**
	 * Set the paint to have the right color for the wall
	 * 
	 * @param g Graphics object to draw to
	 * @param t Type of the wall to test
	 * @param isVertical Is this horizontal or vertical wall?
	 */
	private void SetWallColor(Graphics2D g, WallType t, boolean isVertical) {
		
		switch (t) {
		case NO_WALL:
			g.setColor(MainWindow.backColor);
			return;
		case WALL:
			g.setColor(this.wallColor);
			return;
		case SECRET_WALL:
			g.setColor(new Color(wallColor.getRed(), wallColor.getGreen(), wallColor.getBlue(), 128));
			return;
		case EXIT:
			if (isVertical) {g.setPaint(MainWindow.getRainbow(32, 128));}
			else {g.setPaint(MainWindow.getRainbow(128, 32));}
			return;
		}
	}
	
	
	
	
	
	
	/**
	 * Draw a liquid rectangle.<br>
	 * <br>
	 * <b>X,Y,Width,Height correspond to 32x32 squares</b>
	 * 
	 * @param g2 Graphics2D object to draw to
	 * @param startX
	 * @param startY
	 * @param width
	 * @param height
	 * @param l Liquid
	 */
	private void drawLiquidRectangle(Graphics2D g2, int startX, int startY, int width, int height, LiquidType l) {
		
		for (int x = startX * 32; x < (width + startX) * 32; x+=16) {
		for (int y = startY * 32; y < (height + startY) * 32; y+=16) {	
			g2.setColor(l.getDnyamicColor());
			g2.fillRect(x, y, 16, 16);
		}
		}
	}
	
	
	
	/**
	 * Draw the door at position X,Y
	 * 
	 * @param g2 Graphics2D to draw to
	 * @param x X position of door
	 * @param y Y position of door
	 * @param dir The direction of the door
	 * @param door The door type to draw
	 */
	private void drawDoor(Graphics2D g2, int x, int y, Direction dir,  DoorType door) {
		
		if (door == DoorType.NO_DOOR) {return;}
		
		g2.setColor(door.getColor());
		
		if (dir.isVertical()) {
			g2.fillRect(x,y,32,64);
			g2.setColor(Color.WHITE);
			g2.drawRect(x, y, 32, 64);
		
			//Also draw the lock image
			g2.drawImage(door.getLock(dir), x, y+16,32, 32, null );		
			
		} else {
			g2.fillRect(x, y, 64, 32);
			g2.setColor(Color.white);
			g2.drawRect(x, y, 64, 32);
			
			//Also draw the lock image
			g2.drawImage(door.getLock(dir), x+16, y, 32, 32, null );		
		}
		

	}
	
	
	
	/**
	 * Clear all properties in the room
	 */
	public void clearRoom() {
		
		this.walls[0] = WallType.NO_WALL;
		this.walls[1] = WallType.NO_WALL;
		this.walls[2] = WallType.NO_WALL;
		this.walls[3] = WallType.NO_WALL;
		
		this.doors[0] = DoorType.NO_DOOR;
		this.doors[1] = DoorType.NO_DOOR;
		this.doors[2] = DoorType.NO_DOOR;
		this.doors[3] = DoorType.NO_DOOR;
		
		this.liquids[0] = LiquidType.NO_LIQUID;
		this.liquids[1] = LiquidType.NO_LIQUID;
		this.liquids[2] = LiquidType.NO_LIQUID;
		this.liquids[3] = LiquidType.NO_LIQUID;
		
		this.roomItem = ItemType.NO_ITEM;
		this.roomLiquid = LiquidType.NO_LIQUID;
	
		this.notUsed = false;
		
	}
	
	
	/**
	 * Replace the current room with all of the data in the new room
	 * 
	 * @param r The new room
	 */
	public void replaceRoom(Room r) {
		
		for (int i = 0; i < 4; i++) {
			this.walls[i] = r.walls[i];
			this.doors[i] = r.doors[i];
			this.liquids[i] = r.liquids[i];
		}
		
		this.roomLiquid = r.roomLiquid;
		this.roomItem = r.roomItem;
		this.wallColor = r.wallColor;
		this.notUsed = r.notUsed;
	}
	
	
	
	/**
	 * Get the data for this room as an integer array.<br>
	 * <br>
	 * Used when saving to a file
	 * 
	 * @return Integer array holding all of the data
	 */
	public int[] getData() {
		
		//Array is always the same size
		int[] dataArr = new int[15];
		
		for (int i = 0; i < 4; i++) {
			dataArr[i] = this.walls[i].getIndex();
			dataArr[i+4] = this.doors[i].getIndex();
			dataArr[i+8] = this.liquids[i].getIndex();
		}
		
		dataArr[12] = this.roomLiquid.getIndex();
		dataArr[13] = this.roomItem.getIndex();
		
		//Export colors
		/*dataArr[14] = this.wallColor.getRed();
		dataArr[15] = this.wallColor.getGreen();
		dataArr[16] = this.wallColor.getBlue();*/
		
		//Finally, is this room being used?
		dataArr[14] = (this.notUsed == false) ? 0 : 1;
		
		return dataArr;
		
	}

	
	
	/**
	 * Load the room with the integer data array read from file
	 * 
	 * @param data The integer data array
	 * @param r The room to write to
	 * @return If it succeeded or failed
	 */
	public static boolean loadRoom(int[] data, Room r) {
		
		//Validate the array length
		if (data.length != 15) {
			System.out.println("Invalid data length " + data.length);
			return false;
		}
		
		
		Room tempRoom = new Room();
		
		for (int i = 0; i < 4; i++) {
			if (!WallType.validateIndex(data[i])) {
				System.out.println("Invalid wall type " + data[i]);
				return false;
			} 
			tempRoom.setWall(WallType.fromIndex(data[i]), Direction.fromIndex(i));
			
			if (!DoorType.validateIndex(data[i+4])) {
				System.out.println("Invalid door type " + data[i+4]);
				return false;
			}
			tempRoom.setDoor(DoorType.fromIndex(data[i+4]), Direction.fromIndex(i));
			
			if (!LiquidType.validateIndex(data[i+8])) {
				System.out.println("Invalid liquid type " + data[i+8]);
				return false;
			}
			tempRoom.setLiquid(LiquidType.fromIndex(data[i+8]), Direction.fromIndex(i));
		}
		
		
		
		if (!LiquidType.validateIndex(data[12])) {
			System.out.println("Invalid liquid type " + data[12]);
			return false;
		}
		tempRoom.setRoomLiquid(LiquidType.fromIndex(data[12]));
		
		if (!ItemType.validateIndex(data[13])) {
			System.out.println("Invalid item type " + data[13]);
			return false;
		}
		tempRoom.setRoomItem(ItemType.fromIndex(data[13]));
		
		//Validate the colors
		/*if (data[14] < 0 || data[14] > 255 ||
			data[15] < 0 || data[15] > 255 ||
			data[16] < 0 || data[16] > 255) {
			System.out.println("Invalid color");
			return false;
		}
		tempRoom.setWallColor(new Color(data[14], data[15], data[16]));
		*/
		
		//Validate isUsed
		if (data[14] < 0 || data[14] > 1) {
			System.out.println("Invalid isUsed boolean " + data[14]);
			return false;
		}
		tempRoom.setUsed((data[14] == 0 ? false : true));
		
		
		//We be gucci. Update the room
		r.replaceRoom(tempRoom);
		return true;
		
	}
}
