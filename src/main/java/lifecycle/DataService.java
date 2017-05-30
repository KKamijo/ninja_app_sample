package lifecycles;

import com.google.inject.Singleton;

import org.seasar.doma.jdbc.tx.TransactionManager;

import doma.DomaConfig;
import doma.dao.AppDao;

import com.google.inject.Inject;

import ninja.lifecycle.Start;
import ninja.lifecycle.Dispose;

@Singleton
public class DataService {

    @Inject
    private AppDao appDao;

    @Start(order = 90)
    public void startService() {
        System.out.println("----------------create----------------");
        TransactionManager tm = DomaConfig.singleton().getTransactionManager();
        tm.required(() -> {
            appDao.create();
        });
    }

    @Dispose(order = 90)
    public void stopService() {
        System.out.println("----------------drop----------------");
        TransactionManager tm = DomaConfig.singleton().getTransactionManager();
        tm.required(() -> {
            appDao.drop();
        });
    }

    // public Result getCount(Context ctx) {
    //     return Results.json(count.get());
    // }
}    