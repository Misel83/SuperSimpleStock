package all.api;

import all.DataStorage;
import all.pojo.TradeRecord;

/**
 * Created by michael.korvas on 2015-11-29.
 */
public interface StockService {

    /**
     * Calculates divident yield for particular stock and market price
     *
     * @param stock
     * @param marketPrice
     * @return
     * @throws Exception
     */
    public double calculateDividentYield(String stock, int marketPrice) throws Exception;

    /**
     * Calculates PERatio yield for particular stock and market price
     *
     * @param stock
     * @param marketPrice
     * @return
     * @throws Exception
     */
    public double calculatePERatio(String stock, int marketPrice) throws Exception;

    /**
     * Adds trade record for particular stock
     *
     * @param tradeRecord
     * @param nameOfStock
     * @throws Exception
     */
    public void addRecordTrade(TradeRecord tradeRecord, String nameOfStock) throws Exception;

    /**
     * Calculates volume weight stock price for time interval (used 15 minutes in implementation)
     *
     * @param nameOfStock
     * @param intervalInMinutes
     * @return
     */
    public double calculateVolumeWeightStockPriceInterval(String nameOfStock, int intervalInMinutes);

    /**
     * Calculates GBEC, using geometric mean
     *
     * @return result of calculation
     */
    public double calculateGBEC();

    /**
     * Returns storage with all stocks and trades on particular stocks
     *
     * @return
     */
    public DataStorage getDataStorage();
}
