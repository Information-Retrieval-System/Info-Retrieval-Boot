package ca.uwaterloo.cs451.indexer;

import ca.uwaterloo.cs451.indexer.util.CustomTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public void executeCustomQuery() {
        cassandraService.executeCustomQuery("");
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "query", required = false) String query, Model model) {
        try {
            // Process the search query (you can implement your search logic here)
            // For now, let's just print the query to the console
            System.out.println("Search query: " + query);
            List<CustomTuple<String, String, String, Integer>> searchResults = cassandraService.executeCustomQuery(query);
            // For now, let's simulate some search results
            ///List<String> searchResults = Arrays.asList("Result 1", "Result 2", "Result 3");

            // Add the searchResults attribute to the model
            System.out.println(searchResults.size());
            model.addAttribute("res", searchResults);


            // Return the result page
            //return "results";
            StringBuilder builder = new StringBuilder();
            for(CustomTuple r: searchResults){
                builder.append("<h2>term: " + r.getS1()  + " UUID: " + r.getS2() + " DF: " + r.getS3() + " DOCID: " + r.getS4() + "</h2>");
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
            return "error"; // You can redirect to an error page
        }
        // Return the result page (you can create a result Thymeleaf template)
        //return "result";
    }
}