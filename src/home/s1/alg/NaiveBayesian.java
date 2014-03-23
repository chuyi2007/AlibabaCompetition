package home.s1.alg;

import home.s1.data.MyDate;
import home.s1.data.SingleRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NaiveBayesian extends RecommendAlgorithm{

	public NaiveBayesian(List<SingleRecord> ush) {
		super(ush);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<Long, Set<Integer>> execute() {
		// TODO Auto-generated method stub
		Map<Integer, Map<Integer, Float>> actionMap 
			= getActionFloat(super.history, ActionList.Purchase.getCode(), 7, 0F);
		
		Map<Long, Set<Integer>> recommendations
			= new HashMap<Long, Set<Integer>>();
		RecommendAlgorithm algorithm = new NativeConnectionBased(super.history);
		recommendations = algorithm.execute();
		Map<Integer, Set<Integer>> similarItems = new HashMap<Integer, Set<Integer>>();
		for (Integer itemId : actionMap.keySet()) {
			if (!similarItems.containsKey(itemId)) {
				similarItems.put(itemId, new HashSet<Integer>());
			}
			for (Integer recommendId : actionMap.get(itemId).keySet()) {
				if (actionMap.get(recommendId).containsKey(itemId)) {
					similarItems.get(itemId).add(recommendId);
				}
			}
		}
		for (long userId : recommendations.keySet()) {
			for (int itemId : recommendations.get(userId)) {
				for (int addtionalId : similarItems.get(itemId)) {
					recommendations.get(userId).add(addtionalId);
				}
			}
		}
		
		return recommendations;
	}
	
	public Map<Long, Map<MyDate, List<Integer>>> getActionOnDate(List<SingleRecord> recordList,
			int action) {
		Map<Long, Map<MyDate, List<Integer>>> actionMap 
		= new HashMap<Long, Map<MyDate, List<Integer>>>();
		for (SingleRecord record : recordList) {
			if (record.getAction() == action) {
				long userId = record.getUserId();
				int itemId = record.getItemId();
				MyDate date = record.getDate();
				if (!actionMap.containsKey(userId)) {
					actionMap.put(userId, new HashMap<MyDate, List<Integer>>());
				}
				if (!actionMap.get(userId).containsKey(date)) {
					actionMap.get(userId).put(date, new ArrayList<Integer>());
				}
				actionMap.get(userId).get(date).add(itemId);
			}
		}
		return actionMap;
	}
	
	public Map<MyDate, Set<Integer>> getDateToItemByAction(List<SingleRecord> recordList,
			int action) {
		Map<MyDate, Set<Integer>> dateToItems
		= new HashMap<MyDate, Set<Integer>>();
		for (SingleRecord record : recordList) {
			if (action == record.getAction()) {
				int itemId = record.getItemId();
				MyDate date = record.getDate();
				if (!dateToItems.containsKey(date)) {
					dateToItems.put(date, new HashSet<Integer>());
				}
				dateToItems.get(date).add(itemId);
			}
		}
		return dateToItems;
	}
	
	public Map<Integer, Set<MyDate>> getItemToDate(
			List<SingleRecord> recordList,
			int action) {
		Map<Integer, Set<MyDate>> itemToDate
			= new HashMap<Integer, Set<MyDate>>();
		for (SingleRecord r : recordList) {
			if (r.getAction() == action) {
				if (!itemToDate.containsKey(r.getItemId())) {
					itemToDate.put(r.getItemId(), new HashSet<MyDate>());
				}
				itemToDate.get(r.getItemId()).add(r.getDate());
			}
		}
		return itemToDate;
	}
	
	public Map<Integer, Map<Integer, Float>> getActionFloat(
			List<SingleRecord> recordList,
			int action,
			int days,
			float threshold) {
		Map<MyDate, Set<Integer>> dateToItems = getDateToItemByAction(recordList, action);
		Map<Integer, Set<MyDate>> itemToDate = getItemToDate(recordList, action);
		Map<Integer, Integer> actionMap = getActionCountWithinTime(recordList, 
				dateToItems, action, days);
		
		Map<Integer, Map<Integer, Float>> map = new HashMap<Integer, Map<Integer, Float>>();
		
		for (Integer itemId1 : itemToDate.keySet()) {
			int actionCount = actionMap.get(itemId1);
			Map<Integer, Float> prob = new HashMap<Integer, Float>();
			for (Integer itemId2 : itemToDate.keySet()) {
				int numerator = 0;
				for (MyDate date : itemToDate.get(itemId1)) {
					for (int i = 1; i <= days; ++i) {
						MyDate posDay = date.addDay(i);
						if (dateToItems.containsKey(posDay) &&
								dateToItems.get(posDay).contains(itemId2)) {
							++numerator;
						}
					}
				}
				float fraction = (float) (numerator / (double) actionCount);
				if (fraction > threshold)
					prob.put(itemId2, fraction);
			}
			map.put(itemId1, prob);
		}
		
		return map;
	}
	
	public Map<Integer, Integer> getActionCountWithinTime(
			List<SingleRecord> recordList,
			Map<MyDate, Set<Integer>> dateToItems,
			int action,
			int days) {
		Map<Integer, Integer> actionMap 
		= new HashMap<Integer, Integer>();
		
		for (SingleRecord record : recordList) {
			if (record.getAction() == action) {
				int itemId = record.getItemId();
				MyDate date = record.getDate();
				if (!actionMap.containsKey(itemId)) {
					actionMap.put(itemId, 0);
				}
				int count = 0;
				for (int i = 1; i <= days; ++i) {
					MyDate key = date.addDay(i);
					if (dateToItems.containsKey(key)) {
						count += dateToItems.get(key).size();
					}
				}
				actionMap.put(itemId, actionMap.get(itemId) + count);
			}
		}
		return actionMap;
	}
}
