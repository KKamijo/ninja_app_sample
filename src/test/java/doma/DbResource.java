package doma;

import org.junit.rules.ExternalResource;
import org.seasar.doma.jdbc.tx.TransactionManager;

import doma.dao.AppDao;
import doma.dao.AppDaoImpl;

public class DbResource extends ExternalResource {

    private AppDao dao = new AppDaoImpl();

    @Override
    protected void before() throws Throwable {
        TransactionManager tm = DomaConfig.singleton().getTransactionManager();
        tm.required(() -> {
            dao.create();
        });
    }

    @Override
    protected void after() {
        TransactionManager tm = DomaConfig.singleton().getTransactionManager();
        tm.required(() -> {
            dao.drop();
        });
    }

}
