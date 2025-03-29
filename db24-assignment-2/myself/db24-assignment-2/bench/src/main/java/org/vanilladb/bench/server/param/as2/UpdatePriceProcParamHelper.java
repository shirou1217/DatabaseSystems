package org.vanilladb.bench.server.param.as2;

import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

import java.util.Random;

public class UpdatePriceProcParamHelper implements StoredProcedureHelper {

	private static final int NUM_ITEMS = 10;
	private static final double MIN_RAISE = 0.0;
	private static final double MAX_RAISE = 5.0;

	private int[] itemIds;
	private double[] priceRaises;

	public int[] getItemIds() {
		return itemIds;
	}

	public double[] getPriceRaises() {
		return priceRaises;
	}

	@Override
	public void prepareParameters(Object... pars) {
		itemIds = generateRandomItemIds();
		priceRaises = generateRandomPriceRaises();
	}

	@Override
	public Schema getResultSetSchema() {
		return new Schema();
	}

	@Override
	public SpResultRecord newResultSetRecord() {
		return new SpResultRecord();
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	private int[] generateRandomItemIds() {
		Random random = new Random();
		int[] ids = new int[NUM_ITEMS];
		for (int i = 0; i < NUM_ITEMS; i++) {
			ids[i] = random.nextInt(100000); // Adjust according to your item id range
		}
		return ids;
	}

	private double[] generateRandomPriceRaises() {
		Random random = new Random();
		double[] raises = new double[NUM_ITEMS];
		for (int i = 0; i < NUM_ITEMS; i++) {
			raises[i] = MIN_RAISE + (MAX_RAISE - MIN_RAISE) * random.nextDouble();
		}
		return raises;
	}
}
