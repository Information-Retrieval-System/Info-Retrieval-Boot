package ca.uwaterloo.cs451.indexer;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.session.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class CassandraConfig {


//    nosql_cl = Cluster
//            .builder()
//            .addContactPointsWithPorts(aps.CSClusterNodes)
//                    .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
//                    .withProtocolVersion(ProtocolVersion.NEWEST_SUPPORTED)
//                    .withCodecRegistry(codecRegistry)
//                    .withReconnectionPolicy(
//                            new ConstantReconnectionPolicy(5000))
//            .withLoadBalancingPolicy(new TokenAwarePolicy(new DCAwareRoundRobinPolicy()))
//            .withCredentials(aps.CSU, aps.CSP).build();
//            nosql_cl.getConfiguration().getProtocolOptions();
//...
//    UserType app_sourceType = nosql_cl.getMetadata().getKeyspace(aps.CSKeyspace).getUserType("app_source");
//    AppSourceTypeCodec app_sourceCodec = new AppSourceTypeCodec(TypeCodec.userType(app_sourceType), AppSourceType.class);
//codecRegistry.register(app_sourceCodec);



    @Bean
    public CqlSession cqlSession() {
        return CqlSession.builder()
                .withKeyspace("indexer_space")
                .addContactPoint(new InetSocketAddress("localhost", 9042))
                .withLocalDatacenter("datacenter1")
                .build();
    }

//    @Bean
//    public Session cqlSession() {
//        return new Session().
//
//                .withKeyspace("indexer_space")
//                .addContactPoint(new InetSocketAddress("localhost", 9042))
//                .withLocalDatacenter("datacenter1")
//                .build();
//    }

//    @Bean
//    public Session cqlSession() {
//        Cluster cluster = Cluster.builder()
//                .addContactPoint("localhost")
//                .withPort(9042)
//                .build();
//
//        return cluster.connect("indexer_space");
//    }
}