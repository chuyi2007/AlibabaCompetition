package home.s1.alg;

import home.s1.data.SingleRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NativeConnectionBased extends RecommendAlgorithm{

	public NativeConnectionBased(List<SingleRecord> ush) {
		super(ush);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<Long, Set<Integer>> execute() {
		// TODO Auto-generated method stub
		
		int[] validActions = new int[]{
				//ActionList.Click.getCode(),
				ActionList.Bookmark.getCode(), 
				ActionList.Purchase.getCode(),
				ActionList.ShoppingCart.getCode()
				};
		Map<Long, Set<Integer>> userToItem = getUserToItem(super.history,
					validActions);
		

//		Map<Integer, Set<Long>> itemToUser = getItemToUser(super.history
//				//);
//				, validActions);
//		Map<Integer, Map<Integer, Float>> itemSimilarity = getItemSimilarity(itemToUser);
//
		Map<Long, Set<Integer>> recommendations = new HashMap<Long, Set<Integer>>();
//		for (Long userId : userToItem.keySet()) {
//			Set<Integer> itemSet = new HashSet<Integer>();
//			itemSet.addAll(userToItem.get(userId));
//			for (Integer itemId : userToItem.get(userId)) {
//				if (itemSimilarity.containsKey(itemId)) {
//					for (Integer newId : itemSimilarity.get(itemId).keySet()) {
//						double sim = itemSimilarity.get(itemId).get(newId);
//						if (sim > 1.0) {
//							itemSet.add(newId);
//						}
//					}
//				}
//			}
//			recommendations.put(userId, itemSet);
//		}
		Map<Long, Map<Integer, Float>> userBrandProb 
		= getBrandProb(super.history,
				ActionList.Click.getCode());
		Map<Long, Map<Integer, Float>> userBrandProb1 
		= getBrandProb(super.history, 
				ActionList.Purchase.getCode());
		Map<Long, Map<Integer, Float>> userBrandProb2 
		= getBrandProb(super.history, 
				ActionList.ShoppingCart.getCode());
		Map<Long, Map<Integer, Float>> userBrandProb3 
		= getBrandProb(super.history, 
				ActionList.Bookmark.getCode());
	
		
		addRecommendations(0.06F, userBrandProb, recommendations);
		addRecommendations(0F, userBrandProb1, recommendations);
		addRecommendations(1F, userBrandProb2, recommendations);

		addRecommendations(1F, userBrandProb3, recommendations);
		
//		List<Map<Long, Map<Integer, Float>>> userBrandProbList
//		= getBrandProbPerMonth(super.history, ActionList.Click.getCode(), new int[] {4, 5, 6});
//		List<Map<Long, Map<Integer, Float>>> userBrandProbList1
//		= getBrandProbPerMonth(super.history, ActionList.Purchase.getCode(), new int[] {4, 5, 6});
//		List<Map<Long, Map<Integer, Float>>> userBrandProbList2
//		= getBrandProbPerMonth(super.history, ActionList.ShoppingCart.getCode(), new int[] {4, 5, 6});
//		List<Map<Long, Map<Integer, Float>>> userBrandProbList3
//		= getBrandProbPerMonth(super.history, ActionList.Bookmark.getCode(), new int[] {4, 5, 6});
//		addRecommendationsPerMonth(0.4F, userBrandProbList, recommendations);
//		addRecommendationsPerMonth(0.0F, userBrandProbList1, recommendations);
//		addRecommendationsPerMonth(0.0F, userBrandProbList2, recommendations);
//		addRecommendationsPerMonth(0.0F, userBrandProbList3, recommendations);
		return recommendations;
	}
	
	private void addRecommendationsPerMonth(float alpha, 
			List<Map<Long, Map<Integer, Float>>> userBrandProbList,
			Map<Long, Set<Integer>> recommendations) {
		for (Map<Long, Map<Integer, Float>> userBrandProb : userBrandProbList) {
			addRecommendations(alpha, userBrandProb, recommendations);
		}
	}
	private void addRecommendations(float alpha, Map<Long, Map<Integer, Float>> userBrandProb,
			Map<Long, Set<Integer>> recommendations) {
		for (Long userId : userBrandProb.keySet()) {
			for (int itemId : userBrandProb.get(userId).keySet()) {
				float prob = userBrandProb.get(userId).get(itemId);
				if ( prob > alpha) {
					if (!recommendations.containsKey(userId)) {
						recommendations.put(userId, new HashSet<Integer>());
					}
					recommendations.get(userId).add(itemId);
				}
			}
		}
	}
	
	private Map<Long, Integer> userActionCount(List<SingleRecord> recordList, int action) {
		Map<Long, Integer> actionCount = new HashMap<Long, Integer>();
		for (SingleRecord r : recordList) {
			if (!actionCount.containsKey(r.getUserId())) {
				actionCount.put(r.getUserId(), 0);
			}
			if (r.getAction() == action) {
				int count = actionCount.get(r.getUserId());
				actionCount.remove(r.getUserId());
				actionCount.put(r.getUserId(), count + 1);
			}
		}
		return actionCount;
	}
	
	//p(i, j) is the stickiness from user i to brand j per month
	private Map<Long, Map<Integer, Float>> getBrandProb(List<SingleRecord> recordList,
			int action) {
		Map<Long, Integer> actionCount = userActionCount(recordList, action);
		Map<Long, Map<Integer, Float>> userBrandSum 
			= new HashMap<Long, Map<Integer, Float>>();
		for (SingleRecord r : recordList) {
			if (r.getAction() == action) {
				Long userId = r.getUserId();
				int itemId = r.getItemId();
				if (!userBrandSum.containsKey(userId)) {
					userBrandSum.put(userId, new HashMap<Integer, Float>());
				}
				Map<Integer, Float> prob = userBrandSum.get(userId);
				if (!prob.containsKey(itemId)) {
					prob.put(itemId, 0.0F);
				}
				float count = prob.get(itemId);
				++count;
				prob.remove(itemId);
				prob.put(itemId, count);
			}
		}
		Map<Long, Map<Integer, Float>> userBrandProb 
		= new HashMap<Long, Map<Integer, Float>>();
		for (long userId : userBrandSum.keySet()) {
			for (int itemId : userBrandSum.get(userId).keySet()) {
				Float count = userBrandSum.get(userId).get(itemId);
				float fraction = count
						/ actionCount.get(userId);
				if (!userBrandProb.containsKey(userId)) {
					userBrandProb.put(userId, new HashMap<Integer, Float>());
				}
				if (count > 1)
					userBrandProb.get(userId).put(itemId, fraction);
			}
		}
		return userBrandProb;
	}	
	
	private List<Map<Long, Map<Integer, Float>>> getBrandProbPerMonth(
			List<SingleRecord> recordList,
			int action, int[] month) {
		List<Map<Long, Map<Integer, Float>>> maps 
			= new ArrayList<Map<Long, Map<Integer, Float>>>();
		for (int m : month) {
			List<SingleRecord> monthRecords = new ArrayList<SingleRecord>();
			for (SingleRecord record : super.history) {
				if (record.getDate().getMonth() == m) {
					monthRecords.add(record);
				}
			}
			maps.add(getBrandProb(monthRecords, action));
		}
		return maps;
	}
}
