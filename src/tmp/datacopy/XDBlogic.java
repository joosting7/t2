package tmp.datacopy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class XDBlogic {
	public Connection sourceCon;
	public Connection destinationCon;
	private XLog log = null;
	private XCommon xc = null;

	XDBlogic(XLog mlog, XCommon mxc) {
		log = mlog;
		xc = mxc;
		try {
			sourceCon = sourceConnect();

			if (sourceCon == null) {
				log.info("Source DB Connection fail!");
				System.exit(0);
			} else {
				log.info("Source DB Connection success...");
			}
		} catch (Exception e) {
			log.exception(e, "Source Connection error!");
		}
		try {
			destinationCon = destinationConnect();

			if (destinationCon == null) {
				log.info("Destination DB Connection fail!");
				System.exit(0);
			} else {
				log.info("Destination DB Connection success...");
			}
		} catch (Exception e) {
			log.exception(e, "Destination Connection error!");
		}
	}

	Connection sourceConnect() {
		String dbDriver = xc.source_driver;
		String dbUrl = xc.source_url;
		String dbID = xc.source_id;
		String dbPwd = xc.source_pwd;

		System.out.println("Source DB Connection..");

		System.out.println("Driver : " + dbDriver);
		System.out.println("dbUrl : " + dbUrl);
		System.out.println("dbID : " + dbID);
		System.out.println("dbPwd : " + dbPwd);

		Connection con = null;
		try {
			Class.forName(dbDriver);

			con = DriverManager.getConnection(dbUrl, dbID, dbPwd);

			con.setAutoCommit(false);

			return con;
		} catch (Exception e) {
			log.exception(e, "DB connection Exception error. [{}]", e.getMessage());
			System.exit(0);
		}

		return null;
	}

	Connection destinationConnect() {
		String dbDriver = xc.destination_driver;
		String dbUrl = xc.destination_url;
		String dbID = xc.destination_id;
		String dbPwd = xc.destination_pwd;

		System.out.println("Destination DB Connection..");

		System.out.println("Driver : " + dbDriver);
		System.out.println("dbUrl : " + dbUrl);
		System.out.println("dbID : " + dbID);
		System.out.println("dbPwd : " + dbPwd);

		Connection con = null;
		try {
			Class.forName(dbDriver);

			con = DriverManager.getConnection(dbUrl, dbID, dbPwd);

			con.setAutoCommit(false);

			return con;
		} catch (Exception e) {
			log.exception(e, "DB connection Exception error. [{}]", e.getMessage());
			System.exit(0);
		}

		return null;
	}

	private ResultSet selectList(PreparedStatement ps) throws Exception {
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();

			return rs;
		} catch (Exception e) {
			log.exception(e, "select Exception error. [{}]", e.getMessage());
			throw e;
		}		
	}

	public void close() throws Exception {
		try {
			sourceCon.close();
			destinationCon.close();
			log.info("DB Connection is closed!");
		} catch (Exception e) {
			log.exception(e, "Exception error. [{}]", e.getMessage());
			throw e;
		}
	}

	public void rollback() throws Exception {
		try {
			destinationCon.rollback();
			log.info("rollback complete!");
		} catch (Exception e) {
			log.exception(e, "Exception error. [{}]", e.getMessage());
			throw e;
		}
	}

	public void commit() {
		try {
			destinationCon.commit();
			log.info("commit complete!");
		} catch (Exception e) {
			log.exception(e, "Exception error. [{}]", e.getMessage());
		}
	}

	public PreparedStatement insertDestinationToSource(PreparedStatement pstmt, List<String> dataType, List<String> param) throws Exception {
		
		try {
			String query = xc.insert_destination_data;

			if (pstmt == null) {
				pstmt = destinationCon.prepareStatement(query);
			}
			for (int i = 0; i < param.size(); i++) {
				if (param.get(i) == null)
					pstmt.setNull(i + 1, 12);
				else if ("DATE".equals(dataType.get(i)))
					pstmt.setDate(i + 1, xc.getCurrentSysDate());
				else if ("BIGINT".equals(dataType.get(i)))
					pstmt.setLong(i + 1, Long.parseLong((String) param.get(i)));
				else if ("DECIMAL".equals(dataType.get(i)))
					pstmt.setDouble(i + 1, Double.parseDouble((String) param.get(i)));
				else if ("DOUBLE".equals(dataType.get(i)))
					pstmt.setDouble(i + 1, Double.parseDouble((String) param.get(i)));
				else if ("FLOAT".equals(dataType.get(i)))
					pstmt.setFloat(i + 1, Float.parseFloat((String) param.get(i)));
				else if ("INTEGER".equals(dataType.get(i)))
					pstmt.setInt(i + 1, Integer.parseInt((String) param.get(i)));
				else
					pstmt.setString(i + 1, (String) param.get(i));
			}
			pstmt.addBatch();
		} catch (Exception e) {
			log.exception(e, "Exception error 발생. [{}]", e.getMessage());
			throw e;
		}

		return pstmt;
	}

	public long insertExecute(String param1) throws Exception {
		PreparedStatement pstmt = null;
		long ucnt = 0L;
		try {
			String query = xc.insert_destination_data;

			if (pstmt == null) {
				pstmt = destinationCon.prepareStatement(query);
			}
			String[] param = param1.split(";");
			
			for (int i = 0; i < param.length; i++)
				pstmt.setString(i + 1, param[i]);
			
			ucnt = pstmt.executeUpdate();
		} catch (Exception e) {
			log.exception(e, "Exception error 발생. [{}]", e.getMessage());
			throw e;
		} finally {
			try { if (pstmt != null) pstmt.close(); } catch (Exception e2) {}
		}
		return ucnt;
	}
	
	public int updateExecute(String query) throws Exception
	{
		
		PreparedStatement pstmt = null; 
		
		try {
			pstmt = destinationCon.prepareStatement(query);
			
			
			int n_res = pstmt.executeUpdate();
			
			return n_res;
		} 
		catch(Exception e) {
			log.exception(e, "1차 데이터 삭제시 Exception error 발생. [{}]", e.getMessage());
			throw e;
		} finally {
			try { if (pstmt != null) pstmt.close(); } catch (Exception e2) {}
		}
	}

	public ResultSet selectSourceData() {
		ResultSet rs = null;

		String query = xc.select_source_data;
		try {
			PreparedStatement pstmt = sourceCon.prepareStatement(query);

			rs = selectList(pstmt);
		} catch (Exception e) {
			log.exception(e, "Exception error 발생. [{}]", e.getMessage());
		}

		return rs;
	}

	public ResultSet selectSourceData(PreparedStatement pstmt) {
		ResultSet rs = null;
		try {
			rs = selectList(pstmt);
		} catch (Exception e) {
			log.exception(e, "Exception error 발생. [{}]", e.getMessage());
		}

		return rs;
	}

	public PreparedStatement getStmt(String query, String param1) {
		PreparedStatement pstmt = null;
		String[] param = param1.split(";");
		try {
			pstmt = sourceCon.prepareStatement(query);
			for (int i = 0; i < param.length; i++)
				pstmt.setString(i + 1, param[i]);
		} catch (Exception e) {
			log.exception(e, "Exception error 발생. [{}]", e.getMessage());
		}

		return pstmt;
	}
}