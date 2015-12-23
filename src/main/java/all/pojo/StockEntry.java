package all.pojo;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;

import all.enums.StockType;

/**
 * Used like a pojo for entry with particular stock
 */
public class StockEntry {

    private String stockSymbol;

    private StockType stockType;

    private int lastDivident;

    private Integer fixedDivident;

    private int parValue;

    private double price;

    private TreeMap tradeRecords;

    private Timestamp lastMeasuringOfPriceTime;

    private double lastMeasuredPriceTotal;

    private double lastMeasuredQuantityTotal;

    public StockEntry(String stockSymbol, StockType stockType, int lastDivident, Integer fixedDivident, int parValue) {
        tradeRecords = new TreeMap<Timestamp, TradeRecord>(Collections.reverseOrder());
        this.stockSymbol = stockSymbol;
        this.stockType = stockType;
        this.lastDivident = lastDivident;
        this.fixedDivident = fixedDivident;
        this.parValue = parValue;
        this.lastMeasuredQuantityTotal = 0;
        this.lastMeasuringOfPriceTime = new Timestamp(Long.MIN_VALUE);
    }

    public TreeMap getTradeRecords() {
        return tradeRecords;
    }

    public void setTradeRecords(TreeMap tradeRecords) {
        this.tradeRecords = tradeRecords;
    }

    public void addTradeRecord(Timestamp timestamp, TradeRecord tradeRecord) {
        this.tradeRecords.put(timestamp, tradeRecord);
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Timestamp getLastMeasuringOfPriceTime() {
        return lastMeasuringOfPriceTime;
    }

    public double getLastMeasuredPriceTotal() {
        return lastMeasuredPriceTotal;
    }

    public double getLastMeasuredQuantityTotal() {
        return lastMeasuredQuantityTotal;
    }

    public StockType getStockType() {
        return stockType;
    }

    public void setStockType(StockType stockType) {
        this.stockType = stockType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getLastDivident() {
        return lastDivident;
    }

    public void setLastDivident(int lastDivident) {
        this.lastDivident = lastDivident;
    }

    public Integer getFixedDivident() {
        return fixedDivident;
    }

    public void setFixedDivident(Integer fixedDivident) {
        this.fixedDivident = fixedDivident;
    }

    public int getParValue() {
        return parValue;
    }

    public void setParValue(int parValue) {
        this.parValue = parValue;
    }

    public double actualizeTradeValuesAndReturnPrice() {

        double countingPriceIncrement = 0;
        double countingQuantityIncrement = 0;
        Iterator iterator;
        Timestamp timestampNew = new Timestamp(System.currentTimeMillis());
        Timestamp timestampForEntry;

        if (!this.getTradeRecords().isEmpty()) {

            iterator = this.tradeRecords.keySet().iterator();

            while (iterator.hasNext()) {
                timestampForEntry = (Timestamp) iterator.next();
                if (timestampForEntry.after(this.lastMeasuringOfPriceTime)) {
                    countingQuantityIncrement += ((TradeRecord) (this.tradeRecords.get(
                            timestampForEntry))).getQuantity();
                    countingPriceIncrement += ((TradeRecord) (this.tradeRecords.get(timestampForEntry))).getQuantity() *
                            ((TradeRecord) (this.tradeRecords.get(timestampForEntry))).getPrice();
                } else {
                    break;
                }
            }
            this.lastMeasuredPriceTotal = lastMeasuredPriceTotal + countingPriceIncrement;
            this.lastMeasuredQuantityTotal = lastMeasuredQuantityTotal + countingQuantityIncrement;
            this.lastMeasuringOfPriceTime = timestampNew;

            return lastMeasuredPriceTotal / lastMeasuredQuantityTotal;
        } else return 0;
    }
}
