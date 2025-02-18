public class ValleyCounter {

    public static int countValleys(String path) {
        int level = 0;
        int valleys = 0;
        for (char step : path.toCharArray()) {
            if (step == 'U') {
                level++;
                if (level == 0) {
                    valleys++;
                }
            } else if (step == 'D') {
                level--;
            }
        }
        return valleys;
    }
}