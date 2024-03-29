package com.solvd.farm;


import com.qaprosoft.carina.core.foundation.IAbstractTest;
import com.qaprosoft.carina.core.foundation.api.http.HttpResponseStatusType;
import com.solvd.farm.testautomation.api.GetWeatherByCityMethod;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class ApiTest implements IAbstractTest {
    private final static Logger LOGGER = LogManager.getLogger(ApiTest.class);
    private final static String CITY_MINSK = "Minsk";

    @Test
    public void apiWeatherTest() {
        GetWeatherByCityMethod getWeatherByCityMethod = new GetWeatherByCityMethod(CITY_MINSK);
        getWeatherByCityMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        Response response = getWeatherByCityMethod.callAPI();
        LOGGER.info(response.body().prettyPrint());
        getWeatherByCityMethod.validateResponseAgainstSchema("api/_get/rs.schema");
    }
}