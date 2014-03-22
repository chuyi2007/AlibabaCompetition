package home.s1.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserShoppingHistory {
	private List<SingleRecord> trainingSet;
	private List<SingleRecord> testSet;
	public UserShoppingHistory(String inputFile, int month) {
		try {
			setTrainingSet(new ArrayList<SingleRecord>());
			setTestSet(new ArrayList<SingleRecord>());
			readFromFile(inputFile, month);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readFromFile(String inputFile, int month) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line = null;
		String[] tokens;
		br.readLine();
		List<SingleRecord> recordList = new ArrayList<SingleRecord>();
		while ((line = br.readLine()) != null) {
			tokens = line.split(",");
			String[] d = tokens[3].split(" ");
			MyDate date = new MyDate(Integer.parseInt(d[0]), Integer.parseInt(d[1]));
			recordList.add(new SingleRecord(Long.parseLong(tokens[0]),
					Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), date));
		}
		br.close();
		splitData(recordList, month);
	}

	public void splitData(List<SingleRecord> history, int month) {
		for (SingleRecord h : history) {
			if (h.getDate().getMonth() <= month)
				getTrainingSet().add(h);
			else
				getTestSet().add(h);
		}
	}

	public static List<SingleRecord> getPurchaseRecords(List<SingleRecord> recordList) {
		List<SingleRecord> purchases = new ArrayList<SingleRecord>();
		for (SingleRecord r : recordList) {
			if (r.getAction() == 1) {
				purchases.add(r);
			}
		}
		return purchases;
	}
	
	public List<SingleRecord> getTestSet() {
		return testSet;
	}

	public void setTestSet(List<SingleRecord> testSet) {
		this.testSet = testSet;
	}

	public List<SingleRecord> getTrainingSet() {
		return trainingSet;
	}

	public void setTrainingSet(List<SingleRecord> trainingSet) {
		this.trainingSet = trainingSet;
	}
}
