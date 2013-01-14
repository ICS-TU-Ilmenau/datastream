package de.tuilmenau.ics.CommonSim.datastream.occurrences.types;

/**
 * Position data object. This object represents the position of
 * an object in the simulation space. It is the base for all mobility and
 * radio models.
 * 
 * @author Markus Brueckner
 */
public class Position {
	
	public final double x;
	public final double y;
	public final double z;
	
	/**
	 * initialize a position object located at (0,0,0)
	 */
	public Position() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	/**
	 * initialize a position object located at (xx,yy,zz)
	 */
	public Position(double xx, double yy, double zz) {
		x = xx;
		y = yy;
		z = zz;
	}

	/**
	 * Calculate the distance between two positions.
	 * 
	 * @param other The other position to which the distance is to be calculated.
	 * @return The distance between the two points in meters.
	 */
	public double distance(Position other) {
		// TODO: rethink scenarios with "warp-around" coordinates like on a sphere
		return Math.sqrt(Math.pow(other.x-x, 2)+Math.pow(other.y-y,2)+Math.pow(other.z-z,2));
	}
	// TODO complete the interface
	
	@Override
	public String toString() {
		return "("+Double.toString(x)+","+Double.toString(y)+","+Double.toString(z)+")";
	}
}
