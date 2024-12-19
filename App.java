import java.io.*;
import java.util.*;

// Book class
class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return isbn;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}

// Patron class
class Patron {
    private String name;
    private int id;
    private List<Book> borrowedBooks;

    public Patron(String name, int id) {
        this.name = name;
        this.id = id;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    @Override
    public String toString() {
        return "Patron{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", borrowedBooks=" + borrowedBooks +
                '}';
    }
}

// Library class
class Library {
    private List<Book> books;
    private List<Patron> patrons;

    public Library() {
        this.books = new ArrayList<>();
        this.patrons = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void registerPatron(Patron patron) {
        patrons.add(patron);
    }

    public boolean borrowBook(String isbn, int patronId) {
        Book book = findBookByISBN(isbn);
        Patron patron = findPatronById(patronId);

        if (book != null && patron != null && book.isAvailable()) {
            book.setAvailable(false);
            patron.borrowBook(book);
            return true;
        }
        return false;
    }

    public boolean returnBook(String isbn, int patronId) {
        Patron patron = findPatronById(patronId);

        if (patron != null) {
            for (Book book : patron.getBorrowedBooks()) {
                if (book.getISBN().equals(isbn)) {
                    book.setAvailable(true);
                    patron.returnBook(book);
                    return true;
                }
            }
        }
        return false;
    }

    private Book findBookByISBN(String isbn) {
        for (Book book : books) {
            if (book.getISBN().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    private Patron findPatronById(int id) {
        for (Patron patron : patrons) {
            if (patron.getId() == id) {
                return patron;
            }
        }
        return null;
    }

    public void displayBooks() {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void displayPatrons() {
        for (Patron patron : patrons) {
            System.out.println(patron);
        }
    }

    public void importBooksFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 3) {
                    addBook(new Book(details[0].trim(), details[1].trim(), details[2].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public void exportBooksToFile(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                bw.write(book.getTitle() + "," + book.getAuthor() + "," + book.getISBN());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}

// App class
public class App {
    public static void main(String[] args) {
        Library library = new Library();

        // Add books
        library.addBook(new Book("The Hobbit", "J.R.R. Tolkien", "123456789"));
        library.addBook(new Book("1984", "George Orwell", "987654321"));

        // Register patrons
        library.registerPatron(new Patron("Alice", 1));
        library.registerPatron(new Patron("Bob", 2));

        // Borrow and return operations
        System.out.println("Borrowing book (The Hobbit): " + library.borrowBook("123456789", 1)); // true
        System.out.println("Borrowing book (1984): " + library.borrowBook("987654321", 2)); // true

        System.out.println("Returning book (The Hobbit): " + library.returnBook("123456789", 1)); // true

        // Display books and patrons
        System.out.println("Books in library:");
        library.displayBooks();

        System.out.println("Patrons:");
        library.displayPatrons();

        // Import and export operations
        System.out.println("Importing books from file...");
        library.importBooksFromFile("books.csv");

        System.out.println("Books after import:");
        library.displayBooks();

        System.out.println("Exporting books to file...");
        library.exportBooksToFile("exported_books.csv");
        System.out.println("Books exported successfully.");
    }
}

