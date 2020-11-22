import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rate {

	public static void main(String[] args) {
		
		DecimalFormat formatter = new DecimalFormat("###,###");

		double rate = 1.003d;
		
		double amt = 70000000;
//		double amt = 100000000;
		
		double profitSum = 0;
		
		String s = "2020-11-11";
//		String e = "2028-01-04";
		String e = "2028-01-04";
		LocalDate start = LocalDate.parse(s);
		LocalDate end = LocalDate.parse(e);
		List<LocalDate> totalDates = new ArrayList<>();
		
		String prevMonth = "2020-11";
		String prevYear = "";
		
		Map<String, String> amtMap = new HashMap<String, String>();
		List<String> amtList = new ArrayList<String>();
		
		double drawAmtSum = 0;
		int i = 1;
		int holiday = 0;
		while (!start.isAfter(end)) {
		    totalDates.add(start);
		    start = start.plusDays(1);
		    DayOfWeek dayOfWeek = start.getDayOfWeek();
		    
		    String dateStr = start.toString();
		    
		    String name = dayOfWeek.name();
			if("SATURDAY".equals(name) || "SUNDAY".equals(name) || holiday > 0) {
				holiday--;
		    	continue;
		    }
		    
			String month = dateStr.substring(0, 7);
			if(!prevMonth.equals(month)) {
				holiday = 2;
				amt -= 500000;
				prevMonth = month;
				System.out.println("=====================================");
			}
			
			
			if(start.equals(LocalDate.parse("2021-06-30"))) {
				System.out.println("=====================================" + start.toString());
				amt -= 60000000;
			}
			
			if(amt > 2000000000) {
				amt -= 200000000;
				drawAmtSum += 200000000;
				System.out.println("=====================drawAmtSum += 200000000");
			}
			
		    double aaa = amt;
			amt *= rate;
			double diff = amt- aaa;
			profitSum += diff;
			
			System.out.println(i + " " + start + " => " + formatter.format(amt) +  ", " + formatter.format(diff) +  ", " + formatter.format(profitSum)  + " (" + dayOfWeek + ")");
			i++;
			
			String year = dateStr.substring(0, 4);
			if(!prevYear.equals(year)) {
				amtMap.put(prevYear, formatter.format(amt));
				prevYear = year;
				
				amtList.add(prevYear);
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
				
	}

}
