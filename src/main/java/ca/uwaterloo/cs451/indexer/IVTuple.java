//package ca.uwaterloo.cs451.indexer;
//
//////import com.datastax.oss.driver.api.mapper.annotations.CqlName;
////
////import com.datastax.oss.driver.api.mapper.annotations.CqlName;
//////import com.datastax.oss.driver.api.mapper.annotations.Frozen;
////
////
////@CqlName("iv_tuple")
////public class PostingTuple {
////    private String docId;
////    private int tf;
////    private int docLen;
////
////    @Override
////    public String toString() {
////        return "PostingTuple{" +
////                "docId='" + docId + '\'' +
////                ", tf=" + tf +
////                ", docLen=" + docLen +
////                '}';
////    }
////
////    // Add getters and setters
////}
//
//
//import com.datastax.driver.mapping.annotations.Field;
//import com.datastax.driver.mapping.annotations.UDT;
//
//@UDT(keyspace = "indexer_space", name="iv_tuple")
//public class IVTuple{
//
//    @Field(name="doc_id")
//    private String docId;
//
//    @Field(name="tf")
//    private String tf;
//
//    @Field(name="doc_len")
//    private String doc_len;
//
//
//}