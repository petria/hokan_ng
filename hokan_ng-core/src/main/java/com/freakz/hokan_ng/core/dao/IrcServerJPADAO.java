package com.freakz.hokan_ng.core.dao;

import com.freakz.hokan_ng.common.entity.IrcServer;
import com.freakz.hokan_ng.core.exception.HokanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Repository(value = "ircServerDAO")
public class IrcServerJPADAO implements IrcServerDAO {

	private static final Logger logger = LoggerFactory.getLogger(IrcServerJPADAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public IrcServerJPADAO() {
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<IrcServer> getIrcServers() throws HokanException {
		try {
			Query query = getEntityManager().createNativeQuery("select * from IrcServer", IrcServer.class);
			List<IrcServer> resultList = query.getResultList();
			return resultList;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new HokanException(e.getMessage());

		}
	}

	@Override
	public IrcServer createIrcServer(String network, String server, int port, String password, boolean useThrottle, String channelsToJoin) throws HokanException {
		try {
			IrcServer ircServer = new IrcServer();
			ircServer.setNetwork(network);
			ircServer.setServer(server);
			ircServer.setPort(port);
			ircServer.setServerPassword(password);
			ircServer.setUseThrottle(useThrottle ? 1 : 0);
			ircServer.setChannelsToJoin(channelsToJoin);
			entityManager.persist(ircServer);
			return ircServer;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new HokanException(e.getMessage());
		}
	}

}
