package home.s1.alg;

import home.s1.data.SingleRecord;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class RecommendAlgorithm {
	public enum MLMethods{
		NaiveColloborativeFiltering, NaiveConnectionBased, NaiveBayesian, Shizhijian, FeatureExtraction;
	}
	
    public enum ActionList
    {
        Click(0), Purchase(1), Bookmark(2), ShoppingCart(3);
        
        private int code;
        
        private ActionList(int c) {
          code = c;
        }

        public int getCode() {
          return code;
        }
    }
    
    public enum MonthList {
    	April(4), May(5), June(6), July(7), August(8);
        private int code;
        
        private MonthList(int c) {
          code = c;
        }

        public int getCode() {
          return code;
        }
    }
    
	protected List<SingleRecord> history;
	
	public RecommendAlgorithm(List<SingleRecord> ush) {
		history = ush;
	}
	
	abstract public Map<Long, Set<Integer>> execute();
	
	
	public static Map<Integer, Double> getWeight(Map<Integer, Map<Integer, Float>> itemSimilarity) {
		Map<Integer, Double> weight = new HashMap<Integer, Double>();
		for (int key : itemSimilarity.keySet()) {
			double sum = 0;
			for (int key2 : itemSimilarity.get(key).keySet()) {
				sum += itemSimilarity.get(key).get(key2);
			}
			weight.put(key, sum);
		}
		return weight;
	}
	
	public static Map<Integer, Map<Integer, Float>> getItemSimilarity(Map<Integer, Set<Long>> itemToUser) {
		Map<Integer, Map<Integer, Float>> map = new HashMap<Integer, Map<Integer, Float>>();
		for (Integer key1 : itemToUser.keySet()) {
			for (Integer key2 : itemToUser.keySet()) {
				if (key1 != key2) {
					int commonCount = getCommonCount(itemToUser.get(key1), itemToUser.get(key2));
					if (commonCount > 0) {
						double val = 2 * commonCount / (double) (itemToUser.get(key1).size() + itemToUser.get(key2).size());
						Map<Integer, Float> single = new HashMap<Integer, Float>();
						single.put(key2, (float)val);
						map.put(key1, single);
					}
				}
			}
		}
		return map;
	}
	
	public static int getCommonCount(Set<Long> set, Set<Long> set2) {
		int count = 0;
		for (long i : set) {
			if (set2.contains(i)) {
				++count;
			}
		}
		return count;
	}
	
	public static Map<Integer, Set<Long>> getItemToUser(List<SingleRecord> recordList) {
		Map<Integer, Set<Long>> itemToUser = new HashMap<Integer, Set<Long>>();
		for (SingleRecord s : recordList) {
			if (!itemToUser.containsKey(s.getItemId())) {
				itemToUser.put(s.getItemId(), new HashSet<Long>());
			}
			itemToUser.get(s.getItemId()).add(s.getUserId());
		}
		return itemToUser;
	}
	
	public static Map<Integer, Set<Long>> getItemToUser(List<SingleRecord> recordList,
														int[] actions) {
		Map<Integer, Set<Long>> itemToUser = new HashMap<Integer, Set<Long>>();
		for (SingleRecord s : recordList) {
			if (checkExist(s.getAction(), actions)) {
				if (!itemToUser.containsKey(s.getItemId())) {
					itemToUser.put(s.getItemId(), new HashSet<Long>());
				}
				itemToUser.get(s.getItemId()).add(s.getUserId());
			}
		}
		return itemToUser;
	}
	
	private static boolean checkExist(int val, int[] A) {
		for (int a : A) {
			if (a == val)
				return true;
		}
		return false;
	}
	
	public static Map<Integer, Set<Long>> getItemToUser(List<SingleRecord> recordList,
			int action, int month) {
		Map<Integer, Set<Long>> itemToUser = new HashMap<Integer, Set<Long>>();
		for (SingleRecord s : recordList) {
			if (s.getAction() == action && s.getDate().getMonth() == month) {
				if (!itemToUser.containsKey(s.getItemId())) {
					itemToUser.put(s.getItemId(), new HashSet<Long>());
				}
				itemToUser.get(s.getItemId()).add(s.getUserId());
			}
		}
		return itemToUser;
	}
	
	public static Map<Long, Set<Integer>> getUserToItem(List<SingleRecord> recordList) {
		Map<Long, Set<Integer>> itemToUser = new HashMap<Long, Set<Integer>>();
		for (SingleRecord s : recordList) {
			if (!itemToUser.containsKey(s.getUserId())) {
				itemToUser.put(s.getUserId(), new HashSet<Integer>());
			}
			itemToUser.get(s.getUserId()).add(s.getItemId());
		}
		return itemToUser;
	}
	
	public static Map<Long, Set<Integer>> getUserToItem(List<SingleRecord> recordList,
			int[] actions) {
		Map<Long, Set<Integer>> itemToUser = new HashMap<Long, Set<Integer>>();
		for (SingleRecord s : recordList) {
			if (checkExist(s.getAction(), actions)) {
				if (!itemToUser.containsKey(s.getUserId())) {
					itemToUser.put(s.getUserId(), new HashSet<Integer>());
				}
				itemToUser.get(s.getUserId()).add(s.getItemId());
			}
		}
		return itemToUser;
	}
	
	public static Map<Long, Set<Integer>> getUserToItem(List<SingleRecord> recordList,
			int action, int month) {
		Map<Long, Set<Integer>> itemToUser = new HashMap<Long, Set<Integer>>();
		for (SingleRecord s : recordList) {
			if (s.getAction() == action && s.getDate().getMonth() == month) {
				if (!itemToUser.containsKey(s.getUserId())) {
					itemToUser.put(s.getUserId(), new HashSet<Integer>());
				}
				itemToUser.get(s.getUserId()).add(s.getItemId());
			}
		}
		return itemToUser;
	}
}
