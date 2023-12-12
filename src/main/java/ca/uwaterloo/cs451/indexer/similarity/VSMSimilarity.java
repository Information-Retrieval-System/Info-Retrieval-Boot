package ca.uwaterloo.cs451.indexer.similarity;

import ca.uwaterloo.cs451.indexer.similarity.util.Utility;
import ca.uwaterloo.cs451.indexer.util.PostingsInfo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class VSMSimilarity extends BaseSimilarity{

    HashMap<String, PostingsInfo> ogMap;
    private final String collection = "/Users/shakti/Desktop/University_of_Waterloo/Fall2023/CS651/Project/Info-Retrieval-Boot/src/main/resources/static/collection.txt";

    public VSMSimilarity(SimilarityType type) {
        super(type);
    }


    //returns IDF for a term
    // numQ -  number of documents containing q_i = n(qi)
    public double idf(int totalDocs, int numQ) {
        return Math.log((double) totalDocs / numQ);
    }


    // Given Query q_1 to q_n, the BM score of document is returned
//    private Set<Integer> fetchDocumentSet(String term) throws IOException {
//        Set<Integer> set = new TreeSet<>();
//
//        for (PairOfInts pair : fetchPostings(term)) {
//            set.add(pair.getLeftElement());
//        }
//
//        return set;
//    }

    private Set<Integer> fetchDocumentSet(String term) throws IOException {
        Set<Integer> set = new TreeSet<>();
        PostingsInfo info = this.ogMap.get(term);
        List<List<Integer>> list = info.getPostings();
        for (List<Integer> subPost : list) {
            set.add(Integer.valueOf(subPost.get(0)));
        }

        return set;
    }

//    private ArrayListWritable<PairOfInts> fetchPostings(String term) throws IOException {
//        Text key = new Text();
//        PairOfWritables<IntWritable, ArrayListWritable<PairOfInts>> value =
//                new PairOfWritables<>();
//
//        key.set(term);
////    index.get(key, value);
//        // get  from database
//
//        return value.getRightElement();
//    }

    public HashMap<Integer, Double> getVSMScores() throws Exception {
        //String[] queryArray = query.split("\\s+");
//    List<String> queryList = new ArrayList<>();
//        double score = 0.0;
//        int totalDocs = 0;
//        int numQ = 0;
//        for (String q : queryArray) {
//            int tf = 0; // get from DB
//            Set<Integer> docSet =  fetchDocumentSet(q);
//            double idf = idf(totalDocs,numQ);
//
//        }
//        return score;

        Set<String> terms = this.ogMap.keySet();
        Set<Integer> docSet = new HashSet<>();

        double score = 0.0;
        int totalDocs = 122458;
        //int docFreq = 0;

        //double avgDocLength = 0; //get from DB
        HashMap<Integer, Double> docScore = new HashMap<Integer, Double>();

        for (String term : terms) {
            docSet.addAll(Utility.fetchDocumentSet(term, this.ogMap));
        }

        HashMap<Integer, String> docIDTerm = new HashMap<>();
        for (String term : terms) {
            int docFreq = Integer.parseInt(this.ogMap.get(term).getDf());
            double idf = idf(totalDocs, docFreq);
            for (int doc : docSet) {

                //int tf = 0; // get from DB
                int tf = Utility.getTF(term, doc, this.ogMap);
                if (tf == -1) continue;

                // int docLength = 0; // get from DB
                int docLength = Utility.getDocLen(term, doc, this.ogMap);
                if (docLength == -1) continue;
                docIDTerm.put(doc, term);

                //score = ithScore(tf, idf, docLength, avgDocLength, k1, b);
                score = tf * idf;

                if (docScore.containsKey(doc)) {
                    // If present, update the score
                    double existingScore = docScore.get(doc);
                    docScore.put(doc, existingScore + score);
                } else {
                    // If not present, add the docID and score
                    docScore.put(doc, score);
                }
            }
        }

        // divide by sqroot of docLen
        for (int key : docScore.keySet()) {
            String tm = docIDTerm.get(key);
            int docLength = Utility.getDocLen(tm, key, this.ogMap);
            docScore.put(key, (docScore.get(key) / Math.sqrt(docLength)));
        }
        return docScore;
    }

    public List<String> getRelevantDocs(HashMap<String, PostingsInfo> map, int topK) throws Exception {
        this.ogMap = map;
        HashMap<Integer, Double> scores = this.getVSMScores();
//        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(topK);
//        Set<Integer> keys = scores.keySet();
//        for (int key : keys) {
//            queue.add(key);
//            if (queue.size() > topK) {
//                queue.remove(key);
//            }
//        }
//
//        //get the docs from id
//        List<String> response = new ArrayList<>();
//        while (!queue.isEmpty()) {
//            int offset = queue.poll();
//            String document = Utility.getDoc(offset);
//            response.add(document);
//        }
        List<Map.Entry<Integer,Double>> scoresList = new ArrayList<>(scores.entrySet());
        //scoresList.sort((a, b) -> (int) (b.getValue() -a.getValue()));
        Utility.helperCustomSort(scoresList);
//        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(topK);
//        Set<Integer> keys = scores.keySet();
//        for (int key : keys) {
//            queue.add(key);
//            if (queue.size() > topK) {
//                queue.remove(key);
//            }
//        }

        //Just take topk now from the sorted scoresList
        List<String> response = new ArrayList<>();
        for(int i=0; i < topK && i < scoresList.size(); i++){
            int dID = scoresList.get(i).getKey();
            String document = Utility.getDoc(dID);
            response.add(document);
        }

        return response;
    }

//    public String getDoc(int offset) {
//        String doc = "Empty doc";
//        try {
//            RandomAccessFile randomAccessFile = new RandomAccessFile(collection, "r");
//            randomAccessFile.seek(offset);
//
////            byte[] buffer = new byte[1024];
//            doc = randomAccessFile.readLine();
//
//            // Process the read data (you can convert it to a String or handle it as needed)
//            // String data = new String(buffer, 0, bytesRead);
//            System.out.println("Read data from offset " + offset + ": " + doc);
//
//            // Close the file
//            randomAccessFile.close();
//
//            return doc;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return doc;
//    }

}

