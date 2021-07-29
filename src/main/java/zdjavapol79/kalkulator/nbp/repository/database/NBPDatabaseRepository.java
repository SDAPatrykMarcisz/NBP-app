package zdjavapol79.kalkulator.nbp.repository.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class NBPDatabaseRepository {

    private final EntityManager entityManager;

    public NBPDatabaseRepository(){
        this.entityManager = DatabaseConnection.getInstance().getEntityManagerFactory().createEntityManager();
    }

    public NBPDatabaseRepository(DatabaseConnection databaseConnection) {
        this.entityManager = databaseConnection.getEntityManagerFactory().createEntityManager();
    }

    public Optional<Currency> findCurrency(String currencyCode) {
        return Optional.ofNullable(entityManager.find(Currency.class, currencyCode));
    }

    public Optional<Currency> findCourseForCurrencyForDate(String currencyCode, LocalDate date){
        return entityManager.createQuery("select c from Currency c where c.effectiveDate = :date and c.code = :currencyCode", Currency.class)
                .setParameter("date", date)
                .setParameter("currencyCode", currencyCode)
                .getResultList().stream().findFirst();
    }

    public Optional<List<Currency>> findAllCurrencies() {
        return Optional.ofNullable(entityManager.createQuery("FROM Currency", Currency.class).getResultList());
    }

    public void addCurrency(Currency currency) {
        doInTransaction(currency, entry -> entityManager.persist(currency));
    }

    public void addIfNotAvailable(List<Currency> currencies){
        doInTransaction(currencies, currencyList -> currencyList.forEach(currencyToSave -> {
            Optional<Currency> courseForCurrencyForDate = findCourseForCurrencyForDate(currencyToSave.getCode(), currencyToSave.getEffectiveDate());
            if(courseForCurrencyForDate.isEmpty()){
                entityManager.persist(currencyToSave);
            }
        }));
    }

    public void addCurrencyList(List<Currency> currencies){
        doInTransaction(currencies, currencyList -> currencyList.forEach(entityManager::persist));
    }

    public void removeCurrency(String currencyCode) {
        Optional<Currency> currency = findCurrency(currencyCode);
        if (currency.isPresent()) {
            doInTransaction(currency.get(), entry -> entityManager.remove(currency.get()));
        }
    }

    public <T> void doInTransaction(T data, Consumer<T> consumer){
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        consumer.accept(data);
        transaction.commit();
    }

}
