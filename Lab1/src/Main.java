import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Exercitiul 1: Custom String Sorting ---");
        System.out.print("Introduceti un sir de caractere: ");
        String input = scanner.nextLine();
        String sortedString = CustomStringSorting.customSort(input);
        System.out.println("Input: " + input);
        System.out.println("Output: " + sortedString);

        System.out.println("\n--- Exercitiul 2: Friendly Numbers ---");
        System.out.print("Introduceti primul numar: ");
        int num1 = scanner.nextInt();
        System.out.print("Introduceti al doilea numar: ");
        int num2 = scanner.nextInt();
        if (FriendlyNumbers.areFriendly(num1, num2)) {
            System.out.println(num1 + " si " + num2 + " sunt numere prietenoase.");
        } else {
            System.out.println(num1 + " si " + num2 + " NU sunt numere prietenoase.");
        }

        System.out.println("\n--- Exercitiul 3: Hex to Decimal Converter ---");
        System.out.print("Introduceti un numar hexazecimal: ");
        scanner.nextLine();
        String hexInput = scanner.nextLine();
        int decimalOutput = HexToDecimalConverter.hexToDecimal(hexInput);
        System.out.println("Input (Hex): " + hexInput);
        System.out.println("Output (Decimal): " + decimalOutput);

        System.out.println("\n--- Exercitiul 4: Valley Counter ---");
        System.out.print("Introduceti un sir de pasi (U sau D): ");
        String path = scanner.nextLine();
        int valleys = ValleyCounter.countValleys(path);
        System.out.println("Numarul de vai: " + valleys);
    }
}