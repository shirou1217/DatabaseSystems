package org.vanilladb.bench.benchmarks.as2.rte;

import java.util.ArrayList;
import org.vanilladb.bench.benchmarks.as2.As2BenchConstants;
import org.vanilladb.bench.benchmarks.as2.As2BenchTransactionType;
import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.bench.util.RandomValueGenerator;

public class UpdatePriceParamGen implements TxParamGenerator<As2BenchTransactionType> {
    
    // Update Counts
    private static final int TOTAL_UPDATE_COUNT = 10; // 假设我们想要更新10个商品的价格
    
    @Override
    public As2BenchTransactionType getTxnType() {
        return As2BenchTransactionType.UPDATE_PRICE;
    }

    @Override
    public Object[] generateParameter() {
        RandomValueGenerator rvg = new RandomValueGenerator();
        ArrayList<Object> paramList = new ArrayList<Object>();
        
        // Set update count
        paramList.add(TOTAL_UPDATE_COUNT);
        
        // Generate item IDs and price raises
        for (int i = 0; i < TOTAL_UPDATE_COUNT; i++) {
            // Generate a random item ID to update
            paramList.add(rvg.number(1, As2BenchConstants.NUM_ITEMS));
            
            // Generate a random price raise value between 0.0 and 5.0
            paramList.add(rvg.fixedDecimalNumber(1, 0.0, 5.0));
        }

        return paramList.toArray(new Object[0]);
    }
}
