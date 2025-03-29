package org.vanilladb.bench.benchmarks.as2.rte.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vanilladb.bench.benchmarks.as2.As2BenchConstants;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.remote.jdbc.VanillaDbJdbcResultSet;
import org.vanilladb.bench.rte.jdbc.JdbcJob;
import org.vanilladb.bench.server.param.as2.UpdatePriceProcParamHelper;

public class UpdatePriceJdbcJob implements JdbcJob {
    private static Logger logger = Logger.getLogger(UpdatePriceJdbcJob.class.getName());

    @Override
    public SutResultSet execute(Connection conn, Object[] pars) throws SQLException {
        UpdatePriceProcParamHelper paramHelper = new UpdatePriceProcParamHelper();
        paramHelper.prepareParameters(pars);

        Statement statement = null;
        ResultSet rs = null;
        
        try {
            statement = conn.createStatement();

            for (int i = 0; i < paramHelper.getUpdateCount(); i++) {
                int iid = paramHelper.getUpdateItemId(i);
                double raise = paramHelper.getPriceRaise(i);

                // Select the current price
                String sql = "SELECT i_price FROM item WHERE i_id = " + iid;
                rs = statement.executeQuery(sql);
                rs.beforeFirst();
                if (rs.next()) {
                    double currentPrice = rs.getDouble("i_price");
                    double newPrice = currentPrice + raise;

                    if (newPrice > As2BenchConstants.MAX_PRICE) {
                        newPrice = As2BenchConstants.MIN_PRICE;
                    }

                    // Update the price
                    sql = "UPDATE item SET i_price = " + newPrice + " WHERE i_id = " + iid;
                    int updateCount = statement.executeUpdate(sql);
                    
                    if (updateCount == 0) {
                        throw new RuntimeException("Cannot update item record with i_id = " + iid);
                    }
                } else {
                    throw new RuntimeException("Could not find item record with i_id = " + iid);
                }
                rs.close();
                
            }
            conn.commit();
            return new VanillaDbJdbcResultSet(true, "UpdatePriceTxn executed successfully");
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING))
                logger.warning(e.toString());
            try {
                conn.rollback();
            } catch (SQLException e2) {
                logger.log(Level.SEVERE, "Could not roll back the connection.", e2);
            }
            return new VanillaDbJdbcResultSet(false, "Error in UpdatePriceTxn: " + e.getMessage());
        }
    }
}
