package Isptini;


import java.io.InputStream;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class TimeFormatter{
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
}
class Subtitles {
    List<String> subtitles;

    public Subtitles() {
        subtitles = new ArrayList<>();
    }

    public int loadSubtitles(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        int readSubtitles = 0;
        while (scanner.hasNextLine()) {
            StringBuilder sb = new StringBuilder();
            sb.append(scanner.nextLine()).append('\n');
            sb.append(scanner.nextLine()).append('\n');
            String finalLine;
            while (scanner.hasNextLine() && !(finalLine = scanner.nextLine()).isEmpty())
                sb.append(finalLine).append('\n');

            subtitles.add(sb.toString());
            readSubtitles++;
        }
        return readSubtitles;
    }
    public void print(){
        subtitles.forEach(System.out::println);
    }
    public void shift(int ms){
        subtitles = subtitles.stream().map(subtitle -> {
            String [] parts = subtitle.split("\n");
            StringBuilder sb = new StringBuilder();
            for(int i = 0 ; i < parts.length ; i ++){
                if(i == 1)
                    sb.append(shiftTime(parts[1],ms));
                else
                    sb.append(parts[i]);
                sb.append('\n');
            }
            return sb.toString();
        }).collect(Collectors.toList());

    }
    public String shiftTime(String subtitleTime, int ms) {
        String[] times = subtitleTime.split(" --> ");
        LocalTime start = LocalTime.parse(times[0], TimeFormatter.formatter);
        LocalTime end = LocalTime.parse(times[1], TimeFormatter.formatter);
        start = start.plusNanos(ms * 1000000);
        end = end.plusNanos(ms * 1000000);
        return String.format("%s --> %s", start.format(TimeFormatter.formatter), end.format(TimeFormatter.formatter));
    }
}

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}
