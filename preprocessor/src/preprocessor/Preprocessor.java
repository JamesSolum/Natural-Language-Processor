package preprocessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Preprocessor {

	public static void main(String[] args) {
		ArrayList<String> a = new ArrayList<String>();
		if(args.length < 1) {
			System.out.println("Please provide the path to a directory of txt file");
		} else {
			File f = new File(args[0]);
			if(f.isDirectory()) {
				for(File fil : f.listFiles()) {
					if(isTxtFile(fil)) {
						System.out.println("Checking File");
						a.addAll(Tokenizer.tokenize(fil));
					}
				}
				System.out.println("Done Getting files");
				try {
					if(args.length == 2)
						serializeList(a, args[1]);
					else {
						System.out.println("Serializing");
						serializeList(a);
						System.out.println("Done");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				if(!isTxtFile(f)) 
					System.out.println("Must provide a .txt file");
				else {
					a.addAll(Tokenizer.tokenize(f));
					try {
						if(args.length == 2)
							serializeList(a, args[1]);
						else
							serializeList(a);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static Boolean isTxtFile(File f) {
		String fileName = f.getName();
		if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".")+1).equals("txt");
		else 
			return false;
	}

	public static void serializeList(List<String> l, String name) throws IOException {
		System.out.print(Arrays.toString(l.toArray()));
		RandomAccessFile raf = new RandomAccessFile(name, "rw");
		FileOutputStream fout = new FileOutputStream(raf.getFD());
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(l);
		raf.close();
		fout.close();
	}

	public static void serializeList(List<String> l) throws IOException {
		serializeList(l, "processed");
	}

}