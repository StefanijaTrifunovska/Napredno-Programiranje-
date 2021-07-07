import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class IntegerList {
    private List<Integer> list;

    public IntegerList() {
        list = new ArrayList<>();
    }

    public IntegerList(Integer... numbers) {
        this();
        list.addAll(Arrays.asList(numbers));
    }

    public void add(int el, int idx) {
        while (idx > list.size()) {
            list.add(0);
        }
        list.add(idx, el);
    }

    public int remove(int idx) {

        return list.remove(idx);
    }

    public void set(int el, int idx) {
        list.set(idx, el);
    }

    public int get(int idx) {
        return list.get(idx);
    }

    public int size() {
        return list.size();
    }

    public int count(int el) {
        // int count = 0 ;
        // for(int element : list){
        // if(element == el){
        // count++;
        // }
        // }
        // return count;
        return (int) list.stream().filter(integer -> integer.equals(el)).count();
    }

    public void removeDuplicates() {
        // System.out.println("Sto se deshava tuka");
        Collections.reverse(list);
        this.list = this.list.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(list);
    }

    public int sumFirst(int k) {

        return this.list.stream().limit(k).mapToInt(i -> i).sum();

    }

    public int sumLast(int k) {

        return list.stream().skip(this.list.size() - k).mapToInt(i -> i).sum();
    }

    public void shiftRight(int idx, int k) {

        // Collections.rotate(list,k);
        // Collections.rotate(list,idx);

        int index = (idx + k) % list.size(); // modul za da moze da kruzi
        int number = list.remove(idx);
        list.add(index, number);

    }

    public void shiftLeft(int idx, int k) {
        // Collections.rotate(list,k);
        // Collections.rotate(list,idx);
        int size = list.size();
        int number = this.list.remove(idx);
        if (k >= size) {
            k -= size * (k / size);
        }
        int position = (idx + size - k) % size;
        list.add(position, number);
    }

    public IntegerList addValue(int value) {

        return new IntegerList(this.list.stream().map(i -> i + value).toArray(Integer[]::new));
    }

}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { // test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        list.remove(jin.nextInt());
                    }
                    if (num == 2) {
                        print(list);
                    }
                    if (num == 3) {
                        break;
                    }
                }
            }
            if (subtest == 1) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if (k == 1) { // test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { // count
                    System.out.println(list.count(jin.nextInt()));
                }
                if (num == 1) {
                    list.removeDuplicates();
                }
                if (num == 2) {
                    print(list.addValue(jin.nextInt()));
                }
                if (num == 3) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
        if (k == 2) { // test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { // count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if (num == 1) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0)
            System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0)
                System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}