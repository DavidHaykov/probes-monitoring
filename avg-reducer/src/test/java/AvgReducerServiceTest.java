import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import telran.probes.dto.ProbeData;
import telran.probes.repo.ProbesList;
import telran.probes.repo.ProbsListRepository;
import telran.probes.service.AvgReducerService;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AvgReducerServiceTest {
    private static final double VALUE = 100;
    private static final long SENSOR_ID = 123;
    private ProbeData data = new ProbeData(SENSOR_ID, VALUE, 0);
    HashMap<Long, ProbesList> redisMockMap = new HashMap<>();
    @BeforeEach
    void  setUp() {
        when(repo.findById(any(Long.class))).then(new Answer<Optional<ProbesList>>() {
            @Override
            public Optional<ProbesList> answer(InvocationOnMock invocationOnMock) throws Throwable {
                long sensorId = invocationOnMock.getArgument(0);
                ProbesList list = redisMockMap.get(sensorId);
                return list == null ? Optional.ofNullable(null) : Optional.of(list);
            }
        });
        when(repo.save(any(ProbesList.class))).then(new Answer<ProbesList>() {
            @Override
            public ProbesList answer(InvocationOnMock invocationOnMock) throws Throwable {
                ProbesList list = invocationOnMock.getArgument(0);
                redisMockMap.put(list.getSensorId(), list);
                return list;
            }
        });
    }

    @Autowired
    AvgReducerService service;

    @MockBean
    ProbsListRepository repo;

    @Test
    void test() {
        Double res= service.getAvgValue(data);
        assertNull(res);
        res = service.getAvgValue(data);
        assertNotNull(res);
        assertEquals(VALUE, res);
    }

}
