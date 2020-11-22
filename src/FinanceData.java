

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FinanceData {

	public static void main(String[] args) {
		try {
			
			String code = "214370";
			
            String id1 = selectMarketSum(code);
            
            System.out.println(code + "===>" + id1 ); 
            
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

	private static String selectMarketSum(String code) throws IOException {
		// 1. 수집 대상 URL
		Document html = selectHtml("https://finance.naver.com/item/main.nhn?code=" + code);
		
		// 4. HTML 출력
//            System.out.println( html.toString() ); 
		
		String id1 = html.getElementById("_market_sum").text();
		return id1;
	}

	private static Document selectHtml(String URL) throws IOException {
		// 2. Connection 생성
		Connection conn = Jsoup.connect(URL);
 
		// 3. HTML 파싱.
		Document html = conn.get(); // conn.post();
		return html;
	}

}
