package telran.probes;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.client.RestTemplate;
import telran.probes.dto.Range;
import telran.probes.dto.SensorUpdateData;
import telran.probes.messages.ErrorMessages;
import telran.probes.service.RangeProviderClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnalyzerServiceTest {
    private static final  long SENSOR_ID = 123;
    private static final double MIN_VALUE = 100;
    private static final double MAX_VALUE = 200;

    private static final Range RANGE = new Range(MIN_VALUE, MAX_VALUE);
    private static final long SENSOR_ID_NOT_FOUND = 124;
    private static final Range RANGE_DEFAULT =
            new Range(RangeProviderClient.MIN_DEFAULT_VALUE, RangeProviderClient.MAX_DEFAULT_VALUE);
    private static final Range RANGE_UPDATED = new Range(MIN_VALUE+10, MAX_VALUE+10);
    private static final String URL = "http://localhost:8080/sensor/range/";
    private static final long SENSOR_ID_UNAVAILABLE = 125;
    private String updateBindingName = "updateRangeConsumer-in-0";

    @Autowired
    InputDestination producer;

    @Autowired
    RangeProviderClient service;
    @MockBean
    RestTemplate rest;



    @Test
    @Order(1)
    void testNormalFlowNoCache() {
        when(rest.exchange(URL + SENSOR_ID, HttpMethod.GET, null, Range.class))
                .thenReturn(new ResponseEntity<Range>(RANGE, HttpStatus.OK));
        assertEquals(RANGE, service.getRange(SENSOR_ID));
    }

    @Test
    @Order(2)
    void testNormalFlowWithCache(){
        verify(rest, never()).exchange(URL+SENSOR_ID, HttpMethod.GET, null, Range.class);
        assertEquals(RANGE, service.getRange(SENSOR_ID));
    }
    @Test
    @Order(3)
    void testSensorNotFound(){
        when(rest.exchange(URL + SENSOR_ID_NOT_FOUND, HttpMethod.GET, null, String.class))
                .thenReturn(new ResponseEntity<>(ErrorMessages.SENSOR_NOT_FOUND, HttpStatus.NOT_FOUND));
        assertEquals(RANGE_DEFAULT, service.getRange(SENSOR_ID_NOT_FOUND));
    }

    @Test
    @Order(4)
    void testDefaultRangeNotInCache(){
        when(rest.exchange(URL + SENSOR_ID_NOT_FOUND, HttpMethod.GET, null, Range.class))
                .thenReturn(new ResponseEntity<Range>(RANGE, HttpStatus.OK));
        assertEquals(RANGE, service.getRange(SENSOR_ID_NOT_FOUND));
    }
    @Test
    void testRemoteWebServerUnavailable(){
        when(rest.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
                .thenThrow(new RuntimeException("Service is unavailable"));
        assertEquals(RANGE_DEFAULT, service.getRange(SENSOR_ID_UNAVAILABLE));
    }
    @Test
    void testUpdateRangeInMap() throws InterruptedException {
        producer.send(new GenericMessage<SensorUpdateData>(new SensorUpdateData(SENSOR_ID, RANGE_UPDATED, null))
        , updateBindingName);
        Thread.sleep(1000);
        assertEquals(RANGE_UPDATED, service.getRange(SENSOR_ID));
    }
}