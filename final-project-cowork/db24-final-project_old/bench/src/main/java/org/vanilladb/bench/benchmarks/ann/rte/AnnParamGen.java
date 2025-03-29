package org.vanilladb.bench.benchmarks.ann.rte;

import java.util.ArrayList;

import org.vanilladb.bench.benchmarks.ann.AnnBenchConstants;
import org.vanilladb.bench.benchmarks.ann.AnnTransactionType;
import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.core.sql.VectorConstant;

public class AnnParamGen implements TxParamGenerator<AnnTransactionType> {

    private static final int NUM_QUERY = 10000;

    private VectorConstant[] queryPool = new VectorConstant[NUM_QUERY];
    private VectorConstant query;

    @Override
    public AnnTransactionType getTxnType() {
        return AnnTransactionType.ANN;
    }

    private int idx = 0;

    @Override
    public Object[] generateParameter() {
        ArrayList<Object> paramList = new ArrayList<>();

        // =====================
		// Generating Parameters
		// =====================
        paramList.add(AnnBenchConstants.NUM_DIMENSION);

        // Get a query vector from the query pool
        query = queryPool[idx];
        idx = (idx + 1) % NUM_QUERY;
        
        for (int i = 0; i < AnnBenchConstants.NUM_DIMENSION; i++) {
            paramList.add(query.get(i));
        }

        return paramList.toArray();
    }
    
    public VectorConstant getQuery() {
        return query;
    }
}
