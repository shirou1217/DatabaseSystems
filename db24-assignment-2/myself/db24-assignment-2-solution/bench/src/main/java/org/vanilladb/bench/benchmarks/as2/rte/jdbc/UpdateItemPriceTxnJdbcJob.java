package org.vanilladb.bench.benchmarks.as2.rte.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.remote.jdbc.VanillaDbJdbcResultSet;
import org.vanilladb.bench.rte.jdbc.JdbcJob;
import org.vanilladb.bench.benchmarks.as2.As2BenchConstants;
import org.vanilladb.bench.benchmarks.as2.rte.UpdateItemPriceTxnParam;

public class UpdateItemPriceTxnJdbcJob implements JdbcJob {
	private static Logger logger = Logger.getLogger(ReadItemTxnJdbcJob.class.getName());

	@Override
	public SutResultSet execute(Connection conn, Object[] pars) throws SQLException {
		// Parse parameters
		int readCount = (Integer) pars[0];
		int[] itemIds = new int[readCount];
		double[] raises = new double[readCount];

		for (int i = 0; i < readCount; i++) {
			itemIds[i] = (Integer) (((UpdateItemPriceTxnParam) pars[i + 1]).itemId);
			raises[i] = (Double) (((UpdateItemPriceTxnParam) pars[i + 1]).raise);
		}

		// Output message
		StringBuilder outputMsg = new StringBuilder("[");

		// Execute logic
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = null;

			for (int i = 0; i < 10; i++) {
				double price;

				String sql = "SELECT i_name, i_price FROM item WHERE i_id = " + itemIds[i];
				rs = statement.executeQuery(sql);
				rs.beforeFirst();
				if (rs.next()) {
					outputMsg.append(String.format("'%s', ", rs.getString("i_name")));
					price = rs.getDouble("i_price");
				} else
					throw new RuntimeException("cannot find the record with i_id = " + itemIds[i]);
				rs.close();

				Double updatedPrice = updatePrice(price, raises[i]);
				sql = "UPDATE item SET i_price = " + updatedPrice + " WHERE i_id = " + itemIds[i];

				int result = statement.executeUpdate(sql);
				if (result == 0) {
					throw new RuntimeException("cannot update the record with i_id = " + itemIds[i]);
				}

			}
			conn.commit();

			outputMsg.deleteCharAt(outputMsg.length() - 2);
			outputMsg.append("]");

			return new VanillaDbJdbcResultSet(true, outputMsg.toString());
		} catch (Exception e) {
			if (logger.isLoggable(Level.WARNING))
				logger.warning(e.toString());
			return new VanillaDbJdbcResultSet(false, "");
		}
	}

	private Double updatePrice(double originalPrice, double raise) {
		return (Double) (originalPrice > As2BenchConstants.MAX_PRICE ? As2BenchConstants.MIN_PRICE : originalPrice + raise);
	}
}
