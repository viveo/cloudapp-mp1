import java.io.*;
// import java.io.File;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
       
        // my code starts

        // Store all the tokens(words) in "token - frequency" pair
        HashMap<String, Integer> tokenRecord = new HashMap<String, Integer> ();

        try(BufferedReader inputFileReader = new BufferedReader(new FileReader(inputFileName))) {
            String curLine;
            String[] allTitles = new String[50000];
            int curNum = 0;

            // Read the lines (titles) of input file and save to an array of String
            while((curLine = inputFileReader.readLine()) != null) {
                    allTitles[curNum] = curLine;
                    curNum ++;
            }

            // Generate the "certain indexes"
            Integer[] certainIndexes= getIndexes();

            // Handle only the lines (titles) with "cetain indexes"
            // Notice: It is possible to have an index appear several times. 
            // Notice: In this case, process the index multiple times.
            for (int i = 0; i < 10000; i ++) {
                int curIndex = certainIndexes[i];

                // Divide each String (line/title) into a list of words using delimiters provided in the "delimiters" variable
                // Make the strings (thus tokens) lowercase
                StringTokenizer curStringTokenizer = new StringTokenizer(allTitles[curIndex].toLowerCase(), delimiters);
                
                while(curStringTokenizer.hasMoreTokens()) {
                    // Remove leading and trailing spaces
                    String curToken = curStringTokenizer.nextToken().trim();

                    // Ignore common words provided in the "stopWordsArray" variable
                    if (! (Arrays.asList(stopWordsArray).contains(curToken))) {
                        // If the token is a new one, add a new entry in the HashMap, initial frequency is set as 1
                        // If not, increment the frequency of the token by 1 
                        if (tokenRecord.containsKey(curToken)) {
                            tokenRecord.put(curToken, tokenRecord.get(curToken) + 1);
                        } else {
                            tokenRecord.put(curToken, 1);
                        }
                    }
                }
            }

            // Sort the "token - frequency" pairs by frequency in decending order
            TreeMap<String, Integer> sortedRecord = SortByValue(tokenRecord);
 
            // Get the top 20 items 
            Set curSet = sortedRecord.entrySet();
            Iterator curIterator = curSet.iterator();

            int k = 0;

            while ((curIterator.hasNext()) && (k < 20)) {
                Map.Entry curEntry = (Map.Entry)curIterator.next();
                ret[k] = curEntry.getKey().toString();
                k ++;
            }
        }

        // my code ends

        return ret;
    }

    public static TreeMap<String, Integer> SortByValue(HashMap<String, Integer> curHashMap) {
        ValueComparator vc = new ValueComparator(curHashMap);
        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(vc);
        sortedMap.putAll(curHashMap);
        return sortedMap;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}

// To compare/sort "token - frequency" pairs
class ValueComparator implements Comparator<String> {
    Map<String, Integer> map;
 
    public ValueComparator(Map<String, Integer> base) {
        this.map = base;
    }
 
    public int compare(String a, String b) {
        if (map.get(a) > map.get(b)) {
            return -1;
        } else if (map.get(a) < map.get(b)) {
            return 1;
        } else {
            if (a.compareTo(b) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}




