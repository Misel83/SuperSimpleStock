package all;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static all.enums.ErrorMessages.*;
import static com.google.common.base.Preconditions.*;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import all.exception.SuperSimpleException;
import all.pojo.StockEntry;


@Component
public class DataStorage {

    private final static Logger logger = Logger.getLogger(DataStorage.class.getName());

    private Map<String, StockEntry> dataStorage = new HashMap<String, StockEntry>();

    public void addStockEntry(String name, StockEntry stockEntry) {
        validateArguments(name, stockEntry);
        dataStorage.put(name, stockEntry);
    }

    public StockEntry getStockEntry(String nameOfStock) {
        checkNotNull(nameOfStock, NAME_OF_STOCK_IS_NULL.toString());
        if (dataStorage.containsKey(nameOfStock)) {
            return dataStorage.get(nameOfStock);
        } else
            throw new SuperSimpleException(NO_SUCH_STOCK_EXISTS.toString(), nameOfStock);
    }

    public boolean isStockAvailable(String name) {
        return checkAvailability(name);
    }

    private void validateArguments(String name, StockEntry stockEntry) {
        checkNotNull(name, NAME_OF_STOCK_IS_NULL.toString());
        checkNotNull(stockEntry, OBJECT_IS_NULL.toString() + "stockEntry");

        if (!name.equals(stockEntry.getStockSymbol())) {
            throw new IllegalStateException("Attempt to add stock which is already supported");
        }
    }

    private boolean checkAvailability(String name) {
        return dataStorage.containsKey(name);
    }

    public double calculateGBEC() {

        StockEntry stockEntryTemp;
        double result;
        int countOfStockWithTrades = 0;

        logger.debug("Going to calculateGBEC");

        if (!dataStorage.isEmpty()) {
            Iterator<StockEntry> stockEntryIterator = dataStorage.values().iterator();
            long incrementPricesProperty = 1;

            while (stockEntryIterator.hasNext()) {
                stockEntryTemp = stockEntryIterator.next();
                if (stockEntryTemp.getTradeRecords().size() > 0) {
                    countOfStockWithTrades += 1;
                    logger.debug(
                            "Going to include following stock into calculation: " + stockEntryTemp.getStockSymbol());
                    logger.debug("last measured price total: " + stockEntryTemp.getLastMeasuredPriceTotal());
                    logger.debug("last measured price total: " + stockEntryTemp.getLastMeasuredQuantityTotal());
                    logger.debug("last measured in following time: " + stockEntryTemp.getLastMeasuringOfPriceTime());
                    incrementPricesProperty *= stockEntryTemp.actualizeTradeValuesAndReturnPrice();
                    logger.debug("State of values after recalculation: " + stockEntryTemp.getStockSymbol());
                    logger.debug("new measured price total: " + stockEntryTemp.getLastMeasuredPriceTotal());
                    logger.debug("new measured price total: " + stockEntryTemp.getLastMeasuredQuantityTotal());
                    logger.debug("new measured in following time: " + stockEntryTemp.getLastMeasuringOfPriceTime());
                }
            }
            result = Math.pow(incrementPricesProperty, 1.0 / countOfStockWithTrades);
            logger.debug("result of calculation of GBEC is: " + result);
            return result;
        }
        return -1;
    }
}
