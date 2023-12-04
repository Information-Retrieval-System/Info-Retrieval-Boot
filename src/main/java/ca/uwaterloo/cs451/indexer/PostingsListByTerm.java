//package ca.uwaterloo.cs451.indexer;
//
//import com.datastax.driver.mapping.annotations.*;
////import com.datastax.oss.driver.api.mapper.annotations.*;
////
////import com.datastax.oss.driver.api.mapper.annotations.Transient;
//
//import java.util.List;
//import java.util.UUID;
//
////@Entity
//@Table(keyspace = "indexer_space", name = "posting_list_by_term")
//public class PostingsListByTerm {
//
//    @PartitionKey
//    @Column(name = "term")
//    private String term;
//
//    @ClusteringColumn
//    @Column(name = "uuid_time")
//    private UUID uuidTime;
//
//    public PostingsListByTerm() {
//    }
//
//    public PostingsListByTerm(String term, UUID uuidTime, String df, List<IVTuple> postings) {
//        this.term = term;
//        this.uuidTime = uuidTime;
//        this.df = df;
//        this.postings = postings;
//    }
//
//    private String df;
//
//    //@FrozenValue(target = IvTuple.class)
//    //private List<IvTuple> postings;
//
//    // Constructors, getters, and setters...
//
////   // @Frozen
////    public static class IvTuple {
////        private String docId;
////        private String tf;
////        private String docLen;
////
////        // Constructors, getters, and setters...
////    }
//
//    @Frozen
//    @Column(name = "postings")
//    private List<IVTuple> postings;
//
//    public String getTerm() {
//        return term;
//    }
//
//    public void setTerm(String term) {
//        this.term = term;
//    }
//
//    public UUID getUuidTime() {
//        return uuidTime;
//    }
//
//    public void setUuidTime(UUID uuidTime) {
//        this.uuidTime = uuidTime;
//    }
//
//    public String getDf() {
//        return df;
//    }
//
//    public void setDf(String df) {
//        this.df = df;
//    }
//
//    public List<IVTuple> getPostings() {
//        return postings;
//    }
//
//    public void setPostings(List<IVTuple> postings) {
//        this.postings = postings;
//    }
//
//    @Override
//    public String toString() {
//        return "PostingsListByTerm{" +
//                "term='" + term + '\'' +
//                ", uuidTime=" + uuidTime +
//                ", df='" + df + '\'' +
//                ", postings=" + postings +
//                '}';
//    }
//}
