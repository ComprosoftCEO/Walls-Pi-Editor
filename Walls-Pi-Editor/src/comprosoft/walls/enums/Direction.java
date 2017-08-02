package comprosoft.walls.enums;

/**
 * Holds enum for all cardinal directions
 */
public enum Direction {
	TOP (0, false),
	BOTTOM (1, false),
	LEFT (2, true),
	RIGHT (3, true);
	
	private final int index;
	private final boolean isVertical;
	
	Direction(int i, boolean isVertical) {
		this.index = i;
		this.isVertical = isVertical;
	}
	
	
	/**
	 * Get integer index for arrays
	 * 
	 * @return Index
	 */
	public int getIndex() {
		return this.index;
	}
	
	
	/**
	 * Convert an integer index into a direction
	 * 
	 * @param index Integer index to convert to direction
	 * @return Direction
	 */
	public static Direction fromIndex(int index) {
		switch (index) {
		case 0:
			return Direction.TOP;
		case 1:
			return Direction.BOTTOM;
		case 2:
			return Direction.LEFT;
		case 3:
			return Direction.RIGHT;
		default:
			
			//Default is top
			return Direction.TOP;
		
		}
	}
	
	
	
	/**
	 * Test whether this is a vertical or a horizontal direction
	 * 
	 * @return isVertical
	 */
	public boolean isVertical() {
		return this.isVertical;
	}
}
