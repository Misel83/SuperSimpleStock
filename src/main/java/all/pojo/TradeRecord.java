package all.pojo;

import java.sql.Timestamp;

import all.enums.Indicator;

/**
 * Used like a pojo for trade record objects
 */
public class TradeRecord {

    private Timestamp timestamp;

    private int quantity;

    private Indicator indicator;

    private int price;

    public TradeRecord(Timestamp timestamp, int quantity, Indicator indicator, int price) {
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.indicator = indicator;
        this.price = price;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
