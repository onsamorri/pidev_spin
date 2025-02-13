package tn.esprit.services;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {

    void add(T t) throws SQLException;
    /*void addI(T t) throws SQLException;*/
    List<T> returnList() throws SQLException;
    void delete(T entity) throws SQLException;
    void update(T entity) throws SQLException;
}
