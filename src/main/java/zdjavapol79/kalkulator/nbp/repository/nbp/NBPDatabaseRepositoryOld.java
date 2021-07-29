package zdjavapol79.kalkulator.nbp.repository.nbp;

import java.util.HashMap;
import java.util.Optional;

public class NBPDatabaseRepositoryOld {

    private HashMap<String, Double> courses;

    public NBPDatabaseRepositoryOld(){
        this.courses = new HashMap<>();
    }

    public NBPDatabaseRepositoryOld(HashMap<String, Double> courses){
        this.courses = courses;
    }

    public Optional<Double> findCourseForCurrencyForDate(String currencyCode, String date) {
        return Optional.ofNullable(courses.get(currencyCode+date));
    }

    public void save(String currencyCode, String date, Double value) {
        courses.put(currencyCode+date, value);
    }
}
