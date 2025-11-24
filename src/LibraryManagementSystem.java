//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

class Book {
    int bookId;
    String title;
    String author;
    int quantity;

    public Book(int bookId, String title, String author, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }

    void displayBook() {
        System.out.println("Book ID: " + bookId + ", Title: " + title + ", Author: " + author + ", Quantity: " + quantity);
    }
}

class Student {
    int studentId;
    String name;

    public Student(int studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    void displayStudent() {
        System.out.println("Student ID: " + studentId + ", Name: " + name);
    }
}

class IssueRecord {
    Book book;
    Student student;
    LocalDate issueDate;
    LocalDate returnDate;
    boolean isReturned;
    static final int FINE_PER_DAY = 10;

    public IssueRecord(Book book, Student student, LocalDate issueDate, LocalDate returnDate) {
        this.book = book;
        this.student = student;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.isReturned = false;
    }

    int calculateFine() {
        if (isReturned) return 0;
        long daysLate = ChronoUnit.DAYS.between(returnDate, LocalDate.now());
        if (daysLate > 0) {
            return (int)daysLate * FINE_PER_DAY;
        }
        return 0;
    }

    void displayRecord() {
        System.out.println("Book: " + book.title + " | Issued to: " + student.name +
                " | Issue Date: " + issueDate + " | Return Date: " + returnDate +
                " | Returned: " + isReturned + " | Fine: " + calculateFine());
    }
}

class Library {
    List<Book> books = new ArrayList<>();
    List<Student> students = new ArrayList<>();
    List<IssueRecord> issueRecords = new ArrayList<>();

    void addBook(Book book) {
        books.add(book);
    }

    void addStudent(Student student) {
        students.add(student);
    }

    void issueBook(int bookId, int studentId) {
        Book book = findBook(bookId);
        Student student = findStudent(studentId);
        if (book == null) {
            System.out.println("Book not found");
            return;
        }
        if (student == null) {
            System.out.println("Student not found");
            return;
        }
        if (book.quantity <= 0) {
            System.out.println("Book is not available");
            return;
        }
        LocalDate today = LocalDate.now();
        LocalDate returnDate = today.plusDays(14); // 2 weeks return time
        book.quantity--;
        IssueRecord record = new IssueRecord(book, student, today, returnDate);
        issueRecords.add(record);
        System.out.println("Book issued successfully");
    }

    void returnBook(int bookId, int studentId) {
        for (IssueRecord record : issueRecords) {
            if (!record.isReturned && record.book.bookId == bookId && record.student.studentId == studentId) {
                record.isReturned = true;
                record.book.quantity++;
                System.out.println("Book returned successfully");
                int fine = record.calculateFine();
                if (fine > 0) {
                    System.out.println("Fine to be paid: " + fine);
                }
                return;
            }
        }
        System.out.println("No matching issued record found");
    }

    Book findBook(int bookId) {
        for (Book b : books) {
            if (b.bookId == bookId)
                return b;
        }
        return null;
    }

    Student findStudent(int studentId) {
        for (Student s : students) {
            if (s.studentId == studentId)
                return s;
        }
        return null;
    }

    void displayAvailableBooks() {
        System.out.println("\nAvailable Books:");
        for (Book b : books) {
            if (b.quantity > 0) {
                b.displayBook();
            }
        }
    }

    void displayIssuedBooks() {
        System.out.println("\nIssued Books:");
        for (IssueRecord record : issueRecords) {
            if (!record.isReturned) {
                record.displayRecord();
            }
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library lib = new Library();

        lib.addBook(new Book(1, "Clean Code", "Robert C. Martin", 3));
        lib.addBook(new Book(2, "Effective Java", "Joshua Bloch", 2));

        lib.addStudent(new Student(101, "Charchit"));
        lib.addStudent(new Student(102, "Kalash"));

        lib.displayAvailableBooks();

        lib.issueBook(1, 101);
        lib.issueBook(2, 102);

        lib.displayAvailableBooks();
        lib.displayIssuedBooks();

        lib.returnBook(1, 101);

        lib.displayAvailableBooks();
        lib.displayIssuedBooks();
    }
}
