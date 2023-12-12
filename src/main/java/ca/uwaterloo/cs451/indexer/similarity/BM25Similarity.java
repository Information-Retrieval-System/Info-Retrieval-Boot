package ca.uwaterloo.cs451.indexer.similarity;


import ca.uwaterloo.cs451.indexer.similarity.util.Utility;
import ca.uwaterloo.cs451.indexer.util.PostingsInfo;
import ca.uwaterloo.cs451.indexer.exceptions.ValueNotFoundException;
import ca.uwaterloo.cs451.indexer.similarity.util.Utility.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;


//import tl.lin.data.array.ArrayListWritable;
//import tl.lin.data.pair.PairOfInts;
//import tl.lin.data.pair.PairOfWritables;


public class BM25Similarity extends BaseSimilarity {

    private final double k1 = 1.5;
    private final double b = 0.75;

    private final double avgDocLength = 10.0;

    private final int totalDocs = 122458;

//    private final String collection = "/Users/shakti/Desktop/University_of_Waterloo/Fall2023/CS651/Project/Info-Retrieval-Boot/src/main/resources/static/collection.txt";

     HashMap<String, PostingsInfo> ogMap;
    private Stack<Set<Integer>> stack;

    public BM25Similarity(SimilarityType type) {
        super(type);
    }

    //returns IDF for a term
    // docFreq -  number of documents containing q_i = n(qi) ---- same as df
    public double idf(int totalDocs, int docFreq) {
        double num = totalDocs - docFreq + 0.5;
        double denom = docFreq + 0.5;

        return Math.log((num / denom) + 1);
    }

    //returns ith value of bm25 score (to be summed in total later)
    //Parameters:
    //  fqd = calculates, f(q_i, D) the number of times that q_i occurs in the document D (also tf)
    //  idf - IDF value of q_i
    //  docLength - length of doc D in words
    //  avgDocLength - avg word length of Docs
    public double ithScore(int fqd, double idf, int docLength, double avgDocLength, double k1, double b) throws Exception {
        double numerator = fqd * (k1 + 1);
        double docDiv = docLength / avgDocLength;
        double denominator = fqd + (k1 * (1 - b + (b * docDiv)));
        return idf * (numerator / denominator);
    }

//    private Set<Integer> fetchDocumentSet(String term) throws IOException {
//        Set<Integer> set = new TreeSet<>();
//        PostingsInfo info = this.ogMap.get(term);
//        List<List<String>> list = info.getPostings();
//        for (List<String> subPost : list) {
//            set.add(Integer.valueOf(subPost.get(0)));
//        }
//
//        return set;
//    }


//    public int getDocLen(String term, int docId) {
//        PostingsInfo info = this.ogMap.get(term);
//        List<List<String>> lst = info.getPostings();
//        for(List<String> l: lst){
//            if(Integer.parseInt(l.get(0)) == docId){
//                return Integer.parseInt(l.get(2));
//            }
//        }
//        return -1;
//    }

//    public int getTF(String term, int docId) {
//        PostingsInfo info = this.ogMap.get(term);
//        List<List<String>> lst = info.getPostings();
//        for(List<String> l: lst){
//            if(Integer.parseInt(l.get(0)) == docId){
//                return Integer.parseInt(l.get(1));
//            }
//        }
//        return -1;
//    }
    // Given Query q_1 to q_n, the BM score of document is returned
    public HashMap<Integer, Double> bm25DocScores() throws Exception {
       // String[] queryArray = query.split("\\s+");
//    List<String> queryList = new ArrayList<>();
        //find common docs in the query
        Set<String> terms = this.ogMap.keySet();
        Set<Integer> docSet = new HashSet<>();

        double score = 0.0;

        //int docFreq = 0;

        //double avgDocLength = 0; //get from DB
        HashMap<Integer, Double> docScore = new HashMap<Integer, Double>();

        for (String term : terms) {
            docSet.addAll(Utility.fetchDocumentSet(term, this.ogMap));
        }

        for (String term : terms) {
            int docFreq = Integer.parseInt(this.ogMap.get(term).getDf());
            double idf = idf(totalDocs, docFreq);
            for (int doc : docSet) {

                //int tf = 0; // get from DB
                int tf = Utility.getTF(term, doc, this.ogMap);
                if(tf == -1)continue;

                // int docLength = 0; // get from DB
                int docLength = Utility.getDocLen(term, doc, this.ogMap);
                if(docLength == -1)continue;

                score = ithScore(tf, idf, docLength, avgDocLength, k1, b);
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
        return docScore;
    }

    public List<String> getRelevantDocs(HashMap<String, PostingsInfo> map, int topK) throws Exception {
        this.ogMap = map;
        HashMap<Integer, Double> scores = this.bm25DocScores();
//        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(topK);
//        Set<Integer> keys = scores.keySet();
//        for(int key: keys){
//            queue.add(key);
//            if(queue.size() > topK){
//                queue.remove(key);
//            }
//        }
//
//        //get the docs from id
//        List<String> response = new ArrayList<>();
//        while(!queue.isEmpty()){
//            int offset = queue.poll();
//            String document = Utility.getDoc(offset);
//            response.add(document);
//        }
        List<Map.Entry<Integer,Double>> scoresList = new ArrayList<>(scores.entrySet());
        Utility.helperCustomSort(scoresList);
       // scoresList.sort((a ,b) -> (int) ((Integer) b.getValue() - (Integer) a.getValue()));
//        scoresList.sort(new Comparator<Map.Entry<Integer, Double>>() {
//            private static final double EPSILON = 1e-10; // Adjust the epsilon based on your requirements
//
//            @Override
//            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
//                // Compare with epsilon to handle precision issues
//                if (Math.abs(o1.getValue() - o2.getValue()) < EPSILON) {
//                    return 0;
//                } else if (o1.getValue() < o2.getValue()) {
//                    return -1;
//                } else {
//                    return 1;
//                }
//            }
//
//        });
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

//    public String getDoc(int offset)
//    {
//        String doc = "Empty doc";
//        try {
//            RandomAccessFile randomAccessFile = new RandomAccessFile(collection, "r");
//            randomAccessFile.seek(offset);
//
////            byte[] buffer = new byte[1024];
//            doc = randomAccessFile.readLine();
//
//            // Process the read data (you can convert it to a String or handle it as needed)
//           // String data = new String(buffer, 0, bytesRead);
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
};




