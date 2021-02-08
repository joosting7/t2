import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rate {

	public static void main(String[] args) {
		
		DecimalFormat formatter = new DecimalFormat("###,###");

		double rate = 1.005d;
		double addAmt = 0d;
		double initAmt = 4500000;
		double amt = initAmt;
//		double amt = 100000000;
		
		double profitSum = 0;
		
		String s = "2021-01-17";
//		String e = "2028-01-04";
		String e = "2028-01-04";
		LocalDate start = LocalDate.parse(s);
		LocalDate end = LocalDate.parse(e);
		List<LocalDate> totalDates = new ArrayList<>();
		
		String prevYear = "";
		
		Map<String, String> amtMap = new HashMap<String, String>();
		List<String> amtList = new ArrayList<String>();
		
		double drawAmtSum = 0;
		int i = 1;
		while (!start.isAfter(end)) {
		    totalDates.add(start);
		    start = start.plusDays(1);
		    String dateStr = start.toString();
		    
		    YearMonth ym = YearMonth.from(start);
		    LocalDate endDay = ym.atEndOfMonth();
		    
		    if(endDay.toString().equals(dateStr)) {
		    	amt += 100000;
		    	initAmt += 100000;
		    	addAmt += 100000;
		    }
		    
		    DayOfWeek dayOfWeek = start.getDayOfWeek();
		    String name = dayOfWeek.name();
			if("SATURDAY".equals(name)) {
				System.out.println("---------------------------------------------------------------------------------------------------------------");
		    }
			
			if("SATURDAY".equals(name) || "SUNDAY".equals(name)) {
		    	continue;
		    }
			
		    boolean isContinue = checkHoliday(dateStr, dayOfWeek);
		    
		    if(isContinue == true) {
		    	continue;
		    }
		    
			
			if(start.equals(LocalDate.parse("2024-01-31"))) {
				System.out.println("=====================================" + start.toString());
				amt -= 85000000;
			}
			
			if(amt > 200000000) {
				amt -= 20000000;
				drawAmtSum += 20000000;
			}
			
		    double aaa = amt;
			amt *= rate;
			double diff = amt- aaa;
			profitSum += diff;
			
			String str2 = String.format("%10s %15s %15s %15s %15s %15d %15d", start, formatter.format(amt), formatter.format(diff), formatter.format(profitSum), dayOfWeek, new Double((amt-initAmt)/initAmt*100).intValue(), i);
			System.out.println(str2);
			i++;
			
			String year = dateStr.substring(0, 4);
			if(!prevYear.equals(year)) {
				amtMap.put(prevYear, formatter.format(amt));
				prevYear = year;
				
				amtList.add(prevYear);
			}
			
			if(start.equals(LocalDate.parse("2024-01-31")) || amt < 0) {
				break;
			}
		}
		
		for (int j = 0; j < amtList.size(); j++) {
			String aaa = amtMap.get(amtList.get(j));
			if(aaa == null) {
				break;
			}
			System.out.println(amtList.get(j) + " => " + aaa);
		}
		
		System.out.println("drawAmtSum => " + formatter.format(drawAmtSum));
		System.out.println("addAmt => " + formatter.format(addAmt));
				
	}

	private static boolean checkHoliday(String dateStr, DayOfWeek dayOfWeek) {
		boolean isContinue = false;
		if(dateStr.endsWith("01-01") || dateStr.endsWith("03-01") || dateStr.endsWith("05-05") || dateStr.endsWith("06-06") || dateStr.endsWith("08-15") || dateStr.endsWith("10-03") || dateStr.endsWith("10-09") || dateStr.endsWith("12-25") || dateStr.endsWith("12-31")) {
			isContinue = true;
		}
		
		if(dateStr.equals("2021-02-11") || dateStr.equals("2021-02-12") || dateStr.equals("2021-05-19")
				|| dateStr.equals("2021-09-20") || dateStr.equals("2021-09-21") || dateStr.equals("2021-09-22")) {
			isContinue = true;
		}
		
		if(dateStr.equals("2022-01-31") || dateStr.equals("2022-02-01") || dateStr.equals("2022-02-02") || dateStr.equals("2022-03-09")
				|| dateStr.equals("2022-06-01") || dateStr.equals("2022-09-09") || dateStr.equals("2022-09-12")) {
			isContinue = true;
		}
		
		if(dateStr.equals("2023-01-23") || dateStr.equals("2023-09-28") || dateStr.equals("2023-09-29")) {
			isContinue = true;
		}
		
		if(dateStr.equals("2024-02-09") || dateStr.equals("2024-04-10") || dateStr.equals("2024-05-15") || dateStr.equals("2024-09-16") || dateStr.equals("2024-09-17") || dateStr.equals("2024-09-18")) {
			isContinue = true;
		}
		
		if(dateStr.equals("2025-01-28") || dateStr.equals("2025-01-29") || dateStr.equals("2025-01-30")
				|| dateStr.equals("2025-10-06") || dateStr.equals("2025-10-06")) {
			isContinue = true;
		}
		
		if(isContinue == true) {
			String str2 = String.format("%10s %63s %15s", dateStr, dayOfWeek, "HOLIDAY");
			System.out.println(str2);
		}
		
		return isContinue;
	}

}
