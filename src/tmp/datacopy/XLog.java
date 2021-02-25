package tmp.datacopy;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class XLog {
	private Logger mLogger = null;
	private static final int INFO = 0;
	private static final int ERROR = 1;
	private static final int WARN = 2;
	private static final int DEBUG = 3;
	private static final int EXCEPTIONERROR = 4;

	XLog(String path, String logger) {
		PropertyConfigurator.configure(path.trim());
		this.mLogger = Logger.getLogger(logger);
	}

	public <TYPE>void info(TYPE msg, TYPE ... args) {
		logWrite(INFO, msg, args);
	}

	public <TYPE>void error(TYPE msg, TYPE ... args) {
		logWrite(ERROR, msg, args);
	}
	
	public <TYPE>void exception(Exception e, TYPE msg, TYPE ... args) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement arrs[] = null;
		
		arrs = e.getStackTrace();
		
		sb.append(arrs[0].getClassName());
		sb.append("(");
		sb.append(arrs[0].getMethodName());
		sb.append(":");
		sb.append(arrs[0].getLineNumber());
		sb.append(") ");
		
		sb.append(msg);
		
		logWrite(EXCEPTIONERROR, appendLocationHint(sb.toString()), args);
		
	}

	public <TYPE> void debug(TYPE msg, TYPE ... args) {
		logWrite(DEBUG, msg, args);
	}

	public <TYPE> void warn(TYPE msg, TYPE ... args) {
		logWrite(WARN, msg, args);
	}

	private <TYPE> void logWrite(int mod, TYPE msg, TYPE ... args) {
		StringBuffer sb = new StringBuffer();
		String log = (String) msg;
		int length = log.length();

		int pos = 0;
		int from_pos = 0;
		int arg_idx = 0;
		while (true) {
			pos = log.indexOf("{}", from_pos);

			if (pos < 0) {
				sb.append(log.substring(from_pos, length));
				break;
			}

			sb.append(log.substring(from_pos, pos));
			if (args == null) {
				sb.append("null");
				sb.append(log.substring(pos + 2));
				break;
			}

			if (arg_idx >= args.length) {
				sb.append(log.substring(pos));
				break;
			}

			sb.append(args[(arg_idx++)]);
			from_pos = pos + 2;
		}

		if (mod == 0) {
			this.mLogger.info(sb);
		} else if (mod == 1) {
			this.mLogger.error("[ERROR]" + appendLocationHint(sb));
		} else if (mod == 2) {
			this.mLogger.warn("[WARN]" + appendLocationHint(sb));
		} else if (mod == 4) {
			this.mLogger.error("[EXCEPTION ERROR]" + sb);
		} else
			this.mLogger.debug("[DEBUG]" + appendLocationHint(sb));
	}

	private <TYPE> String appendLocationHint(TYPE s) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] arrs = null;

		arrs = new Exception().getStackTrace();

		sb.append(arrs[3].getClassName());
		sb.append("(");
		sb.append(arrs[3].getMethodName());
		sb.append(":");
		sb.append(arrs[3].getLineNumber());
		sb.append(") ");

		sb.append(s);

		return sb.toString();
	}
}