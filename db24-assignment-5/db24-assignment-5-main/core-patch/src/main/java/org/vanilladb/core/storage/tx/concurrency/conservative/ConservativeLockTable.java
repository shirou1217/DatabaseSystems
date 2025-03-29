package org.vanilladb.core.storage.tx.concurrency.conservative;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.vanilladb.core.storage.tx.PrimaryKey;
import org.vanilladb.core.storage.tx.concurrency.LockAbortException;

public class ConservativeLockTable {

	private class Lockers {
		public Set<Long> slockers;
		public long xlocker;
		public Queue<Long> requestQueue;

		Lockers() {
			slockers = new HashSet<Long>();
			xlocker = -1;
			requestQueue = new LinkedList<Long>();
		}

		@Override
		public String toString() {
			return "SLockers: " + slockers + ", XLockers" + xlocker;
		}
	}

	private Map<Object, Lockers> lockerMap = new ConcurrentHashMap<Object, Lockers>();
	private final Object anchors[] = new Object[1009];

	public ConservativeLockTable() {
		for (int i = 0; i < anchors.length; ++i) {
			anchors[i] = new Object();
		}
	}

	public void bookLock(PrimaryKey p, long txNum) {
		synchronized (getAnchor(p)) {
			Lockers lockers = prepareLockers(p);
			lockers.requestQueue.add(txNum);
		}
	}

	public void getXLock(Object p, long txNum) {
		synchronized (getAnchor(p)) {
			Lockers lockers = prepareLockers(p);
			if (lockers.xlocker == txNum)
				lockers.requestQueue.remove(txNum);
			try {
				while ((lockers.requestQueue.peek() != null && lockers.requestQueue.peek().longValue() != txNum)
						|| lockers.xlocker != -1 || (lockers.slockers.size() == 1 && !lockers.slockers.contains(txNum))
						|| lockers.slockers.size() > 1) {
					getAnchor(p).wait();
					lockers = prepareLockers(p);
				}
				lockers.requestQueue.poll();
				lockers.xlocker = txNum;
			} catch (Exception e) {
				throw new LockAbortException();
			}
		}
	}

	public void getSLock(Object p, long txNum) {
		Object anchor = getAnchor(p);
		synchronized (anchor) {
			Lockers lockers = prepareLockers(p);
			if (lockers.slockers.contains(txNum))
				lockers.requestQueue.remove(txNum);
			try {
				while ((lockers.requestQueue.peek() != null && lockers.requestQueue.peek().longValue() != txNum)
						|| (lockers.xlocker != -1 && lockers.xlocker != txNum)) {
					getAnchor(p).wait();
					lockers = prepareLockers(p);
				}

				lockers.requestQueue.poll();
				lockers.slockers.add(txNum);

				anchor.notifyAll();
			} catch (Exception e) {
				throw new LockAbortException();
			}
		}
	}

	public void releaseAll(long txNum, Set<PrimaryKey> bookKeys) {
		for (Object p : bookKeys) {
			Object anchor = getAnchor(p);
			synchronized (anchor) {
				Lockers lockers = lockerMap.get(p);
				if (lockers == null)
					continue;
				releaseLock(anchor, lockers, txNum);
				if (lockers.slockers.size() == 0 && lockers.xlocker == -1 && lockers.requestQueue.size() == 0)
					lockerMap.remove(lockers);
				anchor.notifyAll();
			}
		}
	}

	private void releaseLock(Object anchor, Lockers lockers, long txNum) {
		if (lockers.xlocker == txNum) {
			lockers.xlocker = -1;
			anchor.notifyAll();
		}
		if (lockers.slockers.contains(txNum)) {
			lockers.slockers.remove(txNum);
			if (lockers.slockers.size() == 0)
				anchor.notifyAll();
		}
	}

	private Lockers prepareLockers(Object obj) {
		Lockers lockers = lockerMap.get(obj);
		if (lockers == null) {
			lockers = new Lockers();
			lockerMap.put(obj, lockers);
		}
		return lockers;
	}

	private Object getAnchor(Object o) {
		int code = o.hashCode() % anchors.length;
		if (code < 0) {
			code += anchors.length;
		}
		return anchors[code];
	}

}
