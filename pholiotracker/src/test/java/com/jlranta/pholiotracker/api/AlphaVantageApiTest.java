package com.jlranta.pholiotracker.api;

import org.junit.Test;
import static org.junit.Assert.*;

public class AlphaVantageApiTest {
    private AlphaVantageApi api = new AlphaVantageApi();
    
    @Test
    public void getDataReturnsValidData() {
        assertTrue(api.getData("AAPL").size() > 0);
    }
}
