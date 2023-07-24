package com.example.truckflow.entities;


import java.util.List;
public class DistanceMatrixResponse {
    public String status;
    public List<Row> rows;

    public DistanceMatrixResponse() {
        // Required empty constructor for Gson
    }

    @Override
    public String toString() {
        return "DistanceMatrixResponse{" +
                "status='" + status + '\'' +
                ", rows=" + rows +
                '}';
    }
}
