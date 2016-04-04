package com.fanatics.sterling.util;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.yantra.yfc.dblayer.YFCContext;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class FANDBUtil {

	private static YFCLogCategory logger= YFCLogCategory.instance(YFCLogCategory.class);
	
	public static ArrayList<Object[]> getDBResult(YFSEnvironment env, String sqlQuery,int noOfColumnus) throws Exception
	{
		YFCContext ctxt = (YFCContext) env;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<Object[]> queryRecords = new ArrayList<Object[]>();
			try {
				stmt = ctxt.getConnection().createStatement();
				rs = stmt.executeQuery(sqlQuery);
				logger.verbose("Executed SQL statement");
				
				while (rs.next()) {
					Object[] resultRow = new Object[noOfColumnus]; 
					for( int i=0;i<noOfColumnus;i++)
					{
						resultRow[i]=rs.getObject(i+1);
					}
					queryRecords.add(resultRow);
				}
			} catch (Exception e) {
				logger.verbose("Exception in  :: getDBResult :: DSWDBUtil "	+ e);
				throw new YFSException(e.getMessage());
			} finally {
				// Close Resultset
				try {
					rs.close();
				} catch (Exception e) {
					logger.verbose("Exception while closing resultset  :: getDBResult :: DSWDBUtil "	+ e);
					throw new YFSException(e.getMessage());
				}

				// Close Statement
				try {
					stmt.close();
				} catch (Exception e) {
					logger.verbose("Exception while closing statement  :: getDBResult :: DSWDBUtil "	+ e);
					throw new YFSException(e.getMessage());
				}
			}
			return queryRecords;
	}
}
