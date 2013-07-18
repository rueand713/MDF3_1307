
package libs;

import java.io.Serializable;
import java.util.ArrayList;

public class UniArray implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> OBJECT_KEYS = null;
	private ArrayList<Object> OBJECT_PAIRS = null;
	
	private ArrayList<String> STRING_KEYS = null;
	private ArrayList<String> STRING_PAIRS = null;
	
	// constructor
	public UniArray(){
		
		OBJECT_KEYS = new ArrayList<String>();
		OBJECT_PAIRS = new ArrayList<Object>();
		STRING_KEYS = new ArrayList<String>();
		STRING_PAIRS = new ArrayList<String>();
	}
	
	// method for returning all objects in array
	public Object[] getAllObjects()
	{
		return OBJECT_PAIRS.toArray();
	}
	
	// method for returning all strings in array
	public String[] getAllStrings()
	{
		// integer for holding the array size count
		int count = STRING_PAIRS.size();
		
		// string array for returning object strings
		String[] strings = new String[count];
		
		// iterate through the array list extracting the strings
		for (int i=0; i < count; i++)
		{
			// set the string at the current index of i to the string array
			strings[i] = STRING_PAIRS.get(i);
		}
		
		return strings;
	}
	
	// method for returning all object keys
	public String[] getAllObjectKeys()
	{
		// integer for holding the array size count
		int count = OBJECT_KEYS.size();
		
		// string array for returning object strings
		String[] obKeys = new String[count];
		
		// iterate through the array list extracting the strings
		for (int i=0; i < count; i++)
		{
			// set the string at the current index of i to the string array
			obKeys[i] = OBJECT_KEYS.get(i);
		}
		
		return obKeys;
	}
	
	// method for returning all string keys
	public String[] getAllStringKeys()
	{
		// integer for holding the array size count
		int count = STRING_KEYS.size();
		
		// string array for returning object strings
		String[] stKeys = new String[count];
		
		// iterate through the array list extracting the strings
		for (int i=0; i < count; i++)
		{
			// set the string at the current index of i to the string array
			stKeys[i] = STRING_KEYS.get(i);
		}
		
		return stKeys;
	}
	
	// method for returning the number of strings in the array
	public int stringsLength()
	{	
		return STRING_KEYS.size();
	}
	
	// method for returning the number of objects in the array
	public int objectsLength()
	{	
		return OBJECT_KEYS.size();
	}
	
	// method for returning the key string at the index specified
	public String keyForString(int index)
	{
		// create the value string and set it to default null
		String value = null;
		
		// verify that the array is not null
		if (STRING_KEYS != null)
		{
			// check that the index is in range
			if (index >= 0 && STRING_KEYS.size() > index)
			{
				// set the string to the value in the array at the specified index
				value = STRING_KEYS.get(index);
			}
		}
		
		return value;
	}
	
	// method for returning the key string at the index specified
	public String keyForObject(int index)
	{
		// create the value string and set it to default null
		String value = null;
		
		// verify that the array is not null
		if (OBJECT_KEYS != null)
		{
			// check that the index is in range
			if (index >= 0 && OBJECT_KEYS.size() > index)
			{
				// set the string to the value in the array at the specified index
				value = OBJECT_KEYS.get(index);
			}
		}
		
		return value;
	}
	
	// method for returning the index for the array; returns -1 if the key was not found
	public int indexForObject(String key)
	{
		// create the index int and set it to -1 for default numeric false
		int index = -1;
		
		if (hasObject(key))
		{
			// create an int for the the index counter
			int counter = 0;
			
			// iterate over each string value comparing n to key
			for (String n : OBJECT_KEYS)
			{
				// check if the key exists in the array
				if (n.equals(key))
				{
					// set the index to the current counter
					index = counter;
					
					break;
				}
				
				// increment the counter
				counter++;
			}
		}
		
		return index;
	}
	
	// method for returning the index for the array; returns -1 if the key was not found
	public int indexForString(String key)
	{
		// create the index int and set it to -1 for default numeric false
		int index = -1;
		
		if (hasString(key))
		{
			// create an int for the the index counter
			int counter = 0;
			
			// iterate over each string value comparing n to key
			for (String n : STRING_KEYS)
			{
				// check if the key exists in the array
				if (n.equals(key))
				{
					// set the index to the current counter
					index = counter;
					
					break;
				}
				
				// increment the counter
				counter++;
			}
		}
		
		return index;
	}
	
	// method for checking that the object key exists or not
	public Boolean hasObject(String key)
	{
		// create the return boolean
		// init it to false as default
		Boolean result = false;
		
		// ensure that the array is not null
		if (OBJECT_KEYS != null)
		{
			// iterate over each string value comparing n to key
			for (String n : OBJECT_KEYS)
			{
				// check if the key exists in the array
				if (n.equals(key) == true)
				{
					// set the return value to true
					result = true;
					
					break;
				}
			}
		}
		
		return result;
	}
	
	// method for checking that the string key exists or not
	public Boolean hasString(String key)
	{
		// create the return boolean
		// init it to false as default
		Boolean result = false;
		
		// ensure that the array is not null
		if (STRING_KEYS != null)
		{
			// iterate over each string value comparing n to key
			for (String n : STRING_KEYS)
			{
				// check if the key exists in the array
				if (n.equals(key) == true)
				{
					// set the return value to true
					result = true;
					
					break;
				}
			}
		}
		
		return result;
	}
	
	// method for adding new strings to the UniArray object class
	public void putString(String key, String value)
	{
		// verify if this key exists or not
		if (hasString(key))
		{
			// get the index for the existing key
			int index = indexForString(key);
			
			// set the object to the index for the key
			STRING_PAIRS.set(index, value);
		}
		else
		{	
			STRING_KEYS.add(key);
			STRING_PAIRS.add(value);
		}
	}
	
	// method for adding new objects to the UniArray object class
	public void putObject(String key, Object value)
	{
		// verify if this key exists or not
		if (hasObject(key))
		{
			// get the index for the existing key
			int index = indexForObject(key);
			
			// set the object to the index for the key
			OBJECT_PAIRS.set(index, value);
			
		}
		else
		{
			
			OBJECT_KEYS.add(key);
			OBJECT_PAIRS.add(value);
		}
	}
	
	// method for getting and returning the string value of the key
	public String getString(String key)
	{
		// create the return string and init it to null as default
		String value = null;
		
		// verify that the string key does exist
		if (hasString(key))
		{
			// create an int for the the index counter
			int counter = 0;
			
			// iterate through the array of strings comparing n to the key
			for (String n : STRING_KEYS)
			{
				// check that  n equals the key
				if (n.equals(key))
				{
					// set the string VALUE to n
					value = STRING_PAIRS.get(counter);
					
					break;
				}
				
				// increment the counter
				counter++;
			}
		}
		
		return value;
	}
	
	// method for getting and returning the object value of the key
	public Object getObject(String key)
	{
		// create the return object and init it to null as default
		Object value = null;
		
		// verify that the string key does exist
		if (hasObject(key))
		{
			// create an int for the the index counter
			int counter = 0;
			
			// iterate through the array of strings comparing n to the key
			for (String n : OBJECT_KEYS)
			{
				// check that  n equals the key
				if (n.equals(key))
				{
					// set the string VALUE to n
					value = OBJECT_PAIRS.get(counter);
					
					break;
				}
				
				// increment the counter
				counter++;
			}
		}
		
		return value;
	}
	
	// method for getting and returning the string value of the key
	public String getString(int index)
	{
		// create the return string and init it to null as default
		String value = null;
		
		// verify that the array is not null and the index is in range
		if (STRING_KEYS != null && index >= 0)
		{
			// set the string to the value in the array at the specified index
			value = STRING_PAIRS.get(index);
		}
		
		return value;
	}
	
	// method for getting and returning the string value of the key
	public Object getObject(int index)
	{
		// create the return object and init it to null as default
		Object value = null;
		
		// verify that the array is not null and the index is in range
		if (OBJECT_KEYS != null && index >= 0)
		{
			// set the string to the value in the array at the specified index
			value = OBJECT_PAIRS.get(index);
		}
		
		return value;
	}
	
	// method for removing an object key/pair using an index value
	public void removeObject(int index)
	{
		// verify that the array is not null and the index is in range
		if (OBJECT_KEYS != null && index >= 0)
		{
			// remove the object at the current index
			OBJECT_KEYS.remove(index);
			OBJECT_PAIRS.remove(index);
		}
	}
	
	// method for removing an object key/pair using a string key value
	public void removeObject(String key)
	{
		// verify that the array is not null and the index is in range
		if (OBJECT_KEYS != null && hasObject(key))
		{
			// get the index for the key
			int index = indexForObject(key);
			
			// remove the object at the current index
			OBJECT_KEYS.remove(index);
			OBJECT_PAIRS.remove(index);
		}
	}
	
	// method for removing a string key/pair using an index value
	public void removeString(int index)
	{
		// verify that the array is not null and the index is in range
		if (STRING_KEYS != null && index >= 0)
		{
			// remove the string at the current index
			STRING_KEYS.remove(index);
			STRING_PAIRS.remove(index);
		}
	}
	
	// method for removing a string key/pair using an string key value
	public void removeString(String key)
	{
		// verify that the array is not null and the index is in range
		if (STRING_KEYS != null && hasString(key))
		{
			// get the index for the key
			int index = indexForString(key);
			
			// remove the object at the current index
			STRING_KEYS.remove(index);
			STRING_PAIRS.remove(index);
		}
	}
}
