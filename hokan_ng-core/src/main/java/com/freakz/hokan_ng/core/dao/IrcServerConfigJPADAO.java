package com.freakz.hokan_ng.core.dao;

import com.freakz.hokan_ng.common.entity.IrcServerConfig;
import com.freakz.hokan_ng.core.exception.HokanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Date: 3.6.2013
 * Time: 11:45
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Repository(value = "ircServerConfigDAO")
@Slf4j
public class IrcServerConfigJPADAO implements IrcServerConfigDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public IrcServerConfigJPADAO() {
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<IrcServerConfig> getIrcServerConfigs() throws HokanException {
    try {
      Query query = getEntityManager().createNativeQuery("select * from IrcServerConfig", IrcServerConfig.class);
      List<IrcServerConfig> resultList = query.getResultList();
      return resultList;

    } catch (Exception e) {
//      log.error(e.getMessage(), e);
      throw new HokanException(e.getMessage());

    }
  }

  @Override
  public IrcServerConfig createIrcServerConfig(String network, String server, int port, String password, boolean useThrottle, String channelsToJoin) throws HokanException {
    try {
      IrcServerConfig ircServerConfig = new IrcServerConfig();
      ircServerConfig.setNetwork(network);
      ircServerConfig.setServer(server);
      ircServerConfig.setPort(port);
      ircServerConfig.setServerPassword(password);
      ircServerConfig.setUseThrottle(useThrottle ? 1 : 0);
      ircServerConfig.setChannelsToJoin(channelsToJoin);
      entityManager.persist(ircServerConfig);
      return ircServerConfig;
    } catch (Exception e) {
//      log.error(e.getMessage(), e);
      throw new HokanException(e.getMessage());
    }
  }

}
