package de.tuilmenau.ics.CommonSim.datastream.occurrences.types;


/**
 * Class defining a 3D box.
 * 
 * @author Markus Brueckner
 */
public class Box 
{
	public final Position corner1;
	public final Position corner2;
	
	/**
	 * Create a new box with the given corners.
	 * 
	 * @param p1 The first corner of the box.
	 * @param p2 The second corner of the box.
	 */
	public Box(Position p1, Position p2)
	{
		// normalize the corners to ease implementation of hit tests later
		double minx, maxx, miny, maxy, minz, maxz;
		minx = Math.min(p1.x, p2.x);
		maxx = Math.max(p1.x, p2.x);
		miny = Math.min(p1.y, p2.y);
		maxy = Math.max(p1.y, p2.y);
		minz = Math.min(p1.z, p2.z);
		maxz = Math.max(p1.z, p2.z);
		corner1 = new Position(minx, miny, minz);
		corner2 = new Position(maxx, maxy, maxz);
	}
	
	/**
	 * Check whether the box contains the given point.
	 * 
	 * @param p The point to check.
	 * @return true if the point is within the box (or on the boundaries), false else.
	 */
	public boolean contains(Position p)
	{
		return p.x >= corner1.x && p.x <= corner2.x &&
				p.y >= corner1.y && p.y <= corner2.y &&
				p.z >= corner1.z && p.z <= corner2.z;
	}
	
	/**
	 * Check whether a box hits another box (i.e. they share at least a part of the 3D space.
	 * Sharing a common boundary also counts as a hit.
	 * 
	 * @param other The other box. 
	 * @return true if the boxes hit each other, false else.
	 */
	public boolean hits(Box other)
	{
		// as the boxes may not be rotated they hit each other if the x-, y-, and z-distances of their
		//  middle points are smaller than the respective dimensions/2 added 
		// The return statement always compares the distance of the middle points in each dimension (left side)
		//  with the half of the sum of the respective sizes in that dimension (right side). While this might look a bit better with
		//  some additional temporary variables it saves some calculation effort this way if x or y already don't match. 
		return Math.abs((corner1.x + corner2.x)/2-(other.corner1.x + other.corner2.x)/2) <= (corner2.x - corner1.x+other.corner2.x-other.corner1.x)/2 &&
				Math.abs((corner1.y + corner2.y)/2-(other.corner1.y + other.corner2.y)/2) <= (corner2.y - corner1.y+other.corner2.y-other.corner1.y)/2 &&
				Math.abs((corner1.z + corner2.z)/2-(other.corner1.z + other.corner2.z)/2) <= (corner2.z - corner1.z+other.corner2.z-other.corner1.z)/2;
	}
}
