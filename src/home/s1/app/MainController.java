package home.s1.app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import home.s1.alg.NaiveBayesian;
import home.s1.alg.NaiveCollaborativeFiltering;
import home.s1.alg.NativeConnectionBased;
import home.s1.alg.RecommendAlgorithm;
import home.s1.alg.RecommendAlgorithm.MLMethods;
import home.s1.alg.ShiZhiJian;
import home.s1.data.SingleRecord;
import home.s1.data.UserShoppingHistory;

public class MainController {
	private String inputFilePath;
	private String outputFilePath;
	private int trainingSize;
	private int testSize;
	private RecommendAlgorithm algorithm;
	private UserShoppingHistory history;
	
	public MainController(String inputFilePath,
			String outputFilePath, int trainingSize, int testSize, MLMethods method) {
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;
		this.trainingSize = trainingSize;
		this.testSize = testSize;
		history = new UserShoppingHistory(this.inputFilePath, 8);
		algorithm = AlgorithmFactory.getAlgorithm(method, history.getTrainingSet());
	}

	public void writeFile(String outputFilePath, Map<Long, Set<Integer>> topRecommendations) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath));
			Iterator<Long> userIter = topRecommendations.keySet().iterator();
			while (userIter.hasNext()) {
				long userId = userIter.next();
				bw.write(userId + "\t");
				Iterator<Integer> iter = topRecommendations.get(userId).iterator();
				while (iter.hasNext()) {
					bw.write(iter.next().toString());
					if (iter.hasNext()) {
						bw.write(",");
					}
				}
				if (userIter.hasNext())
					bw.write("\n");
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String run() {
		Map<Long, Set<Integer>> topRecommendations = algorithm.execute();
		writeFile(this.outputFilePath, topRecommendations);
		Map<Long, Set<Integer>> testSet = RecommendAlgorithm.getUserToItem(
			UserShoppingHistory.getPurchaseRecords(history.getTestSet()));
		
		double match = 0;
		double total = 0;
		for (long userId : testSet.keySet()) {
			for (int itemId : testSet.get(userId)) {
				if (topRecommendations.containsKey(userId)
						&& topRecommendations.get(userId).contains(itemId)) {
					++match;
				}
				++total;
			}
		}
		String result = "";
		double recall = match / total;
		result += "recall is : " + recall + "\n";
		total = 0;
		for (long userId : testSet.keySet()) {
			if (topRecommendations.containsKey(userId)) {
				total += topRecommendations.get(userId).size();
			}
		}
		
		double precision = match / total;
		result += "precision is : " + precision + "\n";
		
		result += "f1 score is : " + 2 * recall * precision / (recall + precision) + "\n";
		return result;
	}
}

class AlgorithmFactory {
	public static RecommendAlgorithm getAlgorithm(
				MLMethods method, List<SingleRecord> trainingSet) {
		if (MLMethods.NaiveColloborativeFiltering.equals(method)) {
			return new NaiveCollaborativeFiltering(trainingSet);
		}
		else if (MLMethods.NaiveConnectionBased.equals(method)) {
			return new NativeConnectionBased(trainingSet);
		}
		else if (MLMethods.NaiveBayesian.equals(method)) {
			return new NaiveBayesian(trainingSet);
		}
		else if (MLMethods.Shizhijian.equals(method)){
			return new ShiZhiJian(trainingSet);
		}
		return null;
	}
}
