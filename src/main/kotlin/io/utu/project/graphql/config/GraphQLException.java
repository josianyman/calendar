package io.utu.project.graphql.config;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

public class GraphQLException extends RuntimeException implements GraphQLError {

    RuntimeException error;

    public GraphQLException(RuntimeException error) {
        this.error = error;
    }

    @Override
    public String getMessage() {
        return error.getMessage();
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorClassification getErrorType() {
        if(error instanceof IllegalArgumentException) {
            return ErrorType.ValidationError;
        } else {
            return ErrorType.DataFetchingException;
        }
    }
}