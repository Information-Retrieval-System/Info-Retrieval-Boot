package ca.uwaterloo.cs451.indexer.similarity;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


//import tl.lin.data.array.ArrayListWritable;
//import tl.lin.data.pair.PairOfInts;
//import tl.lin.data.pair.PairOfWritables;

import java.nio.file.Paths;

public class BM25 {


    private Stack<Set<Integer>> stack;

    //returns IDF for a term
    // numQ -  number of documents containing q_i = n(qi)
    public double idf(int totalDocs, int numQ) {
        double num = totalDocs - numQ + 0.5;
        double denom = numQ + 0.5;

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

    private Set<Integer> fetchDocumentSet(List<List<String>> postings) throws IOException {
        Set<Integer> set = new TreeSet<>();

        for (List<String> subPost : postings) {
            set.add(Integer.valueOf(subPost.get(0)));
        }

        return set;
    }


    public int returnDocLen(int docId) {

    }

    // Given Query q_1 to q_n, the BM score of document is returned
    public HashMap<Integer, Double> bm25DocScores(String query) throws Exception {
        String[] queryArray = query.split("\\s+");
//    List<String> queryList = new ArrayList<>();
        //find common docs in the query
        double k1 = 1.5;
        double b = 0.75;
        double score = 0.0;
        int totalDocs = 0;
        int numQ = 0;
        double avgDocLength = 0; //get from DB
        HashMap<Integer, Double> docScore = new HashMap<Integer, Double>();
        for (String q : queryArray) {
            Set<Integer> docSet = fetchDocumentSet(q);
            int tf = 0; // get from DB
            double idf = idf(totalDocs, numQ);
            for (int doc : docSet) {
                int docLength = 0; // get from DB
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
};




