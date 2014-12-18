package topology;

public class Location {
	
	private double latitude;
	private double longitude;
	
	/**
	 * Create a geographical location object using geographical latitude
	 * and longitude coordinate information.
	 * 
	 * @param latitude The latitude value.
	 * @param longitude The longitude value.
	 */
	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * Retrieve the latitude value from this Location.
	 * 
	 * @return latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * Retrieve the longitude value from this Location.
	 * 
	 * @return longitude
	 */
	public double getLongitude() {
		return longitude;
	}
}
