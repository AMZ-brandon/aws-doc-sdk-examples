// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

import com.example.redshift.*;
import com.example.redshift.scenario.RedshiftActions;
import com.example.redshift.scenario.RedshiftScenario;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.redshift.RedshiftClient;
import software.amazon.awssdk.services.redshift.model.ClusterAlreadyExistsException;
import software.amazon.awssdk.services.redshift.model.CreateClusterResponse;
import software.amazon.awssdk.services.redshift.model.DeleteClusterResponse;
import software.amazon.awssdk.services.redshift.model.ModifyClusterResponse;
import software.amazon.awssdk.services.redshiftdata.RedshiftDataClient;
import software.amazon.awssdk.services.redshiftdata.model.ExecuteStatementResponse;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * To run these integration tests, you must set the required values
 * in the config.properties file or AWS Secrets Manager.
 */
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AmazonRedshiftTest {
    private static final Logger logger = LoggerFactory.getLogger(AmazonRedshiftTest.class);
    private static RedshiftClient redshiftClient;
    private static RedshiftDataClient redshiftDataClient;
    static RedshiftActions redshiftActions = new RedshiftActions();
    private static String clusterId = "";
    private static String fileNameSc = "";
    private static String userName = "";
    private static String userPassword = "" ;
    private static String databaseName = "" ;
    private static String id;

    @BeforeAll
    public static void setUp() {
        redshiftClient = RedshiftClient.builder()
            .region(Region.US_EAST_1)
            .build();

        redshiftDataClient = RedshiftDataClient.builder()
            .region(Region.US_EAST_1)
            .build();

        Random rand = new Random();
        int randomNum = rand.nextInt((10000 - 1) + 1) + 1;
        databaseName = "dev" ;

        // Get the values to run these tests from AWS Secrets Manager.
        Gson gson = new Gson();
        String json = getSecretValues();
        SecretValues values = gson.fromJson(json, SecretValues.class);
        clusterId = values.getClusterId() +randomNum;
        userName = values.getUserName();
        userPassword = values.getPassword();
        fileNameSc = values.getFileName();
    }

    @Test
    @Tag("IntegrationTest")
    @Order(1)
    public void testHelloRedshift() {
        assertDoesNotThrow(() -> HelloRedshift.listClustersPaginator(redshiftClient));
        logger.info("Test 1 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(2)
    public void testCreateCluster() {
        try {
            CompletableFuture<CreateClusterResponse> future = redshiftActions.createClusterAsync(clusterId, userName, userPassword);
            future.join();
            logger.info("Cluster successfully created.");

        } catch (RuntimeException rt) {
            Throwable cause = rt.getCause();
            if (cause instanceof ClusterAlreadyExistsException) {
                System.out.println("The Cluster {} already exists. Moving on...");
            } else {
                System.out.println("An unexpected error occurred: " + rt.getMessage());
            }
        }
        logger.info("Test 2 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(3)
    public void testWaitCluster() {
        assertDoesNotThrow(() -> {
            CompletableFuture<Void> future = redshiftActions.waitForClusterReadyAsync(clusterId);
            future.join();
        });
        logger.info("Test 3 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(4)
    public void testListDatabases() {
        assertDoesNotThrow(() -> {
            CompletableFuture<Void> future = redshiftActions.listAllDatabasesAsync(clusterId, userName, "dev");
            future.join();
        });
        logger.info("Test 4 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(5)
    public void testCreateDatabaseTable() {
        assertDoesNotThrow(() -> {
            CompletableFuture<ExecuteStatementResponse> future = redshiftActions.createTableAsync(clusterId, databaseName, userName);
            future.join();
        });
        logger.info("Test 5 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(6)
    public void testPopDatabaseTable() {
        assertDoesNotThrow(() -> {
            redshiftActions.popTableAsync(clusterId, databaseName, userName, fileNameSc, 50).join();
        });
        logger.info("Test 6 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(7)
    public void testQueryDatabaseTable() {
        assertDoesNotThrow(() -> {
            CompletableFuture<String> future = redshiftActions.queryMoviesByYearAsync(databaseName, userName, 2014, clusterId);
            id = future.join();
        });
        logger.info("Test 7 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(8)
    public void testCheckStatement() {
        assertDoesNotThrow(() -> {
            CompletableFuture<Void> future = redshiftActions.checkStatementAsync(id);
            future.join();
        });
        logger.info("Test 8 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(9)
    public void testGetResults() {
        assertDoesNotThrow(() -> {
            CompletableFuture<Void> future = redshiftActions.getResultsAsync(id);
            future.join();
        });
        logger.info("Test 9 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(10)
    public void testModifyDatabase() {
        assertDoesNotThrow(() -> {
            CompletableFuture<ModifyClusterResponse> future = redshiftActions.modifyClusterAsync(clusterId);;
            future.join();
        });
        logger.info("Test 10 passed");
    }

    @Test
    @Tag("IntegrationTest")
    @Order(11)
    public void testDeleteDatabase() {
        assertDoesNotThrow(() -> {
            CompletableFuture<DeleteClusterResponse> future = redshiftActions.deleteRedshiftClusterAsync(clusterId);;
            future.join();
        });
        logger.info("Test 11 passed");
    }

    private static String getSecretValues() {
        SecretsManagerClient secretClient = SecretsManagerClient.builder()
            .region(Region.US_EAST_1)
            .build();
        String secretName = "test/red";

        GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
            .secretId(secretName)
            .build();

        GetSecretValueResponse valueResponse = secretClient.getSecretValue(valueRequest);
        return valueResponse.secretString();
    }

    @Nested
    @DisplayName("A class used to get test values from test/red (an AWS Secrets Manager secret)")
    class SecretValues {
        private String clusterId;
        private String userName;

        private String password;
        private String fileName;

        public String getClusterId() {
            return clusterId;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

        public String getFileName() {
            return fileName;
        }
    }
}
