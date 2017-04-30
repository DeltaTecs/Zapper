package lib;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduledHashMap<Key extends Object, Value extends Object> extends HashMap<Key, Value> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final ArrayList<Key> removeList = new ArrayList<Key>();
	private final HashMap<Key, Value> addMap = new HashMap<Key, Value>();

	/**
	 * Updates the Map
	 */
	public void update() {
		if (!removeList.isEmpty()) {
			for (Key k : removeList) {
				this.remove(k);
			}
			removeList.clear();
		}

		if (!addMap.isEmpty()) {
			for (Key k : addMap.keySet()) {
				this.put(k, addMap.get(k));
			}
			addMap.clear();
		}

	}

	/**
	 * Registers the entry for the adding-process
	 * 
	 * @param key
	 * @param value
	 */
	public void schedPut(Key key, Value value) {
		addMap.put(key, value);
	}

	/**
	 * Registers the key for the removeing-process
	 * 
	 * @param key
	 */
	public void schedRemove(Key key) {
		removeList.add(key);
	}

	/**
	 * Registers an Array of keys for the removeing-process
	 * 
	 * @param keys
	 */
	public void schedRemoveAll(Key[] keys) {
		for (Key k : keys) {
			removeList.add(k);
		}
	}

	/**
	 * Registers a List of keys for the removeing-process
	 * 
	 * @param keys
	 */
	public void schedRemoveAll(ArrayList<Key> keys) {
		removeList.addAll(keys);
	}

}
