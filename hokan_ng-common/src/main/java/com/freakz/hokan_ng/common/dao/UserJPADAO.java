package com.freakz.hokan_ng.common.dao;

import com.freakz.hokan_ng.common.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * User: petria
 * Date: 11/8/13
 * Time: 5:52 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Repository("User")
public class UserJPADAO implements UserDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public User findUser(String nick) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public List<User> findUsers(int limit) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public List<User> getLoggedInUsers() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void resetLoggedInUsers() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void resetOlpos() {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
