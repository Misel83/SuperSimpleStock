package all.impl;

import static all.enums.ErrorMessages.*;
import static com.google.common.base.Preconditions.*;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import all.DataStorage;
import all.api.StockService;
import all.enums.StockType;
import all.exception.SuperSimpleException;
import all.pojo.TradeRecord;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    @Qualifier("dataRecord")
    private DataStorage dataStorage;

    private final static Logger logger = Logger.getLogger(StockServiceImpl.class.getName());

    @Override
    public double calculateDividentYield(String nameOfStock, int marketPrice) throws Exception {
        return calculateDivident(nameOfStock, marketPrice);
    }

    @Override
    public double calculatePERatio(String nameOfStock, int marketPrice) throws Exception {

        logger.debug("Going to calculate divident for following stock: " + nameOfStock + " ");
        if (dataStorage.isStockAvailable(nameOfStock)) {
            return (double) marketPrice / calculateDivident(nameOfStock, marketPrice);
        } else {
            throw new SuperSimpleException(NO_SUCH_STOCK_EXISTS.toString(), nameOfStock);
        }
    }

    @Override
    public synchronized void addRecordTrade(TradeRecord tradeRecord, String nameOfStock) throws Exception {
        if (isTradeRecordValid(tradeRecord) && dataStorage.isStockAvailable(nameOfStock)) {
            dataStorage.getStockEntry(nameOfStock).addTradeRecord(tradeRecord.getTimestamp(), tradeRecord);
            TimeUnit.NANOSECONDS.sleep(1);
        } else
            throw new SuperSimpleException(NO_SUCH_STOCK_EXISTS.toString(), nameOfStock);
    }

    @Override
    public double calculateVolumeWeightStockPriceInterval(String nameOfStock, int intervalInMinutes) {
        Timestamp timestampRequestedBefore = new Timestamp(System.currentTimeMillis() - intervalInMinutes * 60 * 1000);
        double countingPriceIncrement = 0;
        double countingVolumeIncrement = 0;
        TradeRecord tradeRecord;

        if (dataStorage.isStockAvailable(nameOfStock)) {
            TreeMap<Timestamp, TradeRecord> timestampTradeRecordTreeMap = dataStorage.getStockEntry(
                    nameOfStock).getTradeRecords();
            Iterator iterator = timestampTradeRecordTreeMap.keySet().iterator();

            while (iterator.hasNext()) {
                tradeRecord = timestampTradeRecordTreeMap.get(iterator.next());
                if ((tradeRecord.getTimestamp()).after(timestampRequestedBefore)) {

                    countingVolumeIncrement += tradeRecord.getQuantity();
                    countingPriceIncrement += tradeRecord.getQuantity() * tradeRecord.getPrice();
                } else {
                    break;
                }
            }
            return countingPriceIncrement / countingVolumeIncrement;
        } else throw new SuperSimpleException(NO_SUCH_STOCK_EXISTS.toString(), nameOfStock);
    }

    @Override
    public double calculateGBEC() {
        return dataStorage.calculateGBEC();
    }

    @Override
    public DataStorage getDataStorage() {
        return this.dataStorage;
    }

    private double calculateDivident(String nameOfStock, int marketPrice) throws Exception {

        checkNotNull(nameOfStock, OBJECT_IS_NULL);

        if (marketPrice <= 0) {
            throw new Exception(PRICE_VALUE_LESS_OR_EQUAL_ZERO.toString());
        }

        if (dataStorage.isStockAvailable(nameOfStock)) {

            logger.debug("Going to calculate divident for following stock: " + nameOfStock + " ");

            if (dataStorage.getStockEntry(nameOfStock).getStockType().equals(StockType.COMMON)) {
                return (double) dataStorage.getStockEntry(nameOfStock).getLastDivident() / marketPrice;
            } else {
                return (double) (dataStorage.getStockEntry(nameOfStock).getFixedDivident() * dataStorage.getStockEntry(
                        nameOfStock).getParValue()) / marketPrice;
            }
        } else {
            throw new SuperSimpleException(NO_SUCH_STOCK_EXISTS.toString(), nameOfStock);
        }
    }

    private boolean isTradeRecordValid(TradeRecord tradeRecord) throws Exception {

        checkNotNull(tradeRecord, OBJECT_IS_NULL);
        checkNotNull(tradeRecord.getIndicator(), OBJECT_IS_NULL);

        if (tradeRecord.getQuantity() <= 0) {
            throw new Exception(QUANTITY_VALUE_LESS_OR_EQUAL_ZERO.toString());
        }
        if (tradeRecord.getPrice() <= 0) {
            throw new Exception(PRICE_VALUE_LESS_OR_EQUAL_ZERO.toString());
        }
        return true;
    }
}
