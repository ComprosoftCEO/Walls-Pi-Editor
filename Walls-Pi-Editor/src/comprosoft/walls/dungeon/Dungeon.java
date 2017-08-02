package comprosoft.walls.dungeon;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import comprosoft.walls.enums.Direction;
import comprosoft.walls.enums.ItemType;
import comprosoft.walls.enums.WallType;

/**
 * Represents a collection of rooms in the dungeon
 */
public class Dungeon {

	Room[][][] dungeonArray;
	
	//Secret word used to identify the file as valid
	private static final int[] secretWord = new int[]{54,27,06,93};
	
	private int length;
	private int width;
	private int height;
	
	private int startX = 0;
	private int startY = 0;
	private int startZ = 0;
	
	
	/**
	 * Create a new Dungeon object
	 * 
	 * @param length The X length of the dungeon
	 * @param width The Y length of the dungeon
	 * @param height The number of levels in the dungeon
	 */
	public Dungeon(int length, int width, int height) {
		
		this.length = length;
		this.width = width;
		this.height = height;
		
		dungeonArray = new Room[length][width][height]; 
		
		//Fill with blank rooms
		this.clearDungeon();
	}
	
	
	/**
	 * Get a room at a given position in the dungeon
	 * 
	 * @param x X coordinate of the room
	 * @param y Y coordinate of the room
	 * @param z Z coordinate of the room
	 * @return
	 */
	public Room getRoom(int x, int y, int z) {
		return dungeonArray[x][y][z];
	}
	
	
	/**
	 * Update the room at a given position in the dungeon
	 * 
	 * @param x X coordinate of the room
	 * @param y Y Coordinate of the room
	 * @param z Z Coordinate of the room
	 * @param r The new room
	 */
	public void setRoom(int x, int y, int z, Room r) {
		this.dungeonArray[x][y][z] = r;
		
		//Enforce locked walls on a specific room in the dungeon
		this.testLockRoom(x,y,z);
		
	}
	
	
	
	/**
	 * Clear the dungeon and reset to default room values
	 */
	public void clearDungeon() {
		clearArray(this.dungeonArray);
		enforceLockedWalls(this.dungeonArray);
		
		//Reset start
		this.startX = 0;
		this.startY = 0;
		this.startZ = 0;
	}
	
	
	/**
	 * Clear array of rooms back to default room array
	 * 
	 * @param arr The array to clear
	 */
	private static void clearArray(Room[][][] arr) {
		for (int x = 0; x < arr.length; x++) {
		for (int y = 0; y < arr[x].length; y++) {
		for (int z = 0; z < arr[x][y].length; z++) {
			arr[x][y][z] = new Room();
		}
		}	
		}
	}
	
	
	/**
	 * Enforced lock walls on a room array
	 * 
	 * @param arr Room array to enforce upon
	 */
	private static void enforceLockedWalls(Room[][][] arr) {
		for (int x = 0; x < arr.length; x++) {
			for (int y = 0; y < arr[x].length; y++) {
			for (int z = 0; z < arr[x][y].length; z++) {
				
				if (x == 0) {
					arr[x][y][z].setWall(WallType.WALL, Direction.LEFT);
					arr[x][y][z].setWallLocked(Direction.LEFT, true);
				}
				if (x == arr.length - 1) {
					arr[x][y][z].setWall(WallType.WALL, Direction.RIGHT);
					arr[x][y][z].setWallLocked(Direction.RIGHT, true);
				}
				if (y == 0) {
					arr[x][y][z].setWall(WallType.WALL, Direction.TOP);
					arr[x][y][z].setWallLocked(Direction.TOP, true);
				}
				if (y == arr[x].length - 1) {
					arr[x][y][z].setWall(WallType.WALL, Direction.BOTTOM);
					arr[x][y][z].setWallLocked(Direction.BOTTOM, true);
				}
			}
			}	
			}
	}
	
	
	/**
	 * Resize the dungeon to the new parameters
	 *  
	 * @param newLen New length of the dungeon (X-Axis)
	 * @param newWid New width of the dungeon (Y-Axis)
	 * @param newHei New height of the dungeon (-Axis)
	 */
	public void resizeDungeon(int newLen, int newWid, int newHei) {
		

		//Create the new array (and clear it)
		Room[][][] newArr = new Room[newLen][newWid][newHei];
		clearArray(newArr);
		
		
		//Now copy over the new values
		for (int x = 0; x < this.dungeonArray.length && x < newArr.length; x++) {
		for (int y = 0; y < this.dungeonArray[x].length && y < newArr[x].length; y++) {
		for (int z = 0; z < this.dungeonArray[x][y].length && z < newArr[x][y].length; z++) {
			newArr[x][y][z] = dungeonArray[x][y][z];
		
			//Update right and bottom walls, which change
			//   when room is resized
			if (x == this.length - 1 && x != newLen - 1) {
				newArr[x][y][z].setWall(WallType.NO_WALL, Direction.RIGHT);
				newArr[x][y][z].setWallLocked(Direction.RIGHT, false);
			}
			if (y == this.width - 1 && y != newWid - 1) {
				newArr[x][y][z].setWall(WallType.NO_WALL, Direction.BOTTOM);
				newArr[x][y][z].setWallLocked(Direction.BOTTOM, false);
			}
			
		}	
		}	
		}
		
		enforceLockedWalls(newArr);
		

		//Get the new length, width, height
		this.length = newLen;
		this.width = newWid;
		this.height = newHei;
		
		//Figure out when to reset xyz
		if (this.startX >= this.length ||
				this.startY >= this.width ||
				this.startZ >= this.height) {
			this.startX = 0;
			this.startY = 0;
			this.startZ = 0;
		}
		
		//System.out.println(length + "," +width +"," + height);
		
		//Update the reference
		this.dungeonArray = newArr;
		
	}
	
	
	/**
	 * Replace all the data in this dungeon with a new dungeon
	 * 
	 * @param d The new dungeon to replace
	 */
	public void replace(Dungeon d) {
		
		this.length = d.length;
		this.width = d.width;
		this.height = d.height;
		
		this.startX = d.startX;
		this.startY = d.startY;
		this.startZ = d.startZ;
		
		//Copy all of the rooms
		this.dungeonArray = new Room[this.length][this.width][this.height];
		for (int x = 0; x < this.length; x++) {
		for (int y = 0; y < this.width; y++) {
		for (int z = 0; z < this.height; z++) {
			this.dungeonArray[x][y][z] = d.dungeonArray[x][y][z];
		}
		}	
		}
	}
	
	
	
