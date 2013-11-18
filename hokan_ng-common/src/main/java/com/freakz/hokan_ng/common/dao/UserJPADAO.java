package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.User;
import com.freakz.hokan_ng.common.exception.HokanDAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * User: petria
 * Date: 11/8/13
 * Time: 5:52 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Repository("User")
@Transactional
public class UserJPADAO implements UserDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public UserJPADAO() {
  }

  @Override
  public User findUser(String nick) throws HokanDAOException {
    try {
      TypedQuery<User> query
          = entityManager.createQuery("SELECT ude FROM User ude WHERE ude.nick = :nick", User.class);
      query.setParameter("nick", nick);
      return query.getSingleResult();
    } catch (Exception e) {
      throw new HokanDAOException(e.getMessage());
    }
  }

  @Override
  public List<User> findUsers() throws HokanDAOException {
    try {
      TypedQuery<User> query
          = entityManager.createQuery("SELECT ude FROM User ude", User.class);
      return query.getResultList();
    } catch (Exception e) {
      throw new HokanDAOException(e.getMessage());
    }
  }

  @Override
  public List<User> getLoggedInUsers() throws HokanDAOException {
    try {
      TypedQuery<User> query
          = entityManager.createQuery("SELECT ude FROM User ude WHERE ude.loggedIn > 0", User.class);
      return query.getResultList();
    } catch (Exception e) {
      throw new HokanDAOException(e.getMessage());
    }
  }

  @Override
  public void resetLoggedInUsers() throws HokanDAOException {
    try {
      Query query = entityManager.createQuery("UPDATE User ude SET ude.loggedIn = 0");
      query.executeUpdate();
    } catch (Exception e) {
      throw new HokanDAOException(e.getMessage());
    }
  }

  @Override
  public void resetOlpos() throws HokanDAOException {
    try {
      Query query = entityManager.createQuery("UPDATE User ude SET ude.olpo= 0");
      query.executeUpdate();
    } catch (Exception e) {
      throw new HokanDAOException(e.getMessage());
    }
  }

  @Override
  public User updateUser(User user) throws HokanDAOException {
    try {
      return entityManager.merge(user);
    } catch (Exception e) {
      throw new HokanDAOException(e.getMessage());
    }
  }
}
