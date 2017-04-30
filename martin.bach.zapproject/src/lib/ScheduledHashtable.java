package lib;

import java.util.ArrayList;
import java.util.Hashtable;

public class ScheduledHashtable<Key extends Object, Value extends Object> extends Hashtable<Key, Value> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final ArrayList<Key> removeList = new ArrayList<Key>();
	private final Hashtable<Key, Value> addTable = new Hashtable<Key, Value>();

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

		if (!addTable.keySet().isEmpty()) {
			for (Key k : addTable.keySet()) {
				this.put(k, addTable.get(k));
			}
			addTable.clear();
		}

	}

	/**
	 * Registers the entry for the adding-process
	 * 
	 * @param key
	 * @param value
	 */
	public void schedPut(Key key, Value value) {
		addTable.put(key, value);
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
