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
		UpdatePriceProcParamHelper paramGen = getParamHelper();
		Transaction tx = getTransaction();
		 // Generate random parameters
        paramGen.prepareParameters();   

		// Get randomly generated item ids and price raises
		int[] itemIds = paramGen.getItemIds();
		double[] priceRaises = paramGen.getPriceRaises();
		
		for (int i = 0; i < itemIds.length; i++) {
			int itemId = itemIds[i];
			double priceRaise = priceRaises[i];
			
			// SELECT name and price of the item
			Scan scan = StoredProcedureHelper.executeQuery(
				"SELECT i_name, i_price FROM item WHERE i_id = " + itemId,
				tx
			);
			scan.beforeFirst();
			
			if (scan.next()) {
				String itemName = (String) scan.getVal("i_name").asJavaVal();
				double originalPrice = (Double) scan.getVal("i_price").asJavaVal();
				double newPrice = originalPrice + priceRaise;
				
				// Check if the new price exceeds the maximum price
				if (newPrice > As2BenchConstants.MAX_PRICE) {
					newPrice = As2BenchConstants.MIN_PRICE;
				}
				
				// Update the item price
				StoredProcedureHelper.executeUpdate(
					"UPDATE item SET i_price = " + newPrice + " WHERE i_id = " + itemId,
					tx
				);
				
				// Log the changes
				System.out.println("Item " + itemName + " price updated from " + originalPrice + " to " + newPrice);
			} else {
				throw new RuntimeException("Could not find item record with i_id = " + itemId);
			}

			scan.close();
		}
	}
}
