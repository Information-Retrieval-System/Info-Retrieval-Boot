package ca.uwaterloo.cs451.indexer;

import ca.uwaterloo.cs451.indexer.util.CustomTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import java.util.List;

@RestController
public class CassandraController {

    @Autowired
    private final CassandraService cassandraService;

    public CassandraController(CassandraService cassandraService) {
        this.cassandraService = cassandraService;
    }

//    @Autowired
//    public CassandraController(CassandraService cassandraService) {
//        this.cassandraService = cassandraService;
//    }

    @GetMapping("/custom-query")
    public void executeCustomQuery() throws Exception {
        cassandraService.executeCustomQuery("", "");
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "query", required = false) String query, @RequestParam(name="algorithm", required = false) String algo, Model model) {
        try {
            // Process the search query (you can implement your search logic here)
            // For now, let's just print the query to the console
            System.out.println("Search query: " + query);
            System.out.println("Search algorithm: " + algo);
            List<String> searchResults = cassandraService.executeCustomQuery(query, algo);
            // For now, let's simulate some search results
            ///List<String> searchResults = Arrays.asList("Result 1", "Result 2", "Result 3");

            // Add the searchResults attribute to the model
            System.out.println(searchResults.size());
            model.addAttribute("res", searchResults);


            // Return the result page
            //return "results";
            StringBuilder builder = new StringBuilder();
            for(String r: searchResults){
                //builder.append("<h2>term: " + r.getS1()  + " UUID: " + r.getS2() + " DF: " + r.getS3() + " DOCID: " + r.getS4() + "</h2>");
                builder.append("<h2>" + r + "</h2>");
            }
            return """
                    <!DOCTYPE html>
                        <title>Info Retrieval</title>
                    </head>
                    <body>""" + builder.toString() +
                    """
                    </div>
                    </body>
                    </html>
                    """;
        }catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Handle the exception or rethrow it based on your requirements
             return """
                    <!DOCTYPE html>
                        <title>Info Retrieval</title>
                    </head>
                    <body>
                       <h2> Sorry, couldn't find any relevant documents for the give query. Please try again. </h2>
                    </body>
                    </html>
                    """;
        }
        // Return the result page (you can create a result Thymeleaf template)
        //return "result";
    }
}