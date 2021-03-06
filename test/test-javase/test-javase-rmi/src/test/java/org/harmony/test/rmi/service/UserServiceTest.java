package org.harmony.test.rmi.service;

import static org.junit.Assert.*;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import org.harmony.test.rmi.persistence.User;
import org.harmony.test.rmi.service.UserService;
import org.harmony.test.rmi.service.impl.SimpleUserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserServiceTest {

    private String address = "rmi://localhost:9696/UserService";

    @Before
    public void setUp() throws Exception {
        UserService service = new SimpleUserService();
        LocateRegistry.createRegistry(9696);
        Naming.rebind(address, service);
    }

    @Test
    public void testGetUser() throws Exception {
        UserService service = (UserService) Naming.lookup(address);
        assertNotNull(service);
        User u1 = service.getUser("test");
        assertEquals(21, u1.getAge());
        //User u2 = new User("admin", 23);
        //User u2Slef = service.returnSlef(u2);
        //assertNotEquals(u2, u2Slef);
    }

    @After
    public void tearDown() throws Exception {

    }

}
