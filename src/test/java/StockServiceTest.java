import static all.enums.ErrorMessages.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import all.api.StockService;
import all.enums.Indicator;
import all.exception.SuperSimpleException;
import all.pojo.TradeRecord;

/**
 * Test for StockService verification
 */
public class StockServiceTest {

    StockService stockService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("file:beans.xml");
        stockService = (StockService) context.getBean("StockService");
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldCalculateDividentYieldWithCommonOption() throws Exception {
        assertTrue(stockService.calculateDividentYield("TEA", 100) == 0);
        assertTrue(stockService.calculateDividentYield("POP", 100) == 0.08);
        assertTrue(stockService.calculateDividentYield("ALE", 100) == 0.23);
        assertTrue(stockService.calculateDividentYield("JOE", 100) == 0.13);
    }

    @Test
    public void shouldCalculateDividentYieldWithPreferredOption() throws Exception {
        assertTrue(stockService.calculateDividentYield("GIN", 40) == 5);
    }

    @Test
    public void shouldThrowExceptionWhenStockIsNotAvailable() throws Exception {
        thrown.expect(SuperSimpleException.class);
        thrown.expectMessage("No such stock exists: HAF");
        stockService.calculateDividentYield("HAF", 100);
    }

    @Test
    public void shouldThrowExceptionIfPriceInArgumentIsZero() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(PRICE_VALUE_LESS_OR_EQUAL_ZERO.toString());
        stockService.calculateDividentYield("GIN", 0);
    }

    @Test
    public void shouldCalculatePERatio() throws Exception {
        assertTrue(stockService.calculatePERatio("POP", 5) == 3.125);
        assertTrue(stockService.calculatePERatio("ALE", 23) == 23);
        assertTrue(stockService.calculatePERatio("GIN", 40) == 8);
    }

    @Test
    public void shouldRecordNewTrade() throws Exception {
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 10, Indicator.BUY, 100), "POP");
        assertTrue(stockService.getDataStorage().getStockEntry("POP").getTradeRecords().size() == 1);
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 20, Indicator.BUY, 10), "POP");
        assertTrue(stockService.getDataStorage().getStockEntry("POP").getTradeRecords().size() == 2);
    }

    @Test
    public void shouldThrowErrorWhenTryingToAddZeroPriceValue() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(PRICE_VALUE_LESS_OR_EQUAL_ZERO.toString());
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 10, Indicator.BUY, 0), "ALE");
    }

    @Test
    public void shouldThrowErrorWhenTryingToAddZeroQuantityValue() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(QUANTITY_VALUE_LESS_OR_EQUAL_ZERO.toString());
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 0, Indicator.BUY, 10), "ALE");
    }

    @Test
    public void shouldThrowErrorWhenTryingToAddNullIndicator() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(OBJECT_IS_NULL.toString());
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 10, null, 10), "ALE");
    }

    @Test
    public void shouldThrowErrorWhenTryingToAddNullTradeRecord() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(OBJECT_IS_NULL.toString());
        stockService.addRecordTrade(null, "ALE");
    }

    @Test
    public void shouldThrowErrorWhenTryingToAddTradeRecordToUnsupportedStock() throws Exception {
        thrown.expect(SuperSimpleException.class);
        thrown.expectMessage("No such stock exists: UNSUPPORTED");
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 10, Indicator.BUY, 100), "UNSUPPORTED");
    }

    @Test
    public void shouldCalculatePriceFromFifteenMinutesInterval() throws Exception {
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 1, Indicator.BUY, 100), "POP");
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 1, Indicator.BUY, 100), "POP");
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 2, Indicator.BUY, 50), "POP");
        assertTrue(stockService.calculateVolumeWeightStockPriceInterval("POP", 15) == 75);
    }

    @Test
    public void shouldCalculatePriceFromFifteenMinutesIntervalNotCountOldTrades() throws Exception {
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis() - 16 * 60 * 1000), 1, Indicator.BUY, 100), "POP");
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 1, Indicator.BUY, 100), "POP");
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 1, Indicator.SELL, 100), "POP");
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 2, Indicator.BUY, 50), "POP");
        assertTrue(stockService.calculateVolumeWeightStockPriceInterval("POP", 15) == 75);
    }

    @Test
    public void shouldThrowErrorWhenTryingToCalculateUnsupportedStock() throws Exception {
        thrown.expect(SuperSimpleException.class);
        thrown.expectMessage("No such stock exists: UNSUPPORTED");
        stockService.calculateVolumeWeightStockPriceInterval("UNSUPPORTED", 15);
    }

    @Test
    public void shouldCalculateGBEC() throws Exception {
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 1, Indicator.BUY, 100), "POP");
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 1, Indicator.BUY, 80), "TEA");
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 2, Indicator.BUY, 90), "GIN");
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 2, Indicator.BUY, 50), "ALE");
        assertEquals(77.45, stockService.calculateGBEC(), 0.01);
        stockService.addRecordTrade(new TradeRecord(new Timestamp(System.currentTimeMillis()), 10, Indicator.BUY, 1), "POP");
        assertEquals(43.55, stockService.calculateGBEC(), 0.01);
    }
}