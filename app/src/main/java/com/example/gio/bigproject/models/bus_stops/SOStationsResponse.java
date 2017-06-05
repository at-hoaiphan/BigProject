package com.example.gio.bigproject.models.bus_stops;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
 * Copyright by Gio.
 * Created on 4/5/2017.
 */

    public class SOStationsResponse {
        @SerializedName("results")
        @Expose
        private List<Result> results = null;
        @SerializedName("status")
        @Expose
        private String status;
        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
