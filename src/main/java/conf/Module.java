/**
 * Copyright (C) 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package conf;

import static com.google.inject.matcher.Matchers.*;

import doma.DomaConfig;
import doma.dao.EmployeeDao;
import doma.dao.EmployeeDaoImpl;
import doma.dao.AppDao;
import doma.dao.AppDaoImpl;
import lifecycles.DataService;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers; 
import com.google.inject.Singleton;

import org.aopalliance.intercept.MethodInterceptor; 

import org.seasar.doma.jdbc.tx.TransactionManager;
import org.seasar.doma.jdbc.tx.TransactionIsolationLevel; 

import doma.transaction.DomaLocalTxInterceptor;
import doma.transaction.DomaTransactional;


/**
 * Defining Injection
 *
 */
@Singleton
public class Module extends AbstractModule {
    

    protected void configure() {
        
        // Dao
        bind(EmployeeDao.class).to(EmployeeDaoImpl.class);
        bind(AppDao.class).to(AppDaoImpl.class);

        // Lifecycle
        bind(DataService.class);

        // トランザクションマネージャ 
        bind(TransactionManager.class).toInstance(DomaConfig.singleton().getTransactionManager()); 
        
        // トランザクション AOP
        MethodInterceptor interceptor = new DomaLocalTxInterceptor(TransactionIsolationLevel.READ_COMMITTED); 
        requestInjection(interceptor); 
        bindInterceptor(any(), annotatedWith(DomaTransactional.class), interceptor); 
    }

}
