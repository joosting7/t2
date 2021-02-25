package tmp.datacopy;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XCommon {
	public String configName = null;
	public String modifier = null;

	private XLog log = null;

	public String log4jpath = null;
	public String commit_count = null;
	public String source_file_location = null;

	public String source_driver = null;
	public String source_url = null;
	public String source_id = null;
	public String source_pwd = null;

	public String destination_driver = null;
	public String destination_url = null;
	public String destination_id = null;
	public String destination_pwd = null;

	public String select_source_data = null;
	public String param_list = null;
	public String insert_destination_data = null;
	

	XCommon(String mConfigName, String mModifier, String logger) {
		configName = mConfigName;
		modifier = mModifier;
		try {
			readConfig();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		log = new XLog(log4jpath, logger);
	}

	public XLog getLog() {
		return log;
	}

	private void readConfig() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		factory.setCoalescing(false);

		Document xmldoc = parser.parse(configName);

		Element root = xmldoc.getDocumentElement();

		for (Node fch = root.getFirstChild(); fch != null; fch = fch.getNextSibling()) {
			
			if (fch.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (fch.getNodeName().equals("log4jpath")) {
				log4jpath = getText(fch);
			} else if (fch.getNodeName().equals("source_file_location")) {
				source_file_location = getText(fch);
			} else if (fch.getNodeName().equals("commit_count")) {
				commit_count = getText(fch);
			} else if (fch.getNodeName().equals("db_source_info")) {
				for (Node sch = fch.getFirstChild(); sch != null; sch = sch
						.getNextSibling()) {
					if (sch.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if (sch.getNodeName().equals("source_driver")) {
						source_driver = getText(sch);
					} else if (sch.getNodeName().equals("source_url")) {
						source_url = getText(sch);
					} else if (sch.getNodeName().equals("source_id")) {
						source_id = getText(sch);
					} else if (sch.getNodeName().equals("source_pwd")) {
						source_pwd = getText(sch);
					}
				}
			} else if (fch.getNodeName().equals("db_destination_info")) {
				for (Node sch = fch.getFirstChild(); sch != null; sch = sch
						.getNextSibling()) {
					if (sch.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if (sch.getNodeName().equals("destination_driver")) {
						destination_driver = getText(sch);
					} else if (sch.getNodeName().equals("destination_url")) {
						destination_url = getText(sch);
					} else if (sch.getNodeName().equals("destination_id")) {
						destination_id = getText(sch);
					} else if (sch.getNodeName().equals("destination_pwd")) {
						destination_pwd = getText(sch);
					}
				}
			} else if (fch.getNodeName().equals("query"))
				for (Node sch = fch.getFirstChild(); sch != null; sch = sch
						.getNextSibling()) {
					if (sch.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if (sch.getNodeName().equals("select_source_data")) {
						select_source_data = getText(sch);
					} else if (sch.getNodeName().equals("param_list")) {
						param_list = getText(sch);
					} else if (sch.getNodeName().equals(
							"insert_destination_data"))
						insert_destination_data = getText(sch);
				}
		}
	}

	private String getText(Node ch) {
		if (ch.getFirstChild() != null) {
			return ch.getTextContent();
		}

		return null;
	}

	public String replaceAll(String source, String str1, String str2) {
		String tmp = "";

		int str1_length = str1.length();
		int i = 0;

		if (source == null)
			return null;

		do {
			if (i + str1_length > source.length()) {
				tmp = tmp + source.charAt(i);
				i++;
			} else if (str1.equals(source.substring(i, i + str1_length))) {
				tmp = tmp + str2;
				i += str1_length;
			} else {
				tmp = tmp + source.charAt(i);
				i++;
			}
		} while (i < source.length());

		return tmp;
	}

	public java.sql.Date getCurrentSysDate() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
	}
}