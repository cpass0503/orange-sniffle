import java.util.Scanner;

public class Book {
	private int numPages;
	private int currentPage;
	
	public Book(int totalPages) {
		numPages = totalPages;
		currentPage = 1;
	}
	public int nextPage() {
		if (currentPage < numPages)
		{
			currentPage++;
		}
        return currentPage;
    }
    public static void main(String[]args) {
        Book Bookobject = new Book(3);
        
        for (int i = 0; i < 3; i++){
            System.out.println("Current Page: " + Bookobject.nextPage());
        }
    }
}