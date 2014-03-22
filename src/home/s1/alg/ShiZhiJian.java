package home.s1.alg;

import home.s1.data.SingleRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShiZhiJian extends RecommendAlgorithm {

	public ShiZhiJian(List<SingleRecord> ush) {
		super(ush);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<Long, Set<Integer>> execute() {
		// TODO Auto-generated method stub
		List<SingleRecord> recordList = super.history;
		for (SingleRecord r : recordList) {
			//r.get
		}
		Map<Long, Set<Integer>> recommendations = new HashMap<Long, Set<Integer>>();
		return recommendations;
	}

}
