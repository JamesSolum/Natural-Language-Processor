import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LanguageGenerator {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		if(args.length < 2) {
			System.out.println("Please input the file path to the Model, and the number of sentences.");
		} else { 
			System.out.println("Deserializing");
			NGramModel model = deserializeFile(args[0], NGramModel.class);
			System.out.println("Generating Text: ");
			generateText(model, Integer.parseInt(args[1]));
		}

	}

	private static void generateText(NGramModel model, int sentences) {
		NGramContainer ngc = new NGramContainer(model.nSize);
		StringBuilder sb = new StringBuilder();
		String firstWord = generateFirstWord(model);

		ngc.add(firstWord.trim());
		sb.append(firstWord);
		int count = 0;
		while(count < sentences) {
			String nextWord = getNextWord(ngc.toString(), model);
			ngc.add(nextWord);
			sb.append(" ");
			sb.append(nextWord);
			if(nextWord.contains(".") || nextWord.contains("?") || nextWord.contains("?")){
				count++;
			}
		}
		System.out.println(sb.toString());
	}

	private static String getNextWord(String words, NGramModel model) {
		while(model.tm.get(words) == null) {
			words = reduce(words);
		}
		HashMap<String, Integer> possibleWords = model.tm.get(words);
		Map<String, Float> weights = generateWeights(possibleWords);
		return weightedRandomChoice(weights);
	}

	private static String reduce(String words) {
		String[] ws = words.split(" ");
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < ws.length; i++) {
			sb.append(ws[i]);
			sb.append(" ");
		}
		return sb.toString().trim();

	}

	private static Map<String, Float> generateWeights(Map<String, Integer> m){
		int total = m.values().stream().mapToInt(i -> i.intValue()).sum();
		if(total != 0) {
			HashMap<String, Float> weights = new HashMap<String, Float>();
			for(String word : m.keySet()) { // Change wordcounts so it reflects percentages
				weights.put(word, ((float) m.get(word)) / total);
			}
			return weights;
		} else {
			return null;
		}
	}

	public static String generateFirstWord(NGramModel model) {
		int total = model.wordCounts.values().stream().mapToInt(i -> i.intValue()).sum();
		List<String> firstWords = model.tm.keySet().stream().map((s) -> s.split(" ")[0]).collect(Collectors.toList());
		HashSet<String> uniqueWords = new HashSet<String>(firstWords);

		HashMap<String, Float> weights = new HashMap<String, Float>();
		for(String word : model.wordCounts.keySet()) { // Change wordcounts so it reflects percentages
			weights.put(word, ((float) model.wordCounts.get(word)) / total);
			model.wordCounts.put(word, model.wordCounts.get(word) / total);
		}

		//Test
		float tota = 0.0f;
		for(float v : weights.values()) {
			tota += v;
		}
		return weightedRandomChoice(weights);
	}

	private static String weightedRandomChoice(Map<String, Float> wordCounts) {
		Random r = new Random();
		float rn = r.nextFloat();

		float current = 0.0f;
		for(String word : wordCounts.keySet()) {
			current += wordCounts.get(word);
			if(current > rn) {
				return word;
			}
		}
		return null;
	}

	public static <T> T deserializeFile(String fname, Class<T> classarg) throws ClassNotFoundException, IOException {
		FileInputStream file = new FileInputStream(fname);
		ObjectInputStream in = new ObjectInputStream(file);
		return classarg.cast(in.readObject());
	}

}
