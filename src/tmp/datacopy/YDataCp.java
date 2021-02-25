package tmp.datacopy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedList;
import java.util.List;

public class YDataCp {
	XLog log = null;
	XCommon xc = null;

	private int BATCH_COUNT = 5000;

	public YDataCp(String mConfigPath, String modifier) {
		xc = new XCommon(mConfigPath, modifier, "datacopy");
		log = xc.getLog();
		BATCH_COUNT = Integer.parseInt(xc.commit_count);
	}

	public void execute() {
		
		XDBlogic db = null;
		
		try {
			db = new XDBlogic(log, xc);
		} catch (Exception e) {
			log.exception(e, "Exception error : [{}]", e.getMessage());
			System.exit(0);
		}

		log.info("쿼리 조회 시작");
		try {
			String query = xc.select_source_data;

			String param_list = xc.param_list.trim();
			String[] sub_param_list = param_list.split("\n");
			PreparedStatement pstmt = null;
			PreparedStatement insert_pstmt = null;
			ResultSet rs = null;

			for (int idx = 0; idx < sub_param_list.length; idx++) {
				
				log.info("param data = {}", sub_param_list[idx].trim());

				pstmt = db.getStmt(query, sub_param_list[idx].trim());
				rs = db.selectSourceData(pstmt);

				ResultSetMetaData rsmd = rs.getMetaData();
				List<String> head = new LinkedList<String>();

				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					if ((rsmd.getColumnType(i + 1) == 93) || (rsmd.getColumnType(i + 1) == 91) || (rsmd.getColumnType(i + 1) == 92))
						head.add("DATE");
					else if (rsmd.getColumnType(i + 1) == -5)
						head.add("BIGINT");
					else if (rsmd.getColumnType(i + 1) == 3)
						head.add("DECIMAL");
					else if (rsmd.getColumnType(i + 1) == 8)
						head.add("DOUBLE");
					else if (rsmd.getColumnType(i + 1) == 6)
						head.add("FLOAT");
					else if (rsmd.getColumnType(i + 1) == 4)
						head.add("INTEGER");
					else {
						head.add("STRING");
					}
				}
				
				int i = 0;
				List<String> data = new LinkedList<String>();

				while (rs.next()) {
					data.clear();
					
					for (int j = 0; j < rsmd.getColumnCount(); j++) {
						data.add(rs.getString(j + 1));
					}

					insert_pstmt = db.insertDestinationToSource(insert_pstmt, head, data);

					i++;

					if (i % BATCH_COUNT == 0) {
						insert_pstmt.executeBatch();
						insert_pstmt.clearBatch();

						log.info("[{}] inserted", i + "");
						
						db.commit();
					}
				}

				if (i % BATCH_COUNT > 0) {
					insert_pstmt.executeBatch();
					insert_pstmt.clearBatch();

					log.info("[{}] inserted", i + "");
					
					db.commit();
				}

				if (pstmt != null) try { pstmt.close();} catch (Exception e) {}
				
				if (rs != null) try { rs.close();} catch (Exception e) {}
			}
			
			if (insert_pstmt != null) 
				try { insert_pstmt.close();} catch (Exception e) {}
			
		} catch (Exception e) {
			log.exception(e, "Exception error : [{}]", e.getMessage());
			try {
				db.rollback();
			} catch (Exception e1) {
				log.exception(e, "Exception error : [{}]", e.getMessage());
			}
		}
		
		try { db.close(); } catch (Exception e) {}
		
		log.info("데이터 생성 종료");
	}

	public static void main(String[] args) throws Exception {
		YDataCp xcp = new YDataCp(args[0], "AUTO");
		xcp.execute();
	}
}