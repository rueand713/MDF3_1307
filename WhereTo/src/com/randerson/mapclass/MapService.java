/*
 * project 		WhereTo
 * 
 * package 		com.randerson.mapclass
 * 
 * @author 		Rueben Anderson
 * 
 * date			Jul 11, 2013
 * 
 */
package com.randerson.mapclass;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapService {

	// map type value constants
	public static final int TERRAIN = GoogleMap.MAP_TYPE_TERRAIN;
	public static final int HYBRID = GoogleMap.MAP_TYPE_TERRAIN;
	public static final int NORMAL = GoogleMap.MAP_TYPE_NORMAL;
	public static final int SATELLITE = GoogleMap.MAP_TYPE_SATELLITE;
	public static final int NONE = GoogleMap.MAP_TYPE_NONE;
	
	// method for creating and returing a google map options object
	// for passing into the 'setFragmentOptions' method
	public static GoogleMapOptions createOptions(boolean hasCompass, boolean canRotate, boolean canTilt, int mapType)
	{ 
		GoogleMapOptions options = new GoogleMapOptions();
		
		// set the map options parameters from the passed in arguments
			options.compassEnabled(hasCompass);
			options.rotateGesturesEnabled(canRotate);
			options.tiltGesturesEnabled(canTilt);
			options.mapType(mapType);
			
		return options;
	}
	
	// method for setting the map fragment initial options
	public static MapFragment setFragmentOptions(GoogleMapOptions options)
	{
		// creates a new map fragment with the passed in google map options object
		MapFragment mapFragment = MapFragment.newInstance(options);
		
		return mapFragment;
	}
	
	public static CameraPosition setCamera(LatLng latLon, float bearing, float tilt, float zoom)
	{
		// create a camera position object
		CameraPosition cPos = new CameraPosition.Builder().target(latLon).bearing(bearing).tilt(tilt).zoom(zoom).build();
			
		return cPos;
	}
	
	public static void updatePosition(GoogleMap map, double lat, double lon)
	{
		// create the latitude longitude object from the passed in args
		LatLng coords = makeCoords(lat, lon);
	
		// reposition the camera on the map with no zoom
		map.moveCamera(CameraUpdateFactory.newLatLng(coords));
	}
	
	public static void updatePosition(GoogleMap map, double lat, double lon, float zoom)
	{
		// create the latitude longitude object from the passed in args
		LatLng coords = new LatLng(lat, lon);
	
		// reposition the camera on the map with zoom
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, zoom));
	}
	
	public static void zoomMap(GoogleMap map, float zoom)
	{
		// zoom the map
		map.moveCamera(CameraUpdateFactory.zoomTo(zoom));
	}
	
	public static LatLng makeCoords(double lat, double lon)
	{
		// return the lat lon object
		return new LatLng(lat, lon);
	}
	
	private static MarkerOptions setMarkerOptions(String title, String note, double lat, double lon)
	{
		// make the coords object
		LatLng coords = makeCoords(lat, lon);
		
		// set the marker options
		MarkerOptions options = new MarkerOptions();
			options.title(title).position(coords).snippet(note);
		
		return options;
	}
	
	public static Marker addMarker(GoogleMap map, String title, String note, double lat, double lon)
	{
		// adds a marker to the passed in map object
		Marker mark = map.addMarker(setMarkerOptions(title, note, lat, lon));
		
		return mark;
	}
}
