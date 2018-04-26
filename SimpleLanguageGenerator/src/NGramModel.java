import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class NGramModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	HashMap<String, HashMap<String, Integer>> tm; //A tree map to hold the (n-1) part of the ngram 
	HashMap<String, Integer> wordCounts;
	int nSize;

	public NGramModel(int n) {
		nSize = n;
		tm = new HashMap<>();
		wordCounts = new HashMap<String, Integer>();
	}

	/*
	public void generateNGrams(List<String> words, int n) {
		for(int i = 1-n; i < words.size()-n; i++) {
			addNgram(words, i, n);
			if(i >= 0) {
				addToCounter(words.get(i));
			}
		}
	}*/
	public void generateNGrams(List<String> words, int n) {
		for(int i = 0; i < words.size()-n; i++) {
			for(int x = n; x > 0; x--) {
				addNgram(words, i, n);
			}
			if(i >= 0) {
				addToCounter(words.get(i));
			}
		}
	}
	
	private void addToCounter(String s) {
		if(wordCounts.containsKey(s)) {
			wordCounts.put(s, wordCounts.get(s) + 1);
		} else 
			wordCounts.put(s, 1);
	}
	
	private void addNgram(List<String> words, int i, int n) {
		StringBuilder sb = new StringBuilder();
		for(int cursor=i; cursor < n+i -1; cursor++) {
			if(cursor < 0) {
				continue;
			} else {
				if (sb.length() == 0)
					sb.append(words.get(cursor));
				else {
					sb.append(" ");
					sb.append(words.get(cursor));
				}
			}
		}
		String nextWord = words.get(n+i -1);
		String begGram = sb.toString();

		if(tm.containsKey(begGram)) {
			if(tm.get(begGram).containsKey(nextWord))
				tm.get(begGram).put(nextWord, tm.get(begGram).get(nextWord) + 1);
			else
				tm.get(begGram).put(nextWord, 1);
		} else {
			HashMap<String, Integer> x = new HashMap<>();
			x.put(nextWord, 1);
			tm.put(begGram, x);
		}

	}
/*	
	private void addNgram(List<String> words, int i, int n) {
		StringBuilder sb = new StringBuilder();
		for(int cursor=i; cursor < n+i -1; cursor++) {
			if(cursor < 0) {
				continue;
			} else {
				if (sb.length() == 0)
					sb.append(words.get(cursor));
				else {
					sb.append(" ");
					sb.append(words.get(cursor));
				}
			}
		}
		String nextWord = words.get(n+i -1);
		String begGram = sb.toString();

		if(tm.containsKey(begGram)) {
			if(tm.get(begGram).containsKey(nextWord))
				tm.get(begGram).put(nextWord, tm.get(begGram).get(nextWord) + 1);
			else
				tm.get(begGram).put(nextWord, 1);
		} else {
			HashMap<String, Integer> x = new HashMap<>();
			x.put(nextWord, 1);
			tm.put(begGram, x);
		}

	}*/
}
