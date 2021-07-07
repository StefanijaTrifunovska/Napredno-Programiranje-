import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TermFrequencyTest {
	public static void main(String[] args) throws FileNotFoundException {
		String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
				"ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
				"што", "на", "а", "но", "кој", "ја" };
		TermFrequency tf = new TermFrequency(System.in,
				stop);
		System.out.println(tf.countTotal());
		System.out.println(tf.countDistinct());
		System.out.println(tf.mostOften(10));
	}
} 
// vasiot kod ovde
class TermFrequency{
    Map<String,Integer> words ;
    int count;

    public TermFrequency(){
        words = new TreeMap<>();
        count = 0 ; 
    }

    public TermFrequency(InputStream input, String[] stop) {
    this();
    Scanner scanner = new Scanner(input);
    while(scanner.hasNext()){
        String next = scanner.next();
        //next = next.compareToIgnoreCase()
        next= next.toLowerCase().replace(',','\0').replace('.', '\0');
        if(!(next.isEmpty() || Arrays.asList(stop).contains(next))){
            int n = words.computeIfAbsent(next, key -> 0);
            words.put(next,++n);
            count++;
             }      
        }
    }
    
    public int countTotal(){
        return count;
    }
    
    public int countDistinct(){
        return words.size();
    }

    public List<String> mostOften(int k){
      
        return words.keySet().stream()
        .sorted(Comparator.comparing(key -> words.get(key)).reversed())
        .collect(Collectors.toList()).subList(0,k);
    }

}