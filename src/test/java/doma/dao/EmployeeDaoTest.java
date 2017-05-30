package doma.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.seasar.doma.jdbc.tx.TransactionManager;

import doma.DomaConfig;
import doma.DbResource;
import doma.entity.Employee;

public class EmployeeDaoTest {

    @Rule
    public final DbResource dbResource = new DbResource();

    private final EmployeeDao dao = new EmployeeDaoImpl();

    @Test
    public void testSelectById() {
        TransactionManager tm = DomaConfig.singleton().getTransactionManager();
        tm.required(() -> {
            Employee employee = dao.selectById(1);
            assertNotNull(employee);
            assertEquals("ALLEN", employee.name);
            assertEquals(Integer.valueOf(30), employee.age);
            assertEquals(Integer.valueOf(0), employee.version);
        });
    }

}
