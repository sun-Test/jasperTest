package sun.jasperreport.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.jasperreport.test.model.Person;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class PersonService {

    public List<Person> generateData() {
        return IntStream.range(1, 10000).mapToObj(x -> new Person(Long.valueOf(x),
                String.format("Sunn%d", x), String.format("soooome infooo %d", x),
                String.format("soooome infooo %d", x))).collect(Collectors.toList());
    }
}
