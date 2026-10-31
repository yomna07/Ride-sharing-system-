

public class DateTime implements IDateTime {
	
	final private int minute , hour, day , month , year;
	
	public DateTime ( int year, int month , int day , int hour , int minute) {
		if ( month < 1 || month > 12 || day < 1 || day > 31 || hour < 0 || hour > 23 || minute < 0 || minute > 59)
			throw new IllegalArgumentException ("Invalid Date or Time");
			
		this.minute = minute;
		this.hour = hour;
		this.day = day;
		this.month = month;
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}
	
	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public String format() {
		return String.format("%02d/%02d/%d %02d:%02d", day, month , year, hour, minute);
	}

	public int compareTo(IDateTime other) {
		
		
		if (year != other.getYear())
			return year > other.getYear()? 1 : -1;	
			
		if (month != other.getMonth())
			return month > other.getMonth()? 1 : -1;
				
		if (day != other.getDay())
			return day > other.getDay()? 1 : -1;
					
		if (hour != other.getHour())
			return hour > other.getHour()? 1 : -1;
						
		if (minute != other.getMinute())
			return minute > other.getMinute()? 1 : -1;
							 
		return 0;
		}
	
	public static DateTime parse(String s) {
	    // format: MM/DD/YYYY HH:MM
	    String[] parts = s.trim().split(" ");
	    String[] d = parts[0].split("/");
	    String[] t = parts[1].split(":");
	    return new DateTime(Integer.parseInt(d[2]), Integer.parseInt(d[0]),
	                        Integer.parseInt(d[1]), Integer.parseInt(t[0]), Integer.parseInt(t[1]));
	}
				
	
}
