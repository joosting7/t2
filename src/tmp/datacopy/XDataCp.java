package tmp.datacopy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedList;
import java.util.List;

public class XDataCp {
	XLog log = null;
	XCommon xc = null;

	private int BATCH_COUNT = 5000;

	public XDataCp(String mConfigPath, String modifier) {
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
			ResultSet rs = db.selectSourceData();

			PreparedStatement pstmt = null;
			ResultSetMetaData rsmd = rs.getMetaData();
			
			List<String> head = new LinkedList<String>();

			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				if ((rsmd.getColumnType(i + 1) == 93)
						|| (rsmd.getColumnType(i + 1) == 91)
						|| (rsmd.getColumnType(i + 1) == 92))
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

				pstmt = db.insertDestinationToSource(pstmt, head, data);

				i++;

				if (i % BATCH_COUNT == 0) {
					pstmt.executeBatch();
					pstmt.clearBatch();

					log.info("[{}] inserted", i + "");
					db.commit();
				}

			}

			if (i % BATCH_COUNT > 0) {
				pstmt.executeBatch();
				pstmt.clearBatch();

				log.info("[{}] inserted", i + "");
				db.commit();
			}

			if (pstmt != null)	try { pstmt.close();} catch (Exception e2) {}
			if (rs != null) try { rs.close(); } catch (Exception e2) {}
			
		} catch (Exception e) {
			log.exception(e, "Exception error : [{}]", e.getMessage());
			try {
				db.rollback();
			} catch (Exception e1) {
				log.exception(e, "Exception error : [{}]", e.getMessage());
			}
		}
		try {
			db.close();
		} catch (Exception localException3) {
		}
		log.info("데이터 생성 종료");
	}

	public static void main(String[] args) throws Exception {
		XDataCp xcp = new XDataCp(args[0], "AUTO");
		xcp.execute();
	}
}