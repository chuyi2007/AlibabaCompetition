package home.s1.alg;

import home.s1.data.MyDate;
import home.s1.data.SingleRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class FeatureExtraction extends RecommendAlgorithm{

	public FeatureExtraction(List<SingleRecord> ush) {
		super(ush);
		// TODO Auto-generated constructor stub
	}

	private List<Entry<Integer, Integer>> getItemPurchase(List<SingleRecord> recordList) {
		Map<Integer, Integer> itemCount = new HashMap<Integer, Integer>();
		for (SingleRecord r : recordList) {
			if (r.getAction() == 1) {
				if (!itemCount.containsKey(r.getItemId())) {
					itemCount.put(r.getItemId(), 1);
				}
				itemCount.put(r.getItemId(), itemCount.get(r.getItemId()) + 1);
			}
		}
		Set<Entry<Integer, Integer>> set = itemCount.entrySet();
        List<Entry<Integer, Integer>> list = new ArrayList<Entry<Integer, Integer>>(set);
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>()
        {
            public int compare( Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
//        for(Map.Entry<Integer, Integer> entry : list){
//            System.out.println(entry.getKey()+" ==== "+entry.getValue());
//        }
        return list;
	}
	
	public Map<Integer, Double> getCommonItem(List<Entry<Integer, Integer>> list1,
			List<Entry<Integer, Integer>> list2) {
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		for (Entry<Integer, Integer> e1 : list1) {
			for (Entry<Integer, Integer> e2 : list2) {
				if (e1.getKey().equals(e2.getKey())) {
					int a = Math.min(e1.getValue(), e2.getValue());
					int b = Math.max(e1.getValue(), e2.getValue());
					map.put(e1.getKey(), a / (double)b);
				}
			}
		}
		return map;
	}
	
	public Map<Integer, Double> getCommonItem3(List<Entry<Integer, Integer>> list1,
			List<Entry<Integer, Integer>> list2, List<Entry<Integer, Integer>> list3) {
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		for (Entry<Integer, Integer> e1 : list1) {
			for (Entry<Integer, Integer> e2 : list2) {
				for (Entry<Integer, Integer> e3 : list3) {
				if (e1.getKey().equals(e2.getKey())
						&& e3.getKey().equals(e2.getKey())) {
					int a = Math.min(e3.getValue(), Math.min(e1.getValue(), e2.getValue()));
					int b = Math.max(e3.getValue(), Math.max(e1.getValue(), e2.getValue()));
					map.put(e1.getKey(), a / (double)b);
				}
				}
			}
		}
		return map;
	}
	
	@Override
	public Map<Long, Set<Integer>> execute() {
		// TODO Auto-generated method stub
//		List<SingleRecord> trainingRecords 
//			= getRecordInTime(super.history, new MyDate(4, 1), new MyDate(6, 1));
//		List<SingleRecord> predictRecords 
//			= getRecordInTime(super.history, new MyDate(6, 1), new MyDate(7, 1));
//		Map<Long, Map<Integer, Integer[]>> result = userTotalActionCountInTime(trainingRecords);
//		Map<Long, Set<Integer>> predictions 
//			= userActionCountInTime(predictRecords, ActionList.Purchase.getCode());
//		generateSet(result, predictions);
//		
//		writeFile("/Users/Ryan/Documents/Dropbox/aliCompetition/data/45_6_feature.txt", result);
//		
		List<SingleRecord> totalRecords
			= getRecordInTime(super.history, new MyDate(4, 1), new MyDate(9, 1));
		Map<Long, Map<Integer, Integer[]>> actionsCounts 
			= userTotalActionCountInTime(totalRecords);
		
//		List<SingleRecord> totalRecords1
//		= getRecordInTime(super.history, new MyDate(4, 1), new MyDate(5, 10));
//		List<SingleRecord> totalRecords2
//		= getRecordInTime(super.history, new MyDate(5, 10), new MyDate(6, 1));
//		List<SingleRecord> totalRecords3
//		= getRecordInTime(super.history, new MyDate(6, 1), new MyDate(7, 1));
//		List<Entry<Integer, Integer>> itemCounts1 = getItemPurchase(totalRecords1);
//		List<Entry<Integer, Integer>> itemCounts2 = getItemPurchase(totalRecords2);
//		List<Entry<Integer, Integer>> itemCounts3 = getItemPurchase(totalRecords3);
//		
//		Map<Integer, Double> common1 = getCommonItem(itemCounts1, itemCounts2);
//		Map<Integer, Double> common2 = getCommonItem(itemCounts1, itemCounts3);
//		Map<Integer, Double> common3 = getCommonItem(itemCounts2, itemCounts3);
//		Map<Integer, Double> common4 = getCommonItem3(itemCounts1, itemCounts2, itemCounts3);
//		
		
		Map<Long, Set<Integer>> recommendations
			= new HashMap<Long, Set<Integer>>();
		Map<Integer, Integer[]> itemActionCount = itemTotalActionCountInTime(totalRecords);
	
		//4 to 8, total 9531, p: 2149
		//4 to 6, total 7017, p: 1413
		Set<Integer> bought = new HashSet<Integer>();
		for (int itemId : itemActionCount.keySet()) {
			if (itemActionCount.get(itemId)[ActionList.Purchase.getCode()] > 0) {
				bought.add(itemId);
			}
		}
		
		Set<Integer> click = new HashSet<Integer>();
		for (int itemId : itemActionCount.keySet()) {
			if (itemActionCount.get(itemId)[ActionList.Click.getCode()] > 0) {
				click.add(itemId);
			}
		}
		
		List<SingleRecord> preRecords
		= getRecordInTime(super.history, new MyDate(4, 1), new MyDate(6, 1));
		
		Map<Integer, Integer[]> preItemActionCount = itemTotalActionCountInTime(preRecords);

		//4 to 8, total 9531, p: 2149
		//4 to 6, total 7017, p: 1413
		//7 to 8, total 6547, p: 1188, in 4 to 6 purchase: 462, in 4 to 6 click: 932
		Set<Integer> preBought = new HashSet<Integer>();
		for (int itemId : preItemActionCount.keySet()) {
			if (preItemActionCount.get(itemId)[ActionList.Purchase.getCode()] > 0) {
				preBought.add(itemId);
			}
		}
		
		Set<Integer> preClick = new HashSet<Integer>();
		for (int itemId : preItemActionCount.keySet()) {
			if (preItemActionCount.get(itemId)[ActionList.Click.getCode()] > 0) {
				preClick.add(itemId);
			}
		}
		
		List<SingleRecord> lastRecords
		= getRecordInTime(super.history, new MyDate(6, 1), new MyDate(7, 1));
		
		Map<Integer, Integer[]> lastItemActionCount = itemTotalActionCountInTime(lastRecords);

		//4 to 8, total 9531, p: 2149
		//4 to 6, total 7017, p: 1413
		//7 to 8, total 6547, p: 1188, in 4 to 6 purchase: 462, in 4 to 6 click: 932
		Set<Integer> lastBought = new HashSet<Integer>();
		for (int itemId : lastItemActionCount.keySet()) {
			if (lastItemActionCount.get(itemId)[ActionList.Purchase.getCode()] > 0) {
				lastBought.add(itemId);
			}
		}

		Set<Integer> mustBuy = new HashSet<Integer>();
		for (int itemId : lastBought) {
			if (preBought.contains(itemId)) {
				mustBuy.add(itemId);
			}
		}
		
		Map<Integer, Double> mustClick = new HashMap<Integer, Double>();
		for (int itemId : lastBought) {
			if (preClick.contains(itemId)
					&& !preBought.contains(itemId)) {
				int p = itemActionCount.get(itemId)[1];
				int c = itemActionCount.get(itemId)[0];
				double val = p 
						/ (double) c;
				mustClick.put(itemId, val);
			}
		}
		
		Map<Integer, Double> clickBuy = new HashMap<Integer, Double>();
		for (int itemId : itemActionCount.keySet()) {
			int p = itemActionCount.get(itemId)[1];
			int c = itemActionCount.get(itemId)[0];
			double val = p 
					/ (double) c;
			clickBuy.put(itemId, val);
		}
	
//		System.out.println(count);
//		
//		int count2 = 0;
//		for (int itemId : lastBought) {
//			if (click.contains(itemId)) {
//				++count2;
//			}
//		}
//		
//		System.out.println(count2);
//		
//		int count3 = 0;
//		for (int itemId : lastBought) {
//			if (click.contains(itemId) && bought.contains(itemId)) {
//				++count3;
//			}
//		}
//		
//		System.out.println(count3);
//		
//		Map<Integer, Map<Integer, Double>> itemSimilarity = 
//				getItemSimilarity1(itemByUsers(totalRecords, ActionList.Purchase.getCode()));
//		
		Map<Long, Set<Integer>> userMustBuy = getUserItemActions(totalRecords,
				new MyDate(4, 1), new MyDate(7, 1), 7);
		
		Map<Long, Map<Integer, List<MyDate>>> userPurchaseToDate
			= getUserActions(totalRecords, ActionList.Purchase.getCode());
		Map<Long, Map<Integer, List<MyDate>>> userClickToDate
		= getUserActions(totalRecords, ActionList.Click.getCode());
	
		try {
			Map<Long, Double[]> models 
				= readModel("/Users/Ryan/Documents/Dropbox/aliCompetition/data/45_6LRmodel_1.csv");
		
		
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
//				if (models.containsKey(userId)) {
//					Integer[] data = actionsCounts.get(userId).get(itemId);
//					if (data.length < 3) {
//						System.out.println("fuck");
//					}
//					Integer[] features = new Integer[3];
//					features[0] = 1;
//					features[1] = data[0];
//					features[2] = data[1];
//
//					double p = sigMoidFunction(models.get(userId), features);
//					if (p > 0.5) {
//						recommendations.get(userId).add(itemId);
//					}
//				}
//				else {
					double[] prob = new double[4];
					
					Integer[] counts = actionsCounts.get(userId).get(itemId);
					for (int i = 0; i < totalCount.length; ++i) {
							prob[i] = counts[i]
									/ (double) totalCount[i];
					}
					
					//recall is : 0.047996041563582385
					//precision is : 0.06327462491846053
					//f1 score is : 0.054586381541924585
					//click: 0.06, 3, purchase: 1
//					if (counts[0] > 0 && counts[1] == 0
//							&& counts[0] * clickBuy.get(itemId) >= 1) {
//						recommendations.get(userId).add(itemId);
//					}
					//else 
//					if (userPurchaseToDate.containsKey(userId)
//							&& userPurchaseToDate.get(userId).containsKey(itemId)
//							&& userPurchaseToDate.get(userId).get(itemId).size() > 2) {
//						recommendations.get(userId).add(itemId);
//					}
					
					if (userClickToDate.containsKey(userId)
							&& userClickToDate.get(userId).containsKey(itemId)) {
						List<MyDate> dateList = userClickToDate.get(userId).get(itemId);
						int size = dateList.size();
						MyDate lastDay = dateList.get(size - 1);
						if (size > 1 && new MyDate(6, 15).before(lastDay))
							recommendations.get(userId).add(itemId);
					}
//					if (common4.containsKey(itemId) && common4.get(itemId) < 0.1f) {
//						recommendations.get(userId).add(itemId);					
//					}
					if (userMustBuy.containsKey(userId) &&
							userMustBuy.get(userId).contains(itemId)
							&& counts[1] > 0) {
						recommendations.get(userId).add(itemId);
//						if (itemSimilarity.containsKey(itemId)) {
//							for (int itemId2 : itemSimilarity.get(itemId).keySet()) {
//								if (itemSimilarity.get(itemId).get(itemId2) 
//										> 0.2) {
//										//* counts[1] >= 0) {
//									recommendations.get(userId).add(itemId2);
//								}
//							}
//						}
					}
					
					
					
//					if (userMustClick.containsKey(userId) &&
//							userMustClick.get(userId).contains(itemId)
//							&& counts[0] > 0) {
//						recommendations.get(userId).add(itemId);
//					}
					
//					if (userMustAction.containsKey(userId) &&
//							userMustAction.get(userId).contains(itemId)) {
//						recommendations.get(userId).add(itemId);
//					}
//					
//					
//					int sum = 0;
//					for (int c : counts)
//						sum += c;
//					if (sum < 1)
//						continue;
//					if (prob[0] > 0.06 && counts[0] > 3) {
//						recommendations.get(userId).add(itemId);
//					}
//					else 
						if (counts[1] > 0 && mustBuy.contains(itemId)
							) {
						//&& userMustAction.containsKey(userId)) {
						recommendations.get(userId).add(itemId);
					}
					else if (counts[1] > 1) {
						recommendations.get(userId).add(itemId);
					}
					else if (mustClick.containsKey(itemId) 
							&& mustClick.get(itemId)
							* counts[0] > 1) {
						recommendations.get(userId).add(itemId);
					}
//				}
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recommendations;
	}

	private Map<Long, Map<Integer, List<MyDate>>> getUserActions(List<SingleRecord> recordList,
			int action) {
		Map<Long, Map<Integer, List<MyDate>>> map
		= new HashMap<Long, Map<Integer, List<MyDate>>>();
		for (SingleRecord r : recordList) {
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
		
		for (long userId : map.keySet()) {
			for (int itemId : map.get(userId).keySet()) {
				Collections.sort(map.get(userId).get(itemId));
			}
		}
		return map;
	}
	
	private Map<Long, Set<Integer>> getUserItemActions(List<SingleRecord> recordList,
			MyDate startDate, MyDate endDate, int days) {
		MyDate curDate = new MyDate(startDate);
		Map<Long, Set<Integer>> mustBuy
		= new HashMap<Long, Set<Integer>>();
		List<Map<Long, Map<Integer, Integer[]>>> actionsCounts
		= new ArrayList<Map<Long, Map<Integer, Integer[]>>>();
		while (curDate.before(endDate)) {
			List<SingleRecord> preRecords
			= getRecordInTime(super.history, curDate, curDate.addDay(days));
			curDate = curDate.addDay(days);
			if (preRecords.size() == 0)
				continue;
			
			actionsCounts.add(userTotalActionCountInTime(preRecords));
		}
		
		for (Map<Long, Map<Integer, Integer[]>> map1 : actionsCounts) {
			for (Map<Long, Map<Integer, Integer[]>> map2 : actionsCounts) {
				if (map1.size() > 0 && map1 != map2) {
				for (long userId : map1.keySet()) {
					if (map2.containsKey(userId)) {
						for (int itemId : map1.get(userId).keySet()) {
							if (map2.get(userId).containsKey(itemId)) {
								Integer[] count1 = map1.get(userId).get(itemId);
								Integer[] count2 = map2.get(userId).get(itemId);
								if (!mustBuy.containsKey(userId)) {
									mustBuy.put(userId, new HashSet<Integer>());
								}
								if (count1[1] > 0 && count2[1] > 0) {
									mustBuy.get(userId).add(itemId);
								}
//								if (count1[0] > 0 && count2[0] > 0) {
//									mustBuy.get(userId).add(itemId);
//								}
							}
						}
					}
				}
				}
			}
		}
		return mustBuy;
	}
	
	private Map<Integer, Map<Integer, Double>> getItemSimilarity1(Map<Integer, Set<Long>> itemToUser) {
		Map<Integer, Map<Integer, Double>> itemSimilarity
			= new HashMap<Integer, Map<Integer, Double>>();
		for (int itemId1 : itemToUser.keySet()) {
			for (int itemId2 : itemToUser.keySet()) {
				if (itemId1 != itemId2) {
					Set<Long> unionSet = new HashSet<Long>();
					int interSection = 0;
					for (Long userId1 : itemToUser.get(itemId1)) {
						if (itemToUser.get(itemId2).contains(userId1))
							interSection++;
						unionSet.add(userId1);
					}
					for (Long userId2 : itemToUser.get(itemId2)) {
						unionSet.add(userId2);
					}

					if (interSection > 0) {
						if (!itemSimilarity.containsKey(itemId1)) {
							itemSimilarity.put(itemId1, new HashMap<Integer, Double>());
						}
						itemSimilarity.get(itemId1).put(itemId2, interSection / (double)unionSet.size());
					}
				}
			}
		}
		return itemSimilarity;
	}
	
	private Map<Integer, Set<Long>> itemByUsers(List<SingleRecord> recordList,
			int action) {
		Map<Integer, Set<Long>> itemSet = new HashMap<Integer, Set<Long>>();
		for (SingleRecord r : recordList) {
			if (r.getAction() == action) {
				if (!itemSet.containsKey(r.getItemId())) {
					itemSet.put(r.getItemId(), new HashSet<Long>());
				}
				itemSet.get(r.getItemId()).add(r.getUserId());
			}
		}
		return itemSet;
	}
	
	private Map<Long, Double[]> readModel(String fileName) throws IOException {
		Map<Long, Double[]> map 
			= new HashMap<Long, Double[]>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line;
		String[] tokens;
		while((line = br.readLine()) != null) {
			tokens = line.split(",");
			Double[] model = new Double[tokens.length - 1];
			boolean flag = false;
			for (int i = 0; i < model.length; ++i) {
				model[i] = new Double(tokens[i + 1]);
				if (model[i] != 0)
					flag = true;
			}
			if (flag)
				map.put(Long.parseLong(tokens[0]), model);
		}
		br.close();
		return map;
	}
	
	private double sigMoidFunction(Double[] model, Integer[] feature) {
		double h = 0;
		for (int i = 0; i < model.length; ++i) {
			if (feature.length < 2) {
				System.out.println();
			}
			h += model[i] * feature[i];
		}
		return 1 / (1 + Math.exp(-h));
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
	
	private List<SingleRecord> getRecordInTime(List<SingleRecord> recordList, MyDate startTime, MyDate endTime) {
		List<SingleRecord> result = new ArrayList<SingleRecord>();
		for (SingleRecord r : recordList) {
			if (r.getDate().before(endTime) && startTime.before(r.getDate())) {
				result.add(r);
			}
//			else {
//				System.out.println();
//			}
		}
		return result;
	}

	private void generateSet(Map<Long, Map<Integer, Integer[]>> totalCount,
			Map<Long, Set<Integer>> trueValues) {
		for (long userId : totalCount.keySet()) {
			for (int itemId : totalCount.get(userId).keySet()) {
				if (trueValues.containsKey(userId) && trueValues.get(userId).contains(itemId)) {
					totalCount.get(userId).get(itemId)[4] = 1;
				}
			}
		}
	}

	//item id, actions
	private Map<Integer, Integer[]> itemTotalActionCountInTime(
			List<SingleRecord> recordList) {
		Map<Integer, Integer[]> actionCount 
		= new HashMap<Integer, Integer[]>();
		for (SingleRecord r : recordList) {
			int itemId = r.getItemId();
			if (!actionCount.containsKey(itemId)) {
				Integer[] c = new Integer[5];
				for (int i = 0; i < c.length; ++i) {
					c[i] = new Integer(0);
				}
				actionCount.put(itemId, c);
			}
			actionCount.get(itemId)[r.getAction()]++;
		}
		return actionCount;
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
		
	//user id, item id
	private Map<Long, Set<Integer>> userActionCountInTime(
			List<SingleRecord> recordList, int action) {
		Map<Long, Set<Integer>> userToItem = new HashMap<Long, Set<Integer>>();
		for (SingleRecord r : recordList) {
			if (r.getAction() == action) {
				if (!userToItem.containsKey(r.getUserId())) {
					userToItem.put(r.getUserId(), new HashSet<Integer>());
				}
				userToItem.get(r.getUserId()).add(r.getItemId());
			}
		}
		return userToItem;
	}
}
