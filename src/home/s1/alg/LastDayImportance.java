package home.s1.alg;

import home.s1.data.MyDate;
import home.s1.data.SingleRecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LastDayImportance extends RecommendAlgorithm {

	public LastDayImportance(List<SingleRecord> ush) {
		super(ush);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<Long, Set<Integer>> execute() {
		// TODO Auto-generated method stub
	List<SingleRecord> totalRecords
		= super.history;
//		= getRecordInTime(super.history, new MyDate(4, 1), new MyDate(9, 1));
	Map<Long, Map<Integer, Integer[]>> actionsCounts 
		= userTotalActionCountInTime(totalRecords);
	
	Map<Long, Set<Integer>> recommendations
		= new HashMap<Long, Set<Integer>>();

	//4 to 8, total 9531, p: 2149
	//4 to 6, total 7017, p: 1413
	//7 to 8, total 6547, p: 1188, in 4 to 6 purchase: 462, in 4 to 6 click: 932

	Map<Long, Map<Integer, List<MyDate>>> userPurchaseToDate
		= getUserActions(totalRecords, ActionList.Purchase.getCode());
	Map<Long, Map<Integer, List<MyDate>>> userClickToDate
	= getUserActions(totalRecords, ActionList.Click.getCode());

	
	for (long userId : actionsCounts.keySet()) {
		int[] totalCount = new int[4];
		
		for (int itemId : actionsCounts.get(userId).keySet()) {
			for (int i = 0; i < actionsCounts.get(userId).get(itemId).length - 1; ++i) {
				totalCount[i] += actionsCounts.get(userId).get(itemId)[i];
			}
		}
		
		for (int itemId : actionsCounts.get(userId).keySet()) {
			if (!recommendations.containsKey(userId)) {
				recommendations.put(userId, new HashSet<Integer>());
			}

				double[] prob = new double[4];
				
				Integer[] counts = actionsCounts.get(userId).get(itemId);
				for (int i = 0; i < totalCount.length; ++i) {
						prob[i] = counts[i]
								/ (double) totalCount[i];
				}
				
				if (userPurchaseToDate.containsKey(userId)
						&& userPurchaseToDate.get(userId).containsKey(itemId)) {
					List<MyDate> dateList = userPurchaseToDate.get(userId).get(itemId);
					int size = dateList.size();
					MyDate lastDay = dateList.get(size - 1);
					if (size > 1)
						recommendations.get(userId).add(itemId);
					MyDate pivot = new MyDate(6, 1);
					if (pivot.before(lastDay))
						recommendations.get(userId).add(itemId);
				}
				
				if (userClickToDate.containsKey(userId)
						&& userClickToDate.get(userId).containsKey(itemId)) {
					List<MyDate> dateList = userClickToDate.get(userId).get(itemId);
					int size = dateList.size();
					MyDate lastDay = dateList.get(size - 1);
					MyDate pivot = new MyDate(6, 15);
					if (size > 2 && pivot.before(lastDay))
						recommendations.get(userId).add(itemId);
				}
				
				if (prob[0] > 0.16 && counts[0] > 3) {
					recommendations.get(userId).add(itemId);
				}
		}
	}
	
	return recommendations;
	}


	private Map<Long, Map<Integer, List<MyDate>>> getUserActions(List<SingleRecord> recordList,
			int action) {
		Map<Long, Map<Integer, List<MyDate>>> map
		= new HashMap<Long, Map<Integer, List<MyDate>>>();
		for (SingleRecord r : recordList) {
			if (r.getAction() == action) {
				if (!map.containsKey(r.getUserId())) {
					map.put(r.getUserId(), new HashMap<Integer, List<MyDate>>());
				}
				if (!map.get(r.getUserId()).containsKey(r.getItemId())) {
					List<MyDate> dates = new ArrayList<MyDate>();
					dates.add(r.getDate());
					map.get(r.getUserId()).put(r.getItemId(), dates);
				}
				else if (!map.get(r.getUserId()).get(r.getItemId()).contains(r.getDate())) {
					map.get(r.getUserId()).get(r.getItemId()).add(r.getDate());
				}
			}
		}
		
		for (long userId : map.keySet()) {
			for (int itemId : map.get(userId).keySet()) {
				Collections.sort(map.get(userId).get(itemId));
			}
		}
		return map;
	}
	
	public void writeFile(
			String outputFilePath, Map<Long, Map<Integer, Integer[]>> featureSet) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath));
			Iterator<Long> userIter = featureSet.keySet().iterator();
			while (userIter.hasNext()) {
				long userId = userIter.next();
				Iterator<Integer> iter = featureSet.get(userId).keySet().iterator();
				while (iter.hasNext()) {
					bw.write(userId + ",");
					int itemId = iter.next();
					bw.write(itemId + ",");
					for (int i = 0; i < 5; ++i) {
						bw.write(featureSet.get(userId).get(itemId)[i].toString());
						if (i != 4) {
							bw.write(",");
						}
					}
					if (userIter.hasNext() || iter.hasNext())
						bw.write("\n");
				}
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<SingleRecord> getRecordInTime(List<SingleRecord> recordList, 
			MyDate startTime, MyDate endTime) {
		List<SingleRecord> result = new ArrayList<SingleRecord>();
		for (SingleRecord r : recordList) {
			if (r.getDate().before(endTime) && startTime.before(r.getDate())) {
				result.add(r);
			}
		}
		return result;
	}
	
	//user id, user id, actions
	private Map<Long, Map<Integer, Integer[]>> userTotalActionCountInTime(
			List<SingleRecord> recordList) {
		Map<Long, Map<Integer, Integer[]>> actionCount 
		= new HashMap<Long, Map<Integer, Integer[]>>();
		for (SingleRecord r : recordList) {
			if (!actionCount.containsKey(r.getUserId())) {
				actionCount.put(r.getUserId(), new HashMap<Integer,Integer[]>());
			}
			if (!actionCount.get(r.getUserId()).containsKey(r.getItemId())) {
				Integer[] c = new Integer[5];
				for (int i = 0; i < c.length; ++i) {
					c[i] = new Integer(0);
				}
				actionCount.get(r.getUserId()).put(r.getItemId(), c);
			}
			actionCount.get(r.getUserId()).get(r.getItemId())[r.getAction()]++;
		}
		return actionCount;
	}
}
