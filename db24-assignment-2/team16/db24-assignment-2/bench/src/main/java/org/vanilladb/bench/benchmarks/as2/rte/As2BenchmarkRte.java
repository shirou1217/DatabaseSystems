/*******************************************************************************
 * Copyright 2016, 2018 vanilladb.org contributors
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
package org.vanilladb.bench.benchmarks.as2.rte;

import org.vanilladb.bench.VanillaBenchParameters;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.as2.As2BenchTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import java.util.Random;

public class As2BenchmarkRte extends RemoteTerminalEmulator<As2BenchTransactionType> {

	public As2BenchmarkRte(SutConnection conn, StatisticMgr statMgr, long sleepTime) {
		super(conn, statMgr, sleepTime);
	}

	// protected As2BenchTransactionType getNextTxType() {
	// 	// return As2BenchTransactionType.UPDATE_PRICE;
	// 	Random random = new Random();
	// 	// System.out.println("random:"+random+",
	// 	// rate:"+VanillaBenchParameters.READ_WRITE_TX_RATE+"\n");
	// 	return random.nextDouble() > VanillaBenchParameters.READ_WRITE_TX_RATE ? As2BenchTransactionType.READ_ITEM
	// 			: As2BenchTransactionType.UPDATE_PRICE;
	// }
	protected As2BenchTransactionType getNextTxType() {
		double readItemRatio = VanillaBenchParameters.READ_WRITE_TX_RATE;
	
		if (Math.random() < readItemRatio) {
			return As2BenchTransactionType.READ_ITEM;
		} else {
			return As2BenchTransactionType.UPDATE_PRICE;
		}
	}
	
	

	protected As2BenchmarkTxExecutor getTxExeutor(As2BenchTransactionType type) {
		switch (type) {
			case READ_ITEM:
				return new As2BenchmarkTxExecutor(new As2ReadItemParamGen());
			case UPDATE_PRICE:
				return new As2BenchmarkTxExecutor(new UpdatePriceParamGen());
			default:
				throw new IllegalArgumentException("Unsupported transaction type: " + type);
		}
	}
}
