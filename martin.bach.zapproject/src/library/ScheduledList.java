package library;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A scheduled ArrayList, which lets you decide about the moment of updating
 * 
 * @author Martin
 *
 * @param <Object>
 */
public class ScheduledList<E extends Object> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2996911937249525959L;

	private ArrayList<E> addList = new ArrayList<E>();
	private ArrayList<E> removeList = new ArrayList<E>();
	
	private boolean clearNext = false;

	/**
	 * Adds an object to the list Shifted-Support
	 * 
	 * @return Always True
	 */
	public boolean schedAdd(E e) {
		return addList.add(e);
	}

	/**
	 * Adds objects to the list: Shifted-Support
	 * 
	 * @return Always True
	 */
	public boolean schedAddAll(Collection<E> c) {
		return addList.addAll(c);
	}

	/**
	 * Removes an object from the list: Shifted-Support
	 * 
	 * @return Always True
	 */
	public boolean schedRemove(E o) {
		return removeList.add(o);
	}

	/**
	 * Removes objects from the list: Shifted-Support
	 * 
	 * @return Always True
	 */
	public boolean schedRemoveAll(Collection<E> c) {
		return removeList.addAll(c);
	}

	/**
	 * Updates the list: Shifted-Support
	 */
	public void update() {
		
		if (clearNext) {
			clear();
			clearNext = false;
		}
		
		if (!removeList.isEmpty()) {
			removeAll(removeList);
			removeList.clear();
		}

		if (!addList.isEmpty()) {
			addAll(addList);
			addList.clear();
		}
	}

	public ArrayList<E> getAddList() {
		return addList;
	}
	
	public void schedClear() {
		clearNext = true;
	}

	public ArrayList<E> getRemoveList() {
		return removeList;
	}

}
