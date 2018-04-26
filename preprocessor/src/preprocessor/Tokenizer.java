package preprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tokenizer {
	
	public Tokenizer() {}

	public static List<String> tokenize(File input){
		ArrayList<String> tokens = new ArrayList<String>();
		Scanner in = null;
		try {
			in = new Scanner(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(in.hasNext()) {
			String token = in.next();
			token = token.replaceAll("\"", ""); // Removes quotes
			token = token.replaceAll("(?<=\\S)(?:(?<=\\p{Punct})|(?=\\p{Punct}))(?=\\S)", " ");
			token = token.replaceAll("(?<=\\S)(?:(?<=\\p{Punct})|(?=\\p{Punct}))(?=\\S)", " ");
			token = token.replaceAll("[\\[\\](){}*]",""); //removes parentheses and brackets
			token = token.replaceAll("[-]*", ""); //remove dashes
			String[] strings = tokenCheck(token);
			for(String stringToken: strings) {
				if(!token.isEmpty() || !token.equals(" "))
				tokens.add(stringToken);
			}
		}
		in.close();
		return tokens;
	}
	
	public static String[] tokenCheck(String token) {
		return token.split(" ");
	}

}
