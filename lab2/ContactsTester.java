import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

abstract class Contact {
    String date;
    int day;
    int month;
    int year;

    public Contact(String date) {
        this.date = date;
        // yyyy-MM-dd
        day = Integer.parseInt(date.substring(8, 10));
        month = Integer.parseInt(date.substring(5, 7));
        year = Integer.parseInt(date.substring(0, 4));
    }

    public boolean isNewerThan(Contact c) {
        if (year > c.year) {
            return true;
        }
        if (year < c.year) {
            return false;
        }
        if (month > c.month) {
            return true;
        }
        if (month < c.month) {
            return false;
        }
        if (day > c.day) {
            return true;
        }
        return false;

    }

    public abstract String getType();

}

class EmailContact extends Contact {
    String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public String getType() {
        return "Email";
    }
}

enum Operator {
    VIP, ONE, TMOBILE
};

class PhoneContact extends Contact {
    String phone;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getType() {
        return "Phone";
    }

    public Operator getOperator() {

        if (phone.charAt(2) == '2' || phone.charAt(2) == '1' || phone.charAt(2) == '0') {
            return Operator.TMOBILE;
        } else if (phone.charAt(2) == '5' || phone.charAt(2) == '6') {
            return Operator.ONE;
        } else {
            return Operator.VIP;
        }
    }
}

class Student {
    String firstName;
    String lastName;
    String city;
    int age;
    long index;
    int numberContacts;
    int phoneContacts;
    int emailContacts;
    Contact[] contacts;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        numberContacts = 0;
        phoneContacts = 0;
        emailContacts = 0;
        contacts = new Contact[0];
    }

    public void addEmailContact(String date, String email) {
        Contact tmp[] = new Contact[numberContacts + 1];

        for (int i = 0; i < numberContacts; i++) {
            tmp[i] = contacts[i];
        }
        tmp[numberContacts++] = new EmailContact(date, email);
        contacts = tmp;
        emailContacts++;
    }

    public void addPhoneContact(String date, String phone) {
        Contact tmp[] = new Contact[numberContacts + 1];
        for (int i = 0; i < numberContacts; i++) {
            tmp[i] = contacts[i];
        }
        tmp[numberContacts++] = new PhoneContact(date, phone);
        contacts = tmp;
        phoneContacts++;
    }

    public Contact[] getEmailContacts() {
        Contact nov[] = new Contact[emailContacts];
        int s = 0;
        for (Contact i : contacts) {
            if (i.getType().equals("Email")) {
                nov[s++] = i;

            }
        }
        return nov;
    }

    public Contact[] getPhoneContacts() {
        Contact nov[] = new Contact[phoneContacts];
        int s = 0;
        for (Contact i : contacts) {
            if (i.getType().equals("Phone")) {
                nov[s] = i;
                s++;
            }
        }
        return nov;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getCity() {
        return city;
    }

    public long getIndex() {
        return index;
    }

    public Contact getLatestContact() {
        int newest = 0;
        for (int i = 1; i < numberContacts; i++) {
            if (contacts[i].isNewerThan(contacts[newest])) {
                newest = i;
            }
        }
        return contacts[newest];
    }

    @Override
    public String toString() {

        String result = String.format("{" + "\"ime\":\"%s\", " + "\"prezime\":\"%s\", " + "\"vozrast\":%d, "
                + "\"grad\":\"%s\", " + "\"indeks\":%d, " + "\"telefonskiKontakti\":[", firstName, lastName, age, city,
                index);

        result += (getPhoneContacts().length != 0 ? "\"" + Arrays.stream(getPhoneContacts())
                .map(i -> ((PhoneContact) i).getPhone()).collect(Collectors.joining("\", \"")) + "\"" : "")
                + "], \"emailKontakti\":[";

        result += (getEmailContacts().length != 0 ? "\"" + Arrays.stream(getEmailContacts())
                .map(i -> ((EmailContact) i).getEmail()).collect(Collectors.joining("\", \"")) + "\"" : "") + "]}";

        return result;
    }

    public int totalContacts() {
        return getEmailContacts().length + getPhoneContacts().length;
    }
}

class Faculty {
    String name;
    Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }

    public int countStudentsFromCity(String cityName) {
        int count = 0;
        for (Student i : students) {
            if (i.getCity().equals(cityName)) {
                count++;
            }
        }
        return count;
    }

    public Student getStudent(long index) {
        return Arrays.stream(students).filter(student -> student.getIndex() == index).findFirst().get();
    }

    public double getAverageNumberOfContacts() {
        return Arrays.stream(students).mapToDouble(student -> student.totalContacts()).average().getAsDouble();
    }

    public Student getStudentWithMostContacts() {
        // int max = Arrays.stream(students)
        // .mapToInt(student -> student.totalContacts())
        // .max()
        // .getAsInt();
        // return Arrays.stream(students).filter(student -> student.totalContacts() ==
        // max).findFirst().get();
        //

        Student max = students[0];
        for (int i = 1; i < students.length; i++) {
            if (students[i].numberContacts > max.numberContacts) {
                max = students[i];
            } else if (students[i].numberContacts == max.numberContacts) {
                if (students[i].getIndex() > max.getIndex()) {
                    max = students[i];
                }
            }
        }
        return max;
    }

    @Override
    public String toString() {

        String result = String.format("{\"fakultet\":\"%s\", \"studenti\":[", name);
        result += Arrays.stream(students).map(Student::toString).collect(Collectors.joining(", ")) + "]}";
        return result;

    }

}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city, age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out
                            .println("Average number of contacts: " + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": " + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex).getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact).getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact).getPhone() + " ("
                                + ((PhoneContact) latestContact).getOperator().toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out.println(faculty.getStudent(rindex).getEmailContacts().length + " "
                                + faculty.getStudent(rindex).getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex).getEmailContacts()[posEmail]
                                .isNewerThan(faculty.getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out
                            .println("Student with most contacts: " + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
