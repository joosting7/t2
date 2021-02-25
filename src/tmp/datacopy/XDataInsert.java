package tmp.datacopy;


public class XDataInsert {
	XLog log = null;
	XCommon xc = null;

	public XDataInsert(String mConfigPath, String modifier) {
		xc = new XCommon(mConfigPath, modifier, "datacopy");
		log = xc.getLog();
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
		
		String param_list = xc.param_list.trim();
		String[] sub_param_list = param_list.split("\n");
		
		try {
			for (int idx = 0; idx < sub_param_list.length; idx++) {
				log.info("param data = {}", sub_param_list[idx].trim());
				
				long n_res = db.insertExecute(sub_param_list[idx].trim());
	
				db.commit();
				
				System.out.println("commit complete!");
				System.out.println("insert count = " + n_res);
			}
			
		} catch (Exception e) {
			log.exception(e, "Exception error : [{}]", e.getMessage());
			try {
				db.rollback();
			} catch (Exception e1) {
				log.exception(e, "Exception error : [{}]", e.getMessage());
			}
		}
		
		try { db.close();} catch (Exception e1) {}
		
		log.info("데이터 생성 종료");
	}

	public static void main(String[] args) throws Exception {
		XDataInsert xcp = new XDataInsert(args[0], "AUTO");
		xcp.execute();
	}
}