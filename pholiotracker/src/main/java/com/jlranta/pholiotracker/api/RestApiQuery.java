package com.jlranta.pholiotracker.api;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * RestApiQuery class is a helper class for placing HTTP queries to the stock
 * APIs. It uses org.apache.http.* for querying.
 * @author Jarkko Lehtoranta
 */
public class RestApiQuery {
    private String apiName = "";
    private CloseableHttpClient cli;
    
    /**
     * Creates a new RestApiQuery instance.
     * @param apiName The display name of the API, where the queries come from
     */
    public RestApiQuery(String apiName) {
        this.apiName = apiName;
        this.cli = HttpClients.createDefault();
    }
    
    /**
     * A simple methods that handles the generic HTTP GET requests.
     * @param url The URL for the HTTP GET request
     * @return The body of the HTTP response as a string
     */
    public String apiRequest(String url) {
        String body = "";
        
        try {
            HttpGet get = new HttpGet(url);
            HttpResponse r = this.cli.execute(get);
            
            if (r.getStatusLine().getStatusCode() != 200) {
                System.err.println(this.apiName + " query failed on HTTP error (" + r.getStatusLine().getStatusCode() + ")");
            } else {
                HttpEntity entity = r.getEntity();
                if (entity != null) {
                    body = EntityUtils.toString(entity);
                } else {
                    System.err.println(this.apiName + " empty HTTP response");
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println(this.apiName + " HTTP request failed");
        }
        
        return body;
    } 
}
