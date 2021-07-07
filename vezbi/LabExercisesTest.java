package Isptini;
import java.util.*;
import java.util.stream.Collectors;

class Student{
    String index;
    List<Integer> points;
    double averagePoints;
    int year;
    public Student(){
        this.index = "181562";
        this.points = new ArrayList<>();
    }

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
        averagePoints = points.stream().mapToInt(integer->integer).sum() / 10.00;
        year = Math.abs(Character.getNumericValue(index.charAt(1))-10);
    }

    public String getIndex() {
        return index;
    }

    public double getAveragePoints() {
        return averagePoints;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(index).append(" ");
        if(points.size() > 7){
            sb.append("YES ");
        }
        else
            sb.append("NO ");
        sb.append(String.format("%.2f",averagePoints));
        return sb.toString();
    }
}
class LabExercises {
    Collection <Student> students;

    public LabExercises(){
        students = new ArrayList<>();
    }
    public void addStudent (Student student){
        students.add(student);
    }
    public void printByAveragePoints(boolean ascending, int n){
        if (ascending == true){
            students.stream()
                    .sorted(Comparator.comparing(Student::getAveragePoints)
                            .thenComparing(Student::getIndex))
                    .limit(n)
                    .forEach(System.out::println);
        }else
            students.stream()
                    .sorted(Comparator.comparing(Student::getAveragePoints)
                            .thenComparing(Student::getIndex)
                            .reversed())
                    .limit(n)
                    .forEach(System.out::println);
    }
    public List<Student> failedStudents (){
       return students
               .stream()
               .filter(student -> student.points.size() < 8)
               .sorted(Comparator.comparing(Student::getIndex)
                       .thenComparing(Student::getAveragePoints))
               .collect(Collectors.toList());
    }
    public Map<Integer,Double> getStatisticsByYear(){
        Set<Map.Entry<Integer,Set<Student>>> tmp = students.stream()
                .filter(student -> !failedStudents().contains(student))
                .collect(Collectors.groupingBy(student -> student.year,Collectors.toSet())).entrySet();
        Map<Integer,Double> map = new TreeMap<>();
        for(Map.Entry<Integer,Set<Student>> key : tmp){
            map.put(key.getKey(),key.getValue()
                    .stream()
                    .mapToDouble(student->student.averagePoints).average()
                    .orElse(0));

        }
    return map;
    }

}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}