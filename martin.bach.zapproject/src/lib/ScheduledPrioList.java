package lib;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A scheduled ArrayList, which lets you decide about the moment of updating.
 * !All sched-Added Objects will be set to the index 0!
 * 
 * @author Martin
 *
 * @param <Object>
 */
public class ScheduledPrioList<E extends Object> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2996911937249525959L;

	private ArrayList<E> addList = new ArrayList<E>();
	private ArrayList<E> removeList = new ArrayList<E>();

	/**
	 * Adds an object to the list Shifted-Support
	 * 
	 * @return Always True
	 */
	public boolean schedAdd(E e) {
		addList.add(0, e);
		return true;
	}

	/**
	 * Adds objects to the list: Shifted-Support
	 * 
	 * @return Always True
	 */
	public boolean schedAddAll(Collection<E> c) {
		for (E e : c) {
			addList.add(0, e);
		}
		return true;
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
		if (!removeList.isEmpty()) {
			removeAll(removeList);
			removeList.clear();
		}

		if (!addList.isEmpty()) {
			for (E e : addList) {
				add(0, e);
			}
			addList.clear();
		}
	}

}
