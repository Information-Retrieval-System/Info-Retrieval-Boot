package ca.uwaterloo.cs451.indexer.util;


import java.util.List;

public class PostingsInfo {

    String term;
    String df;

    List<List<Integer>> postings;

    public PostingsInfo(String term, String df, List<List<Integer>> postings) {
        this.term = term;
        this.df = df;
        this.postings = postings;
    }

    @Override
    public String toString() {
        return "PostingsInfo{" +
                "term='" + term + '\'' +
                ", df='" + df + '\'' +
                ", postings=" + postings +
                '}';
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDf() {
        return df;
    }

    public void setDf(String df) {
        this.df = df;
    }

    public List<List<Integer>> getPostings() {
        return postings;
    }

    public void setPostings(List<List<Integer>> postings) {
        this.postings = postings;
    }
}
