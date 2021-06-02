package zdjavapol79.kalkulator.nbp.repository;

import java.util.HashMap;
import java.util.Optional;

public class NBPDatabaseRepository {

    private HashMap<String, Double> courses;

    public NBPDatabaseRepository(){
        this.courses = new HashMap<>();
    }

    public NBPDatabaseRepository(HashMap<String, Double> courses){
        this.courses = courses;
    }

    public Optional<Double> findCourseForCurrencyForDate(String currencyCode, String date) {
        return Optional.ofNullable(courses.get(currencyCode+date));
    }

    public void save(String currencyCode, String date, Double value) {
        courses.put(currencyCode+date, value);
    }
}
