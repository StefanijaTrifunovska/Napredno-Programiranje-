import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(String message) {
        super(message);
    }
}

class Proizvod {
    int price;
    String tax;

    public Proizvod(int price, String tax) {
        this.price = price;
        this.tax = tax;
    }

    public int getPrice() {
        return price;
    }

    public double Danok() {
        if (tax.equals("A")) {
            return price * 0.18 * 0.15;
        } else if (tax.equals("B")) {
            return price * 0.05 * 0.15;
        } else
            return 0;
    }

    public String getTax() {
        return tax;
    }

}

class FiskalnaSmetka implements Comparable<FiskalnaSmetka> {
    String id;
    List<Proizvod> proizvod;

    public FiskalnaSmetka() {
        proizvod = new ArrayList<>();
    }

    public FiskalnaSmetka(String id, List<Proizvod> proizvod) throws AmountNotAllowedException {
        this.id = id;
        this.proizvod = proizvod;
        if (vkupnoSkeniraniSmetki() > 30000)
            throw new AmountNotAllowedException(
                    "Receipt with amount " + vkupnoSkeniraniSmetki() + " is not allowed to be scanned");
    }

    public double presmetajDanok() {
        return proizvod.stream().mapToDouble(Proizvod::Danok).sum();

        // double ddv = 0.0;
        // for (int i = 0 ; i < proizvod.size(); i++){
        // String tax = proizvod.get(i).tax;
        // int price = proizvod.get(i).price;
        // if(tax.equals( "A")){
        // ddv+= price*0.1800111;
        // }else if(tax.equals("B")){
        // ddv += price*0.050001;
        // }
        // else if(tax.equals("V"))
        // ddv+= price * 0 ;
        // }
        // return ddv* 0.15;
    }

    public int vkupnoSkeniraniSmetki() {
        return proizvod.stream().mapToInt(Proizvod::getPrice).sum();
    }

    @Override
    // сите информации се одвоени со таб
    // id-то i сумата на фискалната сметка се печатат со 10 места
    // повратокот на ДДВ со 10 места, од кои 5 се за цифрите после децималата.
    // 70876 20538 53.41500
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f", id, vkupnoSkeniraniSmetki(), presmetajDanok());
    }

    @Override
    public int compareTo(FiskalnaSmetka other) {

        return Integer.compare(this.vkupnoSkeniraniSmetki(), other.vkupnoSkeniraniSmetki());
    }
}

class MojDDV {

    List<FiskalnaSmetka> smetka;

    public MojDDV() {
        smetka = new ArrayList<>();
    }

    public void readRecords(InputStream in) {
        Scanner sc = new Scanner(in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String id = parts[0];
            List<Proizvod> proizvod = new ArrayList<>();
            for (int i = 1; i < parts.length; i += 2) {
                int price = Integer.parseInt(parts[i]);
                String type = parts[i + 1];
                proizvod.add(new Proizvod(price, type));

            }

            try {
                FiskalnaSmetka s = new FiskalnaSmetka(id, proizvod);
                smetka.add(s);
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void printTaxReturns(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        smetka.forEach(pw::println);

        pw.flush();
    }

    public void printStatistics(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);

        double min = smetka.stream().mapToDouble(FiskalnaSmetka::presmetajDanok).min().getAsDouble();
        double max = smetka.stream().mapToDouble(FiskalnaSmetka::presmetajDanok).max().getAsDouble();
        double sum = smetka.stream().mapToDouble(FiskalnaSmetka::presmetajDanok).sum();
        // long count = smetka.stream().count();
        double average = smetka.stream().mapToDouble(FiskalnaSmetka::presmetajDanok).average().getAsDouble();

        // min: 29.225
        // max: 145.509
        // sum: 726.325
        // count: 9
        // avg: 80.703
        pw.println(String.format("min:\t%2.3f\nmax:\t%3.3f\nsum:\t%3.3f\ncount:\t%-5d\navg:\t%2.3f", min, max, sum,
                smetka.size(), average));
        pw.flush();
    }

}

public class MojDDVTest1 {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}