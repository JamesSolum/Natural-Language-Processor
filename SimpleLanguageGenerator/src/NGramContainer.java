import java.util.Iterator;
import java.util.LinkedList;

public class NGramContainer {

	LinkedList<String> gram;
	int size;

	private int counter;
	
	public NGramContainer(int n) {
		gram = new LinkedList<>();
		size = n;
	}
	
	public void add(String s) {
		gram.addLast(s);
		if(counter >= size) {
			gram.pollFirst();
		} else {
			counter++;
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> iter = gram.iterator();
		while(iter.hasNext()) {
			sb.append(iter.next());
			if(iter.hasNext()) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	

}
