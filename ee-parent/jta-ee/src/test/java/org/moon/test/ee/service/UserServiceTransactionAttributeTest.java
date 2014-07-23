package org.moon.test.ee.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRequiredException;
import javax.ejb.embeddable.EJBContainer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.moon.test.ee.persistence.User;
import org.moon.test.ee.repository.UserRepository;

public class UserServiceTransactionAttributeTest {

	@EJB(beanName = "UserServiceDefaultImpl")
	private UserService serviceDefault;
	@EJB
	private UserRepository userRepository;
	@Resource
	private UserTransaction ux;
	@PersistenceContext(unitName = "hibernate-moon")
	private EntityManager em;

	private EJBContainer container;
	List<User> users = new ArrayList<User>();
	int count = 10;

	@Before
	public void setUp() throws Exception {
		Properties props = new Properties();
		props.put("openejb.conf.file", "src/main/resources/openejb.xml");
		container = EJBContainer.createEJBContainer(props);
		container.getContext().bind("inject", this);
		for (int i = 0; i < count; i++) {
			users.add(new User("user_" + i, i, "F", "10024125125125" + i, Calendar.getInstance()));
		}
	}

	@After
	public void tearDown() throws Exception {
		users.clear();
		userRepository.delete();
		container.close();
		users = null;
		userRepository = null;
		ux = null;
		serviceDefault = null;
		container = null;
	}

	@Test(expected = EJBTransactionRequiredException.class)
	public void testBatchSaveWithTransactionAttributeMandatory() throws Exception {
		assertEquals(0, userRepository.count());
		try {
			serviceDefault.batchSaveWithTransactionAttributeMandatory(users);
		} finally {
			assertEquals(0, userRepository.count());
		}
	}

	@Test
	public void testBatchSaveWithTransactionAttributeMandatoryWithUx() throws Exception {
		assertEquals(0, userRepository.count());
		ux.begin();
		serviceDefault.batchSaveWithTransactionAttributeMandatory(users);
		ux.commit();
		assertEquals(count, userRepository.count());
	}

	@Test
	public void testBatchSaveWithTransactionAttributeNever() throws Exception {
		assertEquals(0, userRepository.count());
		serviceDefault.batchSaveWithTransactionAttributeNever(users);
		assertEquals(count, userRepository.count());
	}

	@Test(expected = EJBException.class)
	public void testBatchSaveWithTransactionAttributeNeverWithUx() throws Exception {
		assertEquals(0, userRepository.count());
		try {
			ux.begin();
			// exception: transactions not support
			serviceDefault.batchSaveWithTransactionAttributeNever(users);
			ux.commit();
		} finally {
			assertEquals(0, userRepository.count());
		}
	}

	@Test
	public void testBatchSaveWithTransactionAttributeNotSupport() throws Exception {
		assertEquals(0, userRepository.count());
		serviceDefault.batchSaveWithTransactionAttributeNotSupport(users);
		assertEquals(count, userRepository.count());
	}

	@Test
	public void testBatchSaveWithTransactionAttributeNotSupportWithUx() throws Exception {
		assertEquals(0, userRepository.count());
		ux.begin();
		// exception
		serviceDefault.batchSaveWithTransactionAttributeNotSupport(users);
		ux.commit();
		assertEquals(count, userRepository.count());
	}

	@Test
	public void testBatchSaveWithTransactionAttributeRequired() throws Exception {
		assertEquals(0, userRepository.count());
		serviceDefault.batchSaveWithTransactionAttributeRequired(users);
		assertEquals(count, userRepository.count());
	}

	@Test
	public void testBatchSaveWithTransactionAttributeRequiredWithUx() throws Exception {
		assertEquals(0, userRepository.count());
		ux.begin();
		serviceDefault.batchSaveWithTransactionAttributeRequired(users);
		ux.commit();
		assertEquals(count, userRepository.count());
	}

	@Test
	public void testBatchSaveWithTransactionAttributeSupports() {
		assertEquals(0, userRepository.count());
		serviceDefault.batchSaveWithTransactionAttributeSupports(users);
		assertEquals(count, userRepository.count());
	}

	@Test
	public void testBatchSaveWithTransactionAttributeSupportsWithUx() throws Exception {
		assertEquals(0, userRepository.count());
		ux.begin();
		serviceDefault.batchSaveWithTransactionAttributeSupports(users);
		ux.commit();
		assertEquals(count, userRepository.count());
	}

	@Test
	public void testBatchSaveWithTransactionAttributeRequestNew() {
		assertEquals(0, userRepository.count());
		serviceDefault.batchSaveWithTransactionAttributeRequestNew(users);
		assertEquals(count, userRepository.count());
	}

	@Test
	public void testBatchSaveWithTransactionAttributeRequestNewWithUx() throws Exception {
		assertEquals(0, userRepository.count());
		ux.begin();
		serviceDefault.batchSaveWithTransactionAttributeRequestNew(users);
		ux.commit();
		assertEquals(count, userRepository.count());
		reflushDate();
		ux.begin();
		serviceDefault.batchSaveWithTransactionAttributeRequestNew(users);
		em.persist(new User("defualt_01", 30, "F", "08678899876", Calendar.getInstance()));
		// roll back this entityManager transaction, but can't roll back batch save transaction
		ux.rollback();
		assertEquals(count * 2, userRepository.count());
	}

	private void reflushDate() {
		users.clear();
		for (int i = 0; i < count; i++) {
			users.add(new User("double_" + i, 2 * (i + 1), "F", "08678899876" + i, Calendar.getInstance()));
		}
	}

}
