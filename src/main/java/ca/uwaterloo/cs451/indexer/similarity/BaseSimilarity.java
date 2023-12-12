package ca.uwaterloo.cs451.indexer.similarity;

import ca.uwaterloo.cs451.indexer.util.PostingsInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseSimilarity
{
    private SimilarityType type;

    public BaseSimilarity(SimilarityType type) {
        this.type = type;
    }

    public SimilarityType getType() {
        return type;
    }

    public void setType(SimilarityType type) {
        this.type = type;
    }

    public List<String> getRelevantDocs(HashMap<String, PostingsInfo> map, int i) throws Exception{
        return new ArrayList<String>();
    }


}
