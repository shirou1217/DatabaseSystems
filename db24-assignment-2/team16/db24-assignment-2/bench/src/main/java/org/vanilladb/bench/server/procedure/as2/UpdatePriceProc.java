package org.vanilladb.bench.server.procedure.as2;
import org.vanilladb.bench.benchmarks.as2.As2BenchConstants;
import org.vanilladb.bench.server.param.as2.UpdatePriceProcParamHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureHelper;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;

public class UpdatePriceProc extends StoredProcedure<UpdatePriceProcParamHelper> {

    public UpdatePriceProc() {
        super(new UpdatePriceProcParamHelper());
    }

    @Override
    protected void executeSql() {
        UpdatePriceProcParamHelper paramHelper = getParamHelper();
        Transaction tx = getTransaction();

        // UPDATE
        for (int idx = 0; idx < paramHelper.getUpdateCount(); idx++) {
            int iid = paramHelper.getUpdateItemId(idx);
            double raise = paramHelper.getPriceRaise(idx);
            // Select the current price
            Scan s = StoredProcedureHelper.executeQuery(
                "SELECT i_price FROM item WHERE i_id = " + iid,
                tx
            );
            s.beforeFirst();
            if (s.next()) {
                double currentPrice = (Double) s.getVal("i_price").asJavaVal();
                double newPrice = currentPrice + raise;
                
                // Check if the new price exceeds the maximum price
                if (newPrice > As2BenchConstants.MAX_PRICE)
                    newPrice = As2BenchConstants.MIN_PRICE;
                
                // Update the price
                int updateCount = StoredProcedureHelper.executeUpdate(
                    "UPDATE item SET i_price = " + newPrice + " WHERE i_id = " + iid,
                    tx
                );
                
                if (updateCount == 0)
                    throw new RuntimeException("Cannot update item record with i_id = " + iid);
            } else {
                throw new RuntimeException("Could not find item record with i_id = " + iid);
            }

            s.close();
        }
    }
}
