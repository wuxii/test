package org.moon.test.jpa;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.moon.test.jpa.proxy.EntityManagerProxy;
import org.moon.test.jpa.query.PersistenceUnitNameNotFindException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DB {

	private static Logger log = Logger.getLogger(DB.class);
	private static EntityManagerFactory entityManagerFactory;
	private static List<EntityManager> entityManagerList = new ArrayList<EntityManager>();
	private static String persistenceUnitName;
	private static int maxSize = 50;
	private static int current = 0;

	public static EntityManager createEntityManager() {
		try {
			return createEntityManager(persistenceUnitName);
		} catch (Exception e) {
			log.error("create EntityManager fail");
			return null;
		}
	}

	/**
	 * @param persistenceUnitName
	 * @return
	 * @throws PersistenceUnitNameNotFindException
	 */
	public static EntityManager createEntityManager(String persistenceUnitName) {
		if (entityManagerFactory == null) {
			synchronized (DB.class) {
				if ((DB.persistenceUnitName = persistenceUnitName) == null) {
					try {
						DB.persistenceUnitName = getDefaultPersistenceUnitName();
					} catch (PersistenceUnitNameNotFindException e) {
						log.error(e);
						throw e;
					}
				} else {
					if (!containPersistenceUnitName(persistenceUnitName))
						throw new PersistenceUnitNameNotFindException("persistence unit name " + persistenceUnitName + " not find");
				}
				entityManagerFactory = Persistence.createEntityManagerFactory(DB.persistenceUnitName);
			}
		}
		return findEntityManagerInList();
	}

	private static EntityManager findEntityManagerInList() {
		if (entityManagerList.size() <= maxSize) {
			EntityManager proxyEntityManager = (EntityManager) new EntityManagerProxy().bind(entityManagerFactory.createEntityManager());
			entityManagerList.add((EntityManager) proxyEntityManager);
			return proxyEntityManager;
		}
		return entityManagerList.get(current = (++current % maxSize));
	}

	protected static boolean containPersistenceUnitName(String persistenceUnitName) {
		String classPath = Thread.currentThread().getContextClassLoader().getResource("").getFile();
		System.out.println(classPath);
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse(new File(classPath + "META-INF/persistence.xml"));
			NodeList nodeList = document.getDocumentElement().getElementsByTagName("persistence-unit");
			for (int i = 0; i < nodeList.getLength(); i++) {
				NamedNodeMap attributes = nodeList.item(i).getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					Node attr = attributes.item(j);
					if ("name".equals(attr.getNodeName()) && persistenceUnitName.equals(attr.getNodeValue())) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}
	
	private static String getDefaultPersistenceUnitName() {
		String classPath = Thread.currentThread().getContextClassLoader().getResource("").getFile();
		System.out.println(classPath);
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse(new File(classPath + "META-INF/persistence.xml"));
			Node node = document.getDocumentElement().getElementsByTagName("persistence-unit").item(0);
			NamedNodeMap attributes = node.getAttributes();
			for (int i = attributes.getLength() - 1; i >= 0; i--) {
				Node attr = attributes.item(i);
				if ("name".equals(attr.getNodeName())) {
					return attr.getNodeValue();
				}
			}
			throw new PersistenceUnitNameNotFindException("default persistence unit name not find!");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}