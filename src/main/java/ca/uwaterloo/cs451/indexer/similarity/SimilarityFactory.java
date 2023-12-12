package ca.uwaterloo.cs451.indexer.similarity;

import org.springframework.stereotype.Component;

@Component
public class SimilarityFactory
{
    BM25Similarity bm25Similarity;
    VSMSimilarity vsmSimilarity;
    TFIDFSimilarity tfidfSimilarity;

    public SimilarityFactory() {
    }

    private BaseSimilarity getBM25(){
        if(bm25Similarity == null){
            bm25Similarity = new BM25Similarity(SimilarityType.BM25);
            return bm25Similarity;
        }else{
            return bm25Similarity;
        }
    }
    private BaseSimilarity getVSM(){
        if(vsmSimilarity == null){
            vsmSimilarity =  new VSMSimilarity(SimilarityType.VSM);
            return vsmSimilarity;
        }else{
            return vsmSimilarity;
        }
    }
    private BaseSimilarity getTFIDF(){
        if(tfidfSimilarity == null){
            tfidfSimilarity = new TFIDFSimilarity(SimilarityType.TFIDF);
            return tfidfSimilarity;
        }else{
            return tfidfSimilarity;
        }
    }



    public BaseSimilarity getCorrectSimilarityAlgorithm(SimilarityType type){
        BaseSimilarity similarity = null;
        switch (type) {
            case VSM:
                return getVSM();
            case BM25:
                return getBM25();
            case TFIDF:
                return getTFIDF();
                //default:  similarity = getBM25();
        }
        return null;
    }

    public SimilarityType similarityType(String type){
        SimilarityType similarityType = SimilarityType.BM25;
        if(type.equals("BM25"))  similarityType = SimilarityType.BM25;
        else if(type.equals("TFIDF")) similarityType = SimilarityType.TFIDF;
        else{
            similarityType = SimilarityType.VSM;
        }
        return similarityType;
    }
}
