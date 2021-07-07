package Isptini;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

class DuplicateNumberException extends Exception{
    public DuplicateNumberException(String number){
        super(String.format("Duplicate number: %s",number));
    }
}

class Contact implements Comparable<Contact>{
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("%s %s",name,number);
    }

    @Override
    public int compareTo(Contact other) {
        if(this.name.contains(other.name)){
            return this.number.compareTo(other.number);
        }
        return this.name.compareTo(other.name);
    }
}
class PhoneBook{
    Set <Contact> contacts;
    public PhoneBook(){
        contacts = new TreeSet<>();
    }
    public void addContact(String name, String number) throws DuplicateNumberException {
        Contact newContact = new Contact(name,number);
        if(contacts.contains(newContact))
            throw new DuplicateNumberException(number);
                  contacts.add(newContact);
    }
    public void contactsByNumber(String number){
        AtomicBoolean flag = new AtomicBoolean(true);
        contacts.stream().filter(contact -> contact.number.contains(number)).forEach(contact -> {
            System.out.println(contact);
            flag.set(false);
        });


        if(flag.get())
            System.out.println("NOT FOUND");

    }
    public void contactsByName(String name){
        AtomicBoolean flag = new AtomicBoolean(true);
        contacts.stream().filter(contact -> contact.name.contains(name)).forEach(contact -> {
            System.out.println(contact);
            flag.set(false);
        });

        if(flag.get())
            System.out.println("NOT FOUND");

    }
}
public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде

