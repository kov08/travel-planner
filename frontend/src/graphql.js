import { gql } from "@apollo/client";

export const ADD_CONNECTION = gql`
  mutation AddConnection(
    $source: String!
    $destination: String!
    $cost: Float!
    $time: Float!
    $mode: String!
  ) {
    addConnection(
      source: $source
      destination: $destination
      cost: $cost
      time: $time
      mode: $mode
    ) {
      id
    }
  }
`;

export const GET_CONNECTIONS = gql`
  query {
    getAllConnections {
      id
      source
      destination
      cost
      time
      mode
    }
  }
`;

export const DELETE_CONNECTION = gql`
  mutation DeleteConnection($id: ID!) {
    deleteConnection(id: $id)
  }
`;
