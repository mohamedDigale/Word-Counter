import java.util.Comparator;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
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
public final class ProjectWordCounter {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private ProjectWordCounter() {
        // no code needed here
    }

    /**
     * Arranges strings in alphabetical order
     */
    private static class alphabetize implements Comparator<String> {
        @Override

        public int compare(String word1, String word2) {
            return word1.toLowerCase().compareTo(word2.toLowerCase());
        }
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

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        char piece = text.charAt(position);
        String pieceStr = "" + piece;
        int i = 0;
        String result = "";
        if (separators.contains(text.charAt(position))) {

            while (i < text.substring(position).length()
                    && separators.contains(piece)) {
                piece = text.charAt(position + i);
                pieceStr = "" + piece;
                if (separators.contains(piece)) {
                    result = result.concat(pieceStr);
                }
                i++;
            }

        } else {

            while (i < text.substring(position).length()
                    && !(separators.contains(piece))) {
                piece = text.charAt(position + i);
                pieceStr = "" + piece;
                if (!separators.contains(piece)) {
                    result = result.concat(pieceStr);
                }
                i++;
            }

        }

        return result;
    }

    /**
     * generate words from input file and puts them into Qwords
     *
     * @param Queue
     *            words from input file
     * @updates Qwords
     * @requires <pre>
     * input.is_open
     *
     * </pre>
     * @ensures <pre>
     * input.is_open and
     * Qwords has all the words and does have word separators
     * </pre>
     */
    private static Queue<String> wordprocessor(SimpleReader file,
            Queue<String> Qwords, Set<Character> separatorSet) {

        while (!file.atEOS()) {

            String line = file.nextLine();
            int position = 0;

            while (position < line.length()) {

                String token = nextWordOrSeparator(line, position,
                        separatorSet);

                if (!separatorSet.contains(token.charAt(0))) {
                    Qwords.enqueue(token);
                }

                position += token.length();
            }

        }

        return Qwords;
    }

    /**
     * finds words from MAP and prints the on the IndexPage
     *
     * @param Map
     *            words from the input map
     * @updates Qwords
     * @requires <pre>
     * input.is_open and
     *
     * </pre>
     * @ensures <pre>
     * record={@code Map}'s words and occurrences
     * and
     * print out into html format]
     * </pre>
     */
    public static void wordSort(Queue<String> Qwords,
            Map<String, Integer> record, Comparator<String> sorter,
            SimpleWriter out) {

        while (Qwords.length() != 0) {

            String word = Qwords.dequeue();

            if (!record.hasKey(word)) {

                record.add(word, 1);
            } else {
                int tempCount = record.value(word);
                tempCount++;
                record.replaceValue(word, tempCount);
            }
        }

        
        Map<String, Integer> tempMap = record.newInstance();
        Queue<String> tempWords = Qwords.newInstance();

        while (record.iterator().hasNext()) {
            Pair<String, Integer> tempPair = record.removeAny();
            tempMap.add(tempPair.key(), tempPair.value());
            tempWords.enqueue(tempPair.key());

        }

        record.clear();

        tempWords.sort(sorter);
        while (tempWords.iterator().hasNext()) {
            String word = tempWords.dequeue();
            Pair<String, Integer> orderedDictionary = tempMap.remove(word);
            out.println("<tr>");
            out.println("<td>" + orderedDictionary.key() + "</td>");
            out.println("<td>" + orderedDictionary.value() + "</td>");
            out.println("</tr>");
        }
    }

    /**
     * Outputs the "opening" tags in the generated HTML file. These are the
     * expected elements generated by this method:
     *
     * <html> <head> <title>Words Counted in data/gettysburg.txt</title> </head>
     * <body>
     * <h2>Words Counter</h2>
     * <hr />
     * <ul>
     *
     *
     * @param out
     *            the output stream
     * @updates out.content
     * @requires out.is_open
     * @ensures out.content = #out.content * [the HTML "opening" tags]
     */
    private static void outputHeader(SimpleWriter out) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Words Counted from your file</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Words Counter</h2>");
        out.println("   <hr/>");
        out.println("<table border=\"1\">");
        out.println("<tr>");
        out.println("<th>Words</th>");
        out.println("<th>Counts</th>");
        out.println("</tr>");

    }

    /**
     * Outputs the "closing" tags in the generated HTML file. These are the
     * expected elements generated by this method:
     *
     * </table>
     * </body> </html>
     *
     * @param out
     *            the output stream
     * @updates out.contents
     * @requires out.is_open
     * @ensures out.content = #out.content * [the HTML "closing" tags]
     */
    private static void outputFooter(SimpleWriter out) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";

        out.print("</table>");
        out.print("</body> </html>");
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        out.println("Enter the input file:");
        SimpleReader file = new SimpleReader1L(in.nextLine());
        out.println("Enter the output folder");
        SimpleWriter indexPage = new SimpleWriter1L(in.nextLine());

        
        
        final String separatorStr = " \t, -- .";
        Set<Character> separatorSet = new Set1L<>();
        generateElements(separatorStr, separatorSet);

        Map<String, Integer> myMap = new Map1L<>();
        Queue<String> Qwords = new Queue1L<>();

        wordprocessor(file, Qwords, separatorSet);

        //arrange words in alphabetical order
        Comparator<String> sorter = new alphabetize();
        Qwords.sort(sorter);

        outputHeader(indexPage);
        wordSort(Qwords, myMap, sorter, indexPage);
        outputFooter(indexPage);

        file.close();
        in.close();
        out.close();
        indexPage.close();
    }

}
