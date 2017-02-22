package com.sample.xml;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;

import com.sample.repo.dao.StockDaoImpl;
import com.sample.repo.domain.Stock;
import com.sample.repo.domain.StockDetail;

public class TestMain {

    private static StockDaoImpl dao = new StockDaoImpl();
    public static void main(String[] args) {
        System.out.println("Hibernate one to one (XML mapping)");

        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        session.beginTransaction();

        Stock stock = new Stock();

        String id = UUID.randomUUID().toString();
        System.out.println(id);
        stock.setStockCode(id);
        stock.setStockName("ENMX"+ id);

        StockDetail stockDetail = new StockDetail();
        stockDetail.setCompName("GENTING Malaysia");
        stockDetail.setCompDesc("Best resort in the world");
        stockDetail.setRemark("Nothing Special");
        stockDetail.setListedDate(new Date());

        stock.setStockDetail(stockDetail);
        stockDetail.setStock(stock);

        session.save(stock);
        session.getTransaction().commit();


        create() ;
        find(51);
        testGetList();
    }
    public static void create() {
        Stock stock = new Stock();
        String id = UUID.randomUUID().toString();
        System.out.println(id);
        stock.setStockCode(id);
        stock.setStockName("ENMX"+ id);
        
        dao.addStock(stock);
        System.out.println("created successfully " + id);
        
    }
    public static void find(int id) {
        System.out.println("===========find by id============");
        Stock stock = dao.findStockById(id);
            System.out.println(String.format("%s; %s", stock.getStockId(), stock.getStockCode()));
    }
    public static void testGetList() {
        System.out.println("===========List all items============");
        List<Stock> list = dao.list();
        for (Stock stock : list) {
            System.out.println(String.format("%s; %s", stock.getStockId(), stock.getStockCode()));
        }
    }

}
