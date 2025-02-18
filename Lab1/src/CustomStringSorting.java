public class CustomStringSorting {

    public static String customSort(String input) {
        StringBuilder lowercaseLetters = new StringBuilder();
        StringBuilder uppercaseLetters = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isLowerCase(c)) {
                lowercaseLetters.append(c);
            } else if (Character.isUpperCase(c)) {
                uppercaseLetters.append(c);
            }
        }
        return lowercaseLetters.toString() + uppercaseLetters.toString();
    }
}