package com.travel.travel_service.config;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class GraphQLLoggingConfig extends DataFetcherExceptionResolverAdapter {

    @Override
    protected List<GraphQLError> resolveToMultipleErrors(Throwable ex, DataFetchingEnvironment env) {
        // This will print the error to your IDE console whenever something goes wrong
        System.err.println("GraphQL Error Occurred: " + ex.getMessage());
        ex.printStackTrace(); 
        return super.resolveToMultipleErrors(ex, env);
    }
}