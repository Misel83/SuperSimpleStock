package all.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import all.DataStorage;
import all.enums.StockType;
import all.pojo.StockEntry;

/**
 * Creates bean with data from homework requirements.
 */
@Configuration
public class SpringConfig {

    @Bean
    public DataStorage dataRecord() {
        DataStorage tradeRecordBean = new DataStorage();
        tradeRecordBean.addStockEntry("TEA", new StockEntry("TEA", StockType.COMMON, 0, null, 100));
        tradeRecordBean.addStockEntry("POP", new StockEntry("POP", StockType.COMMON, 8, null, 100));
        tradeRecordBean.addStockEntry("ALE", new StockEntry("ALE", StockType.COMMON, 23, null, 60));
        tradeRecordBean.addStockEntry("GIN", new StockEntry("GIN", StockType.PREFERRED, 8, 2, 100));
        tradeRecordBean.addStockEntry("JOE", new StockEntry("JOE", StockType.COMMON, 13, null, 150));
        return tradeRecordBean;
    }

}
