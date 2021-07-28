package zdjavapol79.kalkulator.nbp.repository;

public enum CurrencyTable {
    TABLE_A("A"),
    TABLE_B("B");

    private String tableCode;

    CurrencyTable(String code) {
        this.tableCode = code;
    }

    public String getTableCode() {
        return tableCode;
    }
}
