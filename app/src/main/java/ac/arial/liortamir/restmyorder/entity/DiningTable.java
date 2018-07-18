package ac.arial.liortamir.restmyorder.entity;

public class DiningTable {

    public static final String DB_DINING_TABLE_ID = "diningTableId";
    public static final String DB_DINING_TABLE_NAME = "diningTableName";

    private Integer diningTableId;
    private String diningTableName;

    public DiningTable(Integer diningTableId, String diningTableName) {
        this.diningTableId = diningTableId;
        this.diningTableName = diningTableName;
    }

    public Integer getDiningTableId() {
        return diningTableId;
    }

    public void setDiningTableId(Integer diningTableId) {
        this.diningTableId = diningTableId;
    }

    public String getDiningTableName() {
        return diningTableName;
    }

    public void setDiningTableName(String diningTableName) {
        this.diningTableName = diningTableName;
    }

    @Override
    public String toString() {
        return getDiningTableName();
    }
}
