package lib;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A scheduled ArrayList, which lets you decide about the moment of updating.
 * This type is more presicse. It makes sure that the add/remove-process is not
 * compromised by schedAdds/Removes during itself. There might be performance
 * lacking at this point.
 * 
 * @author Martin
 *
 * @param <Object>
 */
public class SynchronisedScheduledList<E extends Object> extends ArrayList<E> {

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
		synchronized (addList) {
			return addList.add(e);
		}
	}

	/**
	 * Adds objects to the list: Shifted-Support
	 * 
	 * @return Always True
	 */
	public boolean schedAddAll(Collection<E> c) {
		synchronized (addList) {
			return addList.addAll(c);
		}
	}

	/**
	 * Removes an object from the list: Shifted-Support
	 * 
	 * @return Always True
	 */
	public boolean schedRemove(E o) {
		synchronized (removeList) {
			return removeList.add(o);
		}
	}

	/**
	 * Removes objects from the list: Shifted-Support
	 * 
	 * @return Always True
	 */
	public boolean schedRemoveAll(Collection<E> c) {
		synchronized (removeList) {
			return removeList.addAll(c);
		}
	}

	/**
	 * Updates the list: Shifted-Support
	 */
	public void update() {

		if (clearNext) {
			clear();
			clearNext = false;
		}
		
		synchronized (addList) {
		if (!addList.isEmpty()) {

				addAll(addList);
				addList.clear();
			}
		}

		synchronized (removeList) {
		if (!removeList.isEmpty()) {
				removeAll(removeList);
				removeList.clear();
			}
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
