package home.s1.alg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import home.s1.data.SingleRecord;

public class NaiveCollaborativeFiltering extends RecommendAlgorithm {

	public NaiveCollaborativeFiltering(List<SingleRecord> ush) {
		super(ush);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<Long, Set<Integer>> execute() {
		// TODO Auto-generated method stub
		Map<Integer, Set<Long>> itemToUser = getItemToUser(super.history);
		Map<Integer, Map<Integer, Float>> itemSimilarity = getItemSimilarity(itemToUser);
		Map<Integer, Double> itemWeight = getWeight(itemSimilarity);
		
		Map<Long, Set<Integer>> userToItem = getUserToItem(super.history);
		
		Map<Long, Map<Integer, Float>> recommendations = new HashMap<Long, Map<Integer, Float>>();
		for (long userId : userToItem.keySet()) {
			for (int itemId : itemToUser.keySet()) {
				Set<Integer> existItems = userToItem.get(userId);
				if (!existItems.contains(itemId)) {
					double sum = 0;
					for (int key : itemSimilarity.get(itemId).keySet()) {
						if (existItems.contains(key)) {
							sum += itemSimilarity.get(itemId).get(key);
						}
					}
					//sum /= itemWeight.get(itemId);
					if (!recommendations.containsKey(userId)) {
						recommendations.put(userId, new HashMap<Integer, Float>());
					}
					//if (sum > 0)
						recommendations.get(userId).put(itemId, (float) sum);
				}
			}
		}
		
		Map<Long, Set<Integer>> topRecommendations = new HashMap<Long, Set<Integer>>();
		for (long userId : recommendations.keySet()) {
			Set<Integer> q = new HashSet<Integer>();
			for (int itemId : recommendations.get(userId).keySet()) {
				//if (recommendations.get(userId).get(itemId) > 0.0001)
					q.add(itemId);
				
			}
			topRecommendations.put(userId,  q);
		}
		
		return topRecommendations;
	}
}
