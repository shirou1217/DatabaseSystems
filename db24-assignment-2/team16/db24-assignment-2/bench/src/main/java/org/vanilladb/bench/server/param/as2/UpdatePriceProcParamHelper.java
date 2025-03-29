package org.vanilladb.bench.server.param.as2;

import org.vanilladb.core.sql.IntegerConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class UpdatePriceProcParamHelper implements StoredProcedureHelper {

    private int updateCount;
    private int[] updateItemId;
    private double[] priceRaise;

    public int getUpdateCount() {
        return updateCount;
    }

    public int getUpdateItemId(int index) {
        return updateItemId[index];
    }

    public double getPriceRaise(int index) {
        return priceRaise[index];
    }

    @Override
    public void prepareParameters(Object... pars) {
        int indexCnt = 0;

        updateCount = (Integer) pars[indexCnt++];
        updateItemId = new int[updateCount];
        priceRaise = new double[updateCount];

        for (int i = 0; i < updateCount; i++) {
            updateItemId[i] = (Integer) pars[indexCnt++];
            priceRaise[i] = (Double) pars[indexCnt++];
        }
    }

    @Override
    public Schema getResultSetSchema() {
        Schema sch = new Schema();
        sch.addField("update_count", Type.INTEGER);
        return sch;
    }

    @Override
    public SpResultRecord newResultSetRecord() {
        SpResultRecord rec = new SpResultRecord();
        rec.setVal("update_count", new IntegerConstant(updateCount));
        return rec;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
