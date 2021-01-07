import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Simple HelloWorld program (clear of Checkstyle and FindBugs warnings).
 *
 * @author Mohamed Mohamed
 */
public final class WordCounter {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private WordCounter() {
        // no code needed here
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param strSet
     *            the {@code Set} to be replaced
     * @replaces strSet
     * @ensures strSet = entries(str)
     */
    private static void generateElements(String str, Set<Character> strSet) {
        assert str != null : "Violation of: str is not null";
        assert strSet != null : "Violation of: strSet is not null";

        strSet.clear();
        char strPiece = 'i';
        int i = 0;
        while (i < str.length()) {
            strPiece = str.charAt(i);
            if (!strSet.contains(strPiece)) {
                strSet.add(strPiece);
            }
            i++;
        }

    }

    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        char piece = text.charAt(position);
        int i = 0;
        String result = "";
        if (separators.contains(text.charAt(position))) {

            while (i < text.substring(position).length()
                    && separators.contains(piece)) {
                piece = text.charAt(position + i);
                if (separators.contains(piece)) {
                    result = result + piece;
                }
                i++;
            }

        } else {

            while (i < text.substring(position).length()
                    && !(separators.contains(piece))) {
                piece = text.charAt(position + i);
                if (!separators.contains(piece)) {
                    result = result + piece;
                }
                i++;
            }

        }

        return result;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();
        SimpleReader in = new SimpleReader1L();
        /*
         * Define separator characters for test
         */
        out.println("Enter: ");
        final String separatorStr = " \t~!@#$%^&*()_+=-?><,";
        Set<Character> separatorSet = new Set1L<>();
        generateElements(separatorStr, separatorSet);
        /*
         * Open input and output streams
         */
        Sequence<String> s = new Sequence1L<>();
        String testStr = in.nextLine();
        int position = 0;

        int greatCount = 0;
        while (position < testStr.length()) {
            String token = nextWordOrSeparator(testStr, position, separatorSet);
            if (separatorSet.contains(token.charAt(0))) {
                out.print("  Separator: <");
            } else {

                out.print("  Word: <");

                for (int i = 0; i < s.length(); i++) {
                    if (token.equals(s.entry(i))) {
                        greatCount++;

                    } else {
                        s.add(0, token);
                        greatCount++;
                    }
                }
            }
            out.println(token + ">");

            position += token.length();
        }
        out.println(greatCount);
    }

}
