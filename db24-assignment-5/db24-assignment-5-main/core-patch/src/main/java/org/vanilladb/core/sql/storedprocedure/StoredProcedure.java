/*******************************************************************************
 * Copyright 2016, 2017 vanilladb.org contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.vanilladb.core.sql.storedprocedure;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.core.query.planner.BadSemanticException;
import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.storage.tx.PrimaryKey;
import org.vanilladb.core.storage.tx.Transaction;
import org.vanilladb.core.storage.tx.concurrency.LockAbortException;
import org.vanilladb.core.storage.tx.concurrency.conservative.ConservativeConcurrencyMgr;

/**
 * An abstract class that denotes the stored procedure supported in VanillaDb.
 */
public abstract class StoredProcedure<H extends StoredProcedureParamHelper> {
	private static Logger logger = Logger.getLogger(StoredProcedure.class
			.getName());
	private static final ReentrantLock serial_lock = new ReentrantLock();

	private H paramHelper;
	private Transaction tx;
	// as5
	protected Set<PrimaryKey> readSet = new HashSet<PrimaryKey>();
	protected Set<PrimaryKey> writeSet = new HashSet<PrimaryKey>();

	protected abstract void prepareRWSet();

	public StoredProcedure(H helper) {
		if (helper == null)
			throw new IllegalArgumentException("paramHelper should not be null");

		paramHelper = helper;
	}

	public void prepare(Object... pars) {
		// prepare parameters
		paramHelper.prepareParameters(pars);

		// as5
		prepareRWSet();

		// create a transaction
		boolean isReadOnly = paramHelper.isReadOnly();
		serial_lock.lock();
		try {
			tx = VanillaDb.txMgr().newTransaction(
					Connection.TRANSACTION_SERIALIZABLE, isReadOnly);
			ConservativeConcurrencyMgr ccMgr = (ConservativeConcurrencyMgr) tx.concurrencyMgr();
			ccMgr.bookPrimaryKeys(writeSet, readSet);
		} finally {
			serial_lock.unlock();
		}
	}

	public SpResultSet execute() {
		boolean isCommitted = false;

		try {
			// as5
			ConservativeConcurrencyMgr ccMgr = (ConservativeConcurrencyMgr) tx.concurrencyMgr();
			ccMgr.acquireAllLock();

			executeSql();

			// The transaction finishes normally
			tx.commit();
			isCommitted = true;

		} catch (LockAbortException lockAbortEx) {
			if (logger.isLoggable(Level.WARNING))
				logger.warning(lockAbortEx.getMessage());
			tx.rollback();
		} catch (ManuallyAbortException me) {
			if (logger.isLoggable(Level.WARNING))
				logger.warning("Manually aborted by the procedure: " + me.getMessage());
			tx.rollback();
		} catch (BadSemanticException be) {
			if (logger.isLoggable(Level.SEVERE))
				logger.warning("Semantic error: " + be.getMessage());
			tx.rollback();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}

		return new SpResultSet(
				isCommitted,
				paramHelper.getResultSetSchema(),
				paramHelper.newResultSetRecord());
	}

	protected abstract void executeSql();

	protected H getParamHelper() {
		return paramHelper;
	}

	protected Transaction getTransaction() {
		return tx;
	}

	protected void abort() {
		throw new ManuallyAbortException();
	}

	protected void abort(String message) {
		throw new ManuallyAbortException(message);
	}
}