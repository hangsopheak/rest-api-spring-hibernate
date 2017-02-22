package com.sample.repo.domain;

public class Stock implements java.io.Serializable {

    private Integer stockId;
    private String stockCode;
    private String stockName;
    private StockDetail stockDetail;
    /**
     * @return the stockId
     */
    public Integer getStockId() {
        return stockId;
    }
    /**
     * @param stockId the stockId to set
     */
    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }
    /**
     * @return the stockCode
     */
    public String getStockCode() {
        return stockCode;
    }
    /**
     * @param stockCode the stockCode to set
     */
    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }
    /**
     * @return the stockName
     */
    public String getStockName() {
        return stockName;
    }
    /**
     * @param stockName the stockName to set
     */
    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
    /**
     * @return the stockDetail
     */
    public StockDetail getStockDetail() {
        return stockDetail;
    }
    /**
     * @param stockDetail the stockDetail to set
     */
    public void setStockDetail(StockDetail stockDetail) {
        this.stockDetail = stockDetail;
    }


}
