package home.s1.data;

public class MyDate implements Comparable<MyDate> {
	private int month;
	private int day;
	public MyDate(int month, int day) {
		super();
		this.setMonth(month);
		this.setDay(day);
	}
	public MyDate(MyDate date) {
		super();
		this.setMonth(date.getMonth());
		this.setDay(date.getDay());
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + month;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyDate other = (MyDate) obj;
		if (day != other.day)
			return false;
		if (month != other.month)
			return false;
		return true;
	}
	
	public boolean before(MyDate other) {
		if (this.month < other.month) {
			return true;
		}
		else if (this.month > other.month) {
			return false;
		}
		else if (this.day < other.day) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public MyDate addDay(int day) {
		int newDay = (day + this.day) % 30;
		int newMonth = this.month + (day + this.day) / 30;
		return new MyDate(newMonth, newDay);
	}
	@Override
	public int compareTo(MyDate other) {
		// TODO Auto-generated method stub
		if (this.before(other)) {
			return -1;
		}
		else {
			return 1;
		}
	}
}
