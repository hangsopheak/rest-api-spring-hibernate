package com.sample.repo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.sample.repo.domain.Stock;
import com.sample.xml.HibernateUtil;
@Repository
public class StockDaoImpl {
    public List<Stock> list() {
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            Query query = session.createQuery("from Stock s");
 
            List queryList = query.list();
            if (queryList != null && queryList.isEmpty()) {
                return null;
            } else {
                System.out.println("list " + queryList);
                return (List<Stock>) queryList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
 
    public Stock findStockById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            Query query = session.createQuery("from Stock s where s.id = :id");
            query.setParameter("id", id);
 
            List queryList = query.list();
            if (queryList != null && queryList.isEmpty()) {
                return null;
            } else {
                return (Stock) queryList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
 
    public void updateStock(Stock Stock) {
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            session.saveOrUpdate(Stock);
            session.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
 
    public Stock addStock(Stock Stock) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            System.out.println("session : "+session);
            transaction = session.beginTransaction();
            session.save(Stock);
            transaction.commit();
            return Stock;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
    public void deleteStock(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            Transaction beginTransaction = session.beginTransaction();
            Query createQuery = session.createQuery("delete from Stock s where s.id =:id");
            createQuery.setParameter("id", id);
            createQuery.executeUpdate();
            beginTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
