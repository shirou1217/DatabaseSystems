package org.vanilladb.core.storage.tx.concurrency.conservative;

import java.util.HashSet;
import java.util.Set;

import org.vanilladb.core.storage.file.BlockId;
import org.vanilladb.core.storage.record.RecordId;
import org.vanilladb.core.storage.tx.PrimaryKey;
import org.vanilladb.core.storage.tx.Transaction;
import org.vanilladb.core.storage.tx.concurrency.ConcurrencyMgr;

public class ConservativeConcurrencyMgr extends ConcurrencyMgr {
	private static ConservativeLockTable conLockTbl = new ConservativeLockTable();

	private Set<PrimaryKey> bookKeys, readSet, writeSet;

	public ConservativeConcurrencyMgr(long txNumber) {
		txNum = txNumber;
		bookKeys = new HashSet<PrimaryKey>();
		readSet = new HashSet<PrimaryKey>();
		writeSet = new HashSet<PrimaryKey>();
	}

	public void bookPrimaryKeys(Set<PrimaryKey> writeSet, Set<PrimaryKey> readSet) {
		for (PrimaryKey p : writeSet) {
			conLockTbl.bookLock(p, txNum);
			bookKeys.add(p);
			writeSet.add(p);
		}

		for (PrimaryKey p : readSet) {
			if (!bookKeys.contains(p))
				conLockTbl.bookLock(p, txNum);
			bookKeys.add(p);
			readSet.add(p);
		}

	}

	public void acquireAllLock() {
		for (PrimaryKey p : writeSet) {
			conLockTbl.getXLock(p, txNum);
		}
		for (PrimaryKey p : readSet) {
			if (!writeSet.contains(p))
				conLockTbl.getSLock(p, txNum);
		}
	}

	@Override
	public void onTxCommit(Transaction tx) {
		conLockTbl.releaseAll(txNum, bookKeys);
		readSet.clear();
		writeSet.clear();
		bookKeys.clear();
	}

	@Override
	public void onTxRollback(Transaction tx) {
		conLockTbl.releaseAll(txNum, bookKeys);
		readSet.clear();
		writeSet.clear();
		bookKeys.clear();
	}

	@Override
	public void onTxEndStatement(Transaction tx) {
	}

	@Override
	public void modifyFile(String fileName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readFile(String fileName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertBlock(BlockId blk) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyBlock(BlockId blk) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readBlock(BlockId blk) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyRecord(RecordId recId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readRecord(RecordId recId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyIndex(String dataFileName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readIndex(String dataFileName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyLeafBlock(BlockId blk) {
	}

	@Override
	public void readLeafBlock(BlockId blk) {
	}

}