	/**
	 * X size of the dungeon
	 * 
	 * @return Length
	 */
	public int getLength() {
		return this.length;
	}
	
	
	
	/**
	 * Y size of the dungeon
	 * 
	 * @return Width
	 */
	public int getWidth() {
		return this.width;
	}
	
	
	/**
	 * Z size of the dungeon
	 * 
	 * @return Height
	 */
	public int getHeight() {
		return this.height;
	}
	
	
	
	/**
	 * Get the X start position
	 * 
	 * @return startX
	 */
	public int getStartX() {
		return this.startX;
	}
	
	/**
	 * Get the Y start position
	 * 
	 * @return startY
	 */
	public int getStartY() {
		return this.startY;
	}
	
	
	/**
	 * Get the Z start position
	 * 
	 * @return startZ
	 */
	public int getStartZ() {
		return this.startZ;
	}
	
	
	/**
	 * Get the total count of energy in the dungeon
	 * 
	 * @return Energy
	 */
	public int getEnergy() {
		
		int count = 0;
		
		for (int x = 0; x < this.getLength(); x++){
		for (int y = 0; y < this.getWidth(); y++) {
		for (int z = 0; z < this.getHeight(); z++)
			if (this.dungeonArray[x][y][z].getRoomItem() == ItemType.EXIT_ENERGY) {
				count++;
			}
		}		
		}
		
		return count;
	}
	
	
	
	/**
	 * Set the start position in the dungeon
	 * 
	 * @param x X start position
	 * @param y Y start position
	 * @param z Z start position
	 */
	public void setStartPosition(int x, int y, int z) {
	
		if (x >= this.length ||
				y >= this.width ||
				z >= this.height) {
			System.out.println("Illegal start position [" + x + "," +y + "," + z +"]");
			return;
		}
		
		this.startX = x;
		this.startY = y;
		this.startZ = z;
		
	}
	
	
	/**
	 * Export the dungeon to a Python array
	 * 
	 * @return Export string
	 */
	public String export() {
		
		String arrStr =  Arrays.deepToString(this.dungeonArray);
		String retStr = "[";
		
		//Add some enters to maake formatting easier
		arrStr = arrStr.replaceAll(Pattern.quote("]],"), "]],\n");
		arrStr = arrStr.replaceAll(Pattern.quote("]]],"), "]]],\n");
		
		//Add the other information
		retStr+=this.startX + ",";
		retStr+=this.startY + ",";
		retStr+=this.startZ + ",";
		retStr+=this.getEnergy()+"\n";
		retStr+=arrStr+"]";
		
		
		
		return retStr;
	}
	
	
	
	/**
	 * Build the dungeon image
	 * 
	 * @param z The z level to draw
	 * @return Built image
	 */
	public Image buildImage(int z) {
		
		Image img = new BufferedImage(this.length * 256, this.width * 256,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) img.getGraphics().create();
		
		for (int x = 0; x < this.length; x++) {
		for (int y = 0; y < this.width; y++) {
			g2.drawImage(this.dungeonArray[x][y][z].buildImage(
				x == this.startX && y == this.startY && z == this.startZ), 256*x, 256*y, null);
		}
		}
		
		return img;
	}
	
	
	/**
	 * Clear the room at X,Y,Z position
	 * 
	 * @param x X coordinate of the room to clear
	 * @param y Y coordinate of the room to clear
	 * @param z Z coordinate of the room to clear
	 */
	public void clearRoom(int x, int y, int z) {
		
		//Validate XYZ
		if (x >= this.length ||
				y >= this.width ||
				z >= this.height) {
			System.out.println("Illegal start position [" + x + "," +y + "," + z +"]");
			return;
		}
		
		
		this.dungeonArray[x][y][z].clearRoom();
		
		//Update all these walls
		this.testLockRoom(x,y,z);
		
	}
	
	
	
