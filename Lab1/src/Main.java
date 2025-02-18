import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti un sir de caractere: ");
        String input = scanner.nextLine();
        String sortedString = CustomStringSorting.customSort(input);
        System.out.println("Input: " + input);
        System.out.println("Output: " + sortedString);
    }
}
