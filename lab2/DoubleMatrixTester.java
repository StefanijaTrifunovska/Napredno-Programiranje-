import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.*;


class InsufficientElementsException extends Exception{

    @Override
    public String getMessage() {
        return "Insufficient number of elements";
        }

}
class InvalidRowNumberException extends Exception {

    public InvalidRowNumberException(){};
    @Override 
    public String getMessage(){
        return String.format("Invalid row number");
    }
}

class InvalidColumnNumberException extends Exception {

    public InvalidColumnNumberException(){};
    
    @Override 
    public String getMessage(){
        return String.format("Invalid column number");
    }
}

final class DoubleMatrix{

    private double matrix [][];

    DoubleMatrix(double a [] , int m, int n ) throws InsufficientElementsException{
        if(a.length < m*n)
         throw new InsufficientElementsException();

         matrix = new double [m][n];

         int s = a.length -1 ; 
         
         for(int i = m - 1 ; i >= 0 ; i --){
             for(int j = n - 1 ; j >= 0 ; j --){
                    matrix[i][j]=a[s--];
             }
         }

    }

   public String getDimensions(){

        return String.format("[%d x %d]" , matrix.length , matrix[0].length); 
    }

    public int rows(){

        return matrix.length;
    }
    public int columns (){
        return matrix[0].length;
    }


    public double maxElementAtRow(int row) throws InvalidRowNumberException{
          row -= 1;
       if(row < 0 || row >= matrix.length) throw new  InvalidRowNumberException();
    
    return Arrays.stream(matrix[row]).max().getAsDouble();

}

    public double maxElementAtColumn(int column) throws InvalidColumnNumberException{

        column -= 1;
        if(column < 0 || column >= matrix[0].length){
            throw new InvalidColumnNumberException ();
        }   
    final int finalCol = column;

    return Arrays.stream(matrix)
            .flatMapToDouble(row->
            IntStream.range(0,row.length)
            .filter(i-> i == finalCol)
            .mapToDouble(i-> row[i])).max().getAsDouble();

    
    }
    public double sum(){

        return Arrays.stream(matrix)
                .mapToDouble(row -> Arrays.stream(row).sum())
                .sum(); 
    }

    public double [] toSortedArray(){

        int n = matrix.length;
        int m = matrix[0].length;
        double result[] = new double[n * m] ; 
        int s = 0;
        for(int i = 0 ; i < n ; i ++){
            for(int j = 0 ; j < m ; j ++){
               result[s++] = matrix[i][j];
            }
        }
    
        for(int i = 0 ; i <result.length - 1 ; i ++){
            for(int j = 0 ; j < result.length - i - 1 ; j ++){
                if(result[j] < result[j+1]){
                    double tmp = result[j];
                    result[j] = result[j+1];
                    result[j+1] = tmp;
                }
            }
        }
        return result;
       // return Arrays.stream(matrix)
       //.flatMapToDouble(row->Array.stream(row))
      //.boxed()
      //.sorted(Comperator.reverseOrder())
      //.mapToDouble(i-> i.doubleValue())
      //.toArray();
    }

    @Override
    public String toString() {
     StringBuffer sb = new StringBuffer();
     int n = matrix.length;
     int m = matrix[0].length;
     for(int i = 0 ; i < n ; i ++){
         for(int j = 0 ; j < m ; j ++){
             sb.append(String.format("%.2f\t",matrix[i][j]));
      }
      sb.setLength(sb.length()-1);
      sb.append("\n");
     } 
     sb.setLength(sb.length()-1);
     return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
            if(this == o){return true;}
            if(o == null){return false;}

            DoubleMatrix other = (DoubleMatrix) o;
            if(!Arrays.deepEquals(matrix,other.matrix)){return false;}
            return true;
    
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = result * prime + Arrays.deepHashCode(matrix);
        return result;
    }

}

class MatrixReader {

    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException {
      
        Scanner scanner = new Scanner (input);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int len = m * n;
        double niza [] = new double [len];
        IntStream.range(0, len).forEach(i -> niza[i] = scanner.nextDouble());
        return new DoubleMatrix(niza, n, m);
    }
}


public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}