	/**
	 * Test a room if it needs to be locked, and force it to do so.
	 * 
	 * 	Avoids having to retest all rooms when only one is changed!
	 * 
	 * @param x X coordinate of the room
	 * @param y Y coordinate of the room
	 * @param z Z coordinate of the room
	 */
	private void testLockRoom(int x, int y, int z) {
		
		//Update all these walls
		if (x == 0) {
			this.dungeonArray[x][y][z].setWall(WallType.WALL, Direction.LEFT);
			this.dungeonArray[x][y][z].setWallLocked(Direction.LEFT, true);
		}
		if (x == this.dungeonArray.length - 1) {
			this.dungeonArray[x][y][z].setWall(WallType.WALL, Direction.RIGHT);
			this.dungeonArray[x][y][z].setWallLocked(Direction.RIGHT, true);
		}
		if (y == 0) {
			this.dungeonArray[x][y][z].setWall(WallType.WALL, Direction.TOP);
			this.dungeonArray[x][y][z].setWallLocked(Direction.TOP, true);
		}
		if (y == this.dungeonArray[x].length - 1) {
			this.dungeonArray[x][y][z].setWall(WallType.WALL, Direction.BOTTOM);
			this.dungeonArray[x][y][z].setWallLocked(Direction.BOTTOM, true);
		}
	}
	
	
	/**
	 * Load a dungeon from the file input stream
	 * 
	 * @param fin Input File
	 * @param d Dungeon to load To
	 * @return True if successful, false if not
	 * @throws IOException 
	 */
	public static boolean loadDungeon(FileInputStream fin, Dungeon d) throws IOException {
		
		//First, read magic number
		for (int i = 0; i < 4; i++) {
			if (fin.read() != Dungeon.secretWord[i]) {
				System.out.println("Invalid secret word!");
				return false;
			}
		}
		
		//Now read length, width, and height
		int length = fin.read();
		int width = fin.read();
		int height = fin.read();
		
		//Validate these things
		if (length < 1 || length > 100 ||
			width < 1 || width > 100 ||
			height < 1 || height > 100) {
			System.out.println("Invalid length, width, or height.");
			return false;
		}
		
		//Make a new dungeon
		Dungeon dung = new Dungeon(length, width, height);
		
		int sX = fin.read();
		int sY = fin.read();
		int sZ = fin.read();
		
		//Validate starting positions
		if (sX < 0 || sX >= length ||
			sY < 0 || sY >= width ||
			sZ < 0 || sZ >= height) {
			System.out.println("Invalid start position");
			return false;
		}
		
		dung.setStartPosition(sX, sY, sZ);
		
		
		//Do all these rooms
		for (int x = 0; x < length; x++) {
		for (int y = 0; y < width; y++) {
		for (int z = 0; z < height; z++) {
			
			int[] roomArr = new int[15];
			
			for (int i = 0; i < 15; i++) {
				roomArr[i] = fin.read();
			}
			
			Room r = new Room();
			if (Room.loadRoom(roomArr, r) == false) {
				System.out.println("Invalid room ["+x+","+y+","+z+"]");
				return false;
			}
			dung.setRoom(x, y, z, r);
			
		}
		}
		}
		
		
		//Everything is all good in the hood
		//   update the dungeon
		d.replace(dung);
		return true;
	}
	
	
	
	/**
	 * Save the dungeon to a file
	 * 
	 * @param fw The file to write to
	 * @throws IOException 
	 */
	public void saveDungeon(FileWriter fw) throws IOException {
		
		//First write the secret word
		for (int i = 0; i < Dungeon.secretWord.length; i++) {
			fw.write(Dungeon.secretWord[i]);
		}
		
		//Next write length, width, height
		fw.write(this.length);
		fw.write(this.width);
		fw.write(this.height);
		
		//And start position
		fw.write(this.startX);
		fw.write(this.startY);
		fw.write(this.startZ);
		
		//Now write all of the data
		for (int x = 0; x < this.length; x++) {
		for (int y = 0; y < this.width; y++) {
		for (int z = 0; z < this.height; z++) {
			
			int[] roomArr = this.dungeonArray[x][y][z].getData();
			for (int i = 0; i < roomArr.length; i++) {
				fw.write((byte) roomArr[i] & 0xff);
			}
		}
		}
		}
		
	}
	
}
