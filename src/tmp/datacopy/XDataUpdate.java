package tmp.datacopy;

import java.io.BufferedReader;
import java.io.FileReader;

public class XDataUpdate {
	XLog log = null;
	XCommon xc = null;

	private int BATCH_COUNT = 5000;

	public XDataUpdate(String mConfigPath, String modifier) {
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
			String file_path = xc.source_file_location;
			
			try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
				String line;
				int i = 0, update_cnt = 0;
				
				while((line = br.readLine()) != null) {
					line = xc.replaceAll(line, ";", "").trim();
					update_cnt += db.updateExecute(line);
					
					i++;
					
					if (i % BATCH_COUNT == 0) {
												
						log.info("[{}] line read / [{}] updated", i + "", update_cnt + "");
						
						db.commit();
					}
				}
				
				if (i % BATCH_COUNT > 0) {
					
					log.info("[{}] line read / [{}] updated", i + "", update_cnt + "");
					
					db.commit();
					
				}
				
				br.close();
				
			} catch (Exception e) {
				log.exception(e, "Exception error : [{}]", e.getMessage());				
			}
			
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
		XDataUpdate xu = new XDataUpdate(args[0], "AUTO");
		xu.execute();
	}
}