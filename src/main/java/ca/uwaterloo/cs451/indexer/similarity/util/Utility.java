package ca.uwaterloo.cs451.indexer.similarity.util;

import ca.uwaterloo.cs451.indexer.util.PostingsInfo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class Utility
{
    private static final String collection = "/Users/shakti/Desktop/University_of_Waterloo/Fall2023/CS651/Project/Info-Retrieval-Boot/src/main/resources/static/Shakespeare.txt";

    public static Set<Integer> fetchDocumentSet(String term, HashMap<String, PostingsInfo> ogMap) throws IOException {
        Set<Integer> set = new TreeSet<>();
        PostingsInfo info = ogMap.get(term);
        List<List<Integer>> list = info.getPostings();
        for (List<Integer> subPost : list) {
            set.add(subPost.get(0));
        }

        return set;
    }

    public static int getDocLen(String term, int docId, HashMap<String, PostingsInfo> ogMap) {
        PostingsInfo info = ogMap.get(term);
        List<List<Integer>> lst = info.getPostings();
        for(List<Integer> l: lst){
            if(l.get(0) == docId){
                return l.get(2);
            }
        }
        return -1;
    }

    public static int getTF(String term, int docId, HashMap<String, PostingsInfo> ogMap) {
        PostingsInfo info = ogMap.get(term);
        List<List<Integer>> lst = info.getPostings();
        for(List<Integer> l: lst){
            if(l.get(0) == docId){
                return l.get(1);
            }
        }
        return -1;
    }

    public static String getDoc(int offset)
    {
        String doc = "Empty doc";
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(collection, "r");
            randomAccessFile.seek(offset);

//            byte[] buffer = new byte[1024];
            doc = randomAccessFile.readLine();

            // Process the read data (you can convert it to a String or handle it as needed)
            // String data = new String(buffer, 0, bytesRead);
            System.out.println("Read data from offset " + offset + ": " + doc);

            // Close the file
            randomAccessFile.close();

            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return doc;
    }

    public static void helperCustomSort(List<Map.Entry<Integer, Double>> scoresList){
        scoresList.sort(new Comparator<Map.Entry<Integer, Double>>() {
            private static final double EPSILON = 1e-10; // Adjust the epsilon based on your requirements

            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                // Compare with epsilon to handle precision issues
                if (Math.abs(o1.getValue() - o2.getValue()) < EPSILON) {
                    return 0;
                } else if (o1.getValue() < o2.getValue()) {
                    return -1;
                } else {
                    return 1;
                }
            }

        });
    }
}
