package zdjavapol86.kalkulator.nbp.repository.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NBPDatabaseRepository {

    private final EntityManager entityManager;

    public NBPDatabaseRepository(){
        this.entityManager = DatabaseConnection.getInstance().getEntityManagerFactory().createEntityManager();
    }

    public NBPDatabaseRepository(DatabaseConnection databaseConnection) {
        this.entityManager = databaseConnection.getEntityManagerFactory().createEntityManager();
    }

    public Optional<CurrencyEntity> findCurrency(String currencyCode) {
        return Optional.ofNullable(entityManager.find(CurrencyEntity.class, currencyCode));
    }

    public Optional<CurrencyEntity> findCourseForCurrencyForDate(String currencyCode, LocalDate date){
        return entityManager.createQuery("select c from CurrencyEntity c where c.effectiveDate = :date and c.code = :currencyCode", CurrencyEntity.class)
                .setParameter("date", date)
                .setParameter("currencyCode", currencyCode)
                .getResultList().stream().findFirst();
    }

    public Optional<List<CurrencyEntity>> findAllCurrencies() {
        return Optional.ofNullable(entityManager.createQuery("FROM CurrencyEntity", CurrencyEntity.class).getResultList());
    }

    public void addCurrency(CurrencyEntity currencyEntity) {
        doInTransaction(currencyEntity, entry -> entityManager.persist(currencyEntity));
    }

//    public void addIfNotAvailable(List<CurrencyEntity> currencies){
//        doInTransaction(currencies, currencyList -> currencyList.forEach(currencyEntityToSave -> {
//            Optional<CurrencyEntity> courseForCurrencyForDate = findCourseForCurrencyForDate(currencyEntityToSave.getCode(), currencyEntityToSave.getEffectiveDate());
//            if(courseForCurrencyForDate.isEmpty()){
//                entityManager.persist(currencyEntityToSave);
//            }
//        }));
//    }

    public void addIfNotAvailable(List<CurrencyEntity> currencies){
        List<CurrencyEntity> ratesForDays = findRatesForDays(currencies.stream().map(CurrencyEntity::getEffectiveDate).distinct().collect(Collectors.toList()));
        doInTransaction(currencies, currencyList -> currencyList.stream()
                .filter(currencyEntityToSave -> ratesForDays.stream().anyMatch(
                        x -> !currencyEntityToSave.getEffectiveDate().equals(x.getEffectiveDate()) &&
                                !currencyEntityToSave.getCode().equals(x.getCode()) &&
                                currencyEntityToSave.getAvgRate() != x.getAvgRate()
                )).forEach(entityManager::persist));
    }

    private List<CurrencyEntity> findRatesForDays(List<LocalDate> datesFor){
        return entityManager.createQuery("from CurrencyEntity c where c.effectiveDate in :dates", CurrencyEntity.class)
                .setParameter("dates", datesFor)
                .getResultList();
    }


    public void addCurrencyList(List<CurrencyEntity> currencies){
        doInTransaction(currencies, currencyList -> currencyList.forEach(entityManager::persist));
    }

    public void removeCurrency(String currencyCode) {
        Optional<CurrencyEntity> currency = findCurrency(currencyCode);
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
