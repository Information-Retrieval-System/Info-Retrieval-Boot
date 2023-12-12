package ca.uwaterloo.cs451.indexer;

import ca.uwaterloo.cs451.indexer.similarity.*;
import ca.uwaterloo.cs451.indexer.util.CustomTuple;
//import com.datastax.driver.core.PreparedStatement;
//import com.datastax.driver.core.ResultSet;
//import com.datastax.driver.core.Session;
//import com.datastax.driver.mapping.Mapper;
//import com.datastax.driver.mapping.MappingManager;
//import com.datastax.driver.mapping.Result;
import ca.uwaterloo.cs451.indexer.util.PostingsInfo;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//import com.datastax.oss.driver.api.mapper.annotations.CqlName;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.nio.file.*;
import java.util.stream.Collectors;

@Service
public class CassandraService {


    @Value("${database.uploader}")
    private boolean databaseUpload;

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspace;

    @Value("${spring.data.cassandra.posting-list-table}")
    private String table;

    @Autowired
    SimilarityFactory similarityFactory;
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
    //public List<CustomTuple<String, String, String, Integer>> executeCustomQuery(String queryterm) throws Exception {
    public List<String> executeCustomQuery(String queryterm, String algo) throws Exception {

        uploaderIndexer();

        String[] queryArray = queryterm.split("\\s+");
        HashMap<String, PostingsInfo> map = new HashMap<>();
        for (String t : queryArray) {
            t = t.toLowerCase();
            String query = "SELECT * FROM " + keyspace + "." + table + " WHERE term = ?";
            PreparedStatement preparedStatement = cqlSession.prepare(query);
            ResultSet resultSet = cqlSession.execute(preparedStatement.bind(t));

            int count = 0;

            for (Row row : resultSet) {
                // Access column values using column names or indexes
                String term = row.getString("term");
                //String uuidTime = row.getUuid("uuid_time").toString();
                //String df = row.getString("df");
                List<List<Integer>> postings = row.get(1, List.class);
                uncompressPostingLists(postings);
                String df = String.valueOf(postings.size());
                if (map.containsKey(term)) {
                    map.get(term).getPostings().addAll(postings);
                } else {
                    map.put(term, new PostingsInfo(term, df, postings));
                }
            }

        }

        System.out.println("Picking Algo");
        SimilarityType t = similarityFactory.similarityType(algo);
        BaseSimilarity similarity = similarityFactory.getCorrectSimilarityAlgorithm(t);
        List<String> docs = similarity.getRelevantDocs(map, 10);

        return docs;
    }

    private void uncompressPostingLists(List<List<Integer>> postings)
    {
        int prevDocID = 0;
        for(List<Integer> lst : postings){

            lst.set(0, prevDocID + lst.get(0));
            prevDocID = lst.get(0);
        }

    }

    public void uploaderIndexer() throws Exception {
        if(!databaseUpload){
            System.out.println("database.uploader flag is set to " + databaseUpload + "No data upload possible");
            return;
        }

        this.dataUploader();
    }



    public void dataUploader() throws IOException {
        String fileName = "src/main/resources/static/PostingLists.txt";

        BufferedReader reader;
        Gson gson = new Gson();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                //System.out.println(line);
                line = line.substring(1, line.length() - 1);
                int commaIndex = line.indexOf(',', 0);
                String term = line.substring(0, commaIndex);
                String postingsList = line.substring(commaIndex + 1);
                List<List<Integer>> lst = convertStringToLists(postingsList);
                String insertQuery = "INSERT INTO " + keyspace + "." + table + "(term, postings) values (?,?)";
                PreparedStatement preparedStatement = cqlSession.prepare(insertQuery);
                ResultSet resultSet = cqlSession.execute(preparedStatement.bind(term, lst));

                System.out.println("term: " + term + " : " + resultSet.wasApplied());
                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<List<Integer>> convertStringToLists(String data){
        // Use Gson library to convert the string to a 2D array
        Gson gson = new Gson();
        int[][] dataArray = gson.fromJson(data, int[][].class);

        // Convert the 2D array to a list of lists
        List<List<Integer>> outputList = new ArrayList<>();
        for (int[] array : dataArray) {
            List<Integer> innerList = Arrays.stream(array).boxed().collect(Collectors.toList());
            outputList.add(innerList);
        }
        return outputList;
    }
}
