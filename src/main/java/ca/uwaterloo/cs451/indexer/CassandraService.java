package ca.uwaterloo.cs451.indexer;

import ca.uwaterloo.cs451.indexer.util.CustomTuple;
//import com.datastax.driver.core.PreparedStatement;
//import com.datastax.driver.core.ResultSet;
//import com.datastax.driver.core.Session;
//import com.datastax.driver.mapping.Mapper;
//import com.datastax.driver.mapping.MappingManager;
//import com.datastax.driver.mapping.Result;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.data.UdtValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.datastax.oss.driver.api.mapper.annotations.CqlName;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
@Service
public class CassandraService {


    private final CqlSession cqlSession;


    @Autowired
    public CassandraService(CqlSession cqlSession) {
        this.cqlSession = cqlSession;
    }

    //    @CqlName("postings")
//    public interface Postings {
//        UdtValue getIndexesTuples();
//        int getDf();
//    }
    public List<CustomTuple<String, String, String, Integer>> executeCustomQuery(String queryterm) {
        //String cqlQuery = "SELECT * FROM posting_list_by_term where term={queryterm}";
//        System.out.println("queryterm" + queryterm);
//        String cqlQuery = "SELECT * FROM posting_list_by_term where term=" + queryterm;
//        ResultSet resultSet = cqlSession.execute(cqlQuery);

//        MappingManager manager = new MappingManager((Session) this.cqlSession);
//        Mapper<PostingsListByTerm> mapper = manager.mapper(PostingsListByTerm.class);
        // Define the parameterized query
        String query = "SELECT * FROM posting_list_by_term1 WHERE term = ?";

        // Prepare the statement
        PreparedStatement preparedStatement = cqlSession.prepare(query);

        // Specify the custom string (replace "customString" with the actual value)
        //String customString = "customString";

        // Execute the query with the custom string as a bound parameter
        ResultSet resultSet = cqlSession.execute(preparedStatement.bind(queryterm));
//        Result<PostingsListByTerm> p = mapper.map((com.datastax.driver.core.ResultSet) resultSet);
//        PostingsListByTerm first = p.one();

        List<CustomTuple<String, String, String, Integer>> res = new ArrayList<>();
        //System.out.println(first.toString());
        // Iterate over the result set
        int count = 0;
        for (Row row : resultSet) {
            // Access column values using column names or indexes
            String term = row.getString("term");
            String uuidTime = row.getUuid("uuid_time").toString();
            String df = row.getString("df");
            List<List<String>> tuples = row.get(3, List.class);
            // UDTValue udtData = row.getUDTValue(address);

//            String term = row.getString("term");
//            String uuidTime = row.getUuid("uuid_time").toString();

            // Extract values from the nested "postings" structure
            //String df = row.getString("postings.df");

            // Extract values from the "indexes_tuples" list
            //Object indexesTuples = row.getObject("postings");
            System.out.println(tuples.get(0).get(0));
//            for (Row indexTuple : indexesTuples) {
//                String docId = indexTuple.getString("doc_id");
//                int tf = indexTuple.getInt("tf");
//                int docLen = indexTuple.getInt("doc_len");
//
//                // Process extracted values as needed
//                System.out.println("Term: " + term + ", UUID Time: " + uuidTime +
//                        ", DF: " + 0 + ", Doc ID: " + docId +
//                        ", TF: " + tf + ", Doc Length: " + docLen);
//            }


            //TIMEUUID column2Value = row.getString("uuid_time");
            //System.out.println(column1Value);
            //UUID uuid = row.getUuid("uuid_time");
            //Object list = row.getUdtValue("postings");
            //System.out.println(list.toString());
            // + " Column2: " + list.toString()
            // Process the data or perform other operations
            //System.out.println("Column1: " + column1Value + ", Column2: " + timeuuidValue);

            res.add(new CustomTuple<>(term, uuidTime, df, tuples.get(0).get(count++)));
        }

//        String x = s.all().get(0).toString();
//        System.out.println(s);
            // Process the result or do other operations
            return res;
        }
    }
