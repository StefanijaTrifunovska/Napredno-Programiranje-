import java.util.stream.IntStream;
import java.util.Scanner;



public class RomanConverterTest
{
public static void main (String [] args){
    Scanner scanner = new Scanner(System.in);
    int n = scanner.nextInt();
    IntStream.range(0, n)
    .forEach(x-> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
    scanner.close();

}

}
class RomanConverter
{
public static String toRoman(int n )
{
    StringBuilder sb = new StringBuilder();
    int povtoruvanja = 0 ;
    String niza [] = new String [] {"I","IV","V","IX","X","XL","L","XC","C","CD","D","M"};
    int [] brojki = new int [] {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000 }; 
    for(int i = brojki.length -1  ; i >= 0 ; i --){
    povtoruvanja = n / brojki[i];
    n%=brojki[i];
    while(povtoruvanja>0){
        sb.append(niza[i]);
        povtoruvanja--;
    }
    }
    return sb.toString();
}
}