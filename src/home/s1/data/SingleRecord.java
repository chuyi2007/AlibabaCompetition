package home.s1.data;

public class SingleRecord implements Comparable<SingleRecord>{
	private long userId;
	private int itemId;
	private int action;
	private MyDate date;
	public SingleRecord 
		(long userId, int itemId, int action, MyDate date) {
		super();
		this.setUserId(userId);
		this.setItemId(itemId);
		this.setAction(action);
		this.setDate(date);
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public MyDate getDate() {
		return date;
	}
	public void setDate(MyDate date) {
		this.date = date;
	}
	@Override
	public int compareTo(SingleRecord other) {
		// TODO Auto-generated method stub
		if (this.userId < other.userId)
			return -1;
		else if (this.userId > other.userId)
			return 1;
		else if (this.date.before(other.date))
			return -1;
		else if (other.date.before(this.date))
			return 1;
		else if (this.itemId < other.itemId)
			return -1;
		else if (this.itemId > other.itemId)
			return 1;
		else if (this.action < other.action)
			return -1;
		else
			return 1;
	}	
}
