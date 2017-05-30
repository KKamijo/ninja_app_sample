package doma.transaction;

import java.lang.reflect.Method; 
import java.util.function.Supplier; 
 
import org.aopalliance.intercept.MethodInterceptor; 
import org.aopalliance.intercept.MethodInvocation; 
import org.seasar.doma.jdbc.tx.TransactionManager; 
import org.seasar.doma.jdbc.tx.TransactionIsolationLevel; 
 
import com.google.inject.Inject; 


/**
 * Doma Transaction Manager Interceptor
 *
 */
public class DomaLocalTxInterceptor implements MethodInterceptor { 
  
    @Inject 
    private TransactionManager transactionManager;

    private static TransactionIsolationLevel transactionIsolationLevel;
 
    public DomaLocalTxInterceptor(TransactionIsolationLevel transactionIsolationLevel) {
        this.transactionIsolationLevel = transactionIsolationLevel;
    }

    /**
     * {@inheritDoc} 
     */ 
    @Override 
    public Object invoke(MethodInvocation invocation) throws Throwable { 
        DomaTransactional transactional = readTransactionMetadata(invocation); 
 
        Supplier<Object> supplier = () -> { 

            try { 
                return invocation.proceed(); 
            } catch (Throwable t) { 
                throw new RuntimeException(t); 
            } 
        }; 
 
        Object result; 
        switch (transactional.attribute()) { 
        case REQURED: 
            result = transactionManager.required(transactionIsolationLevel, supplier); 
            break; 
        case NOT_SUPPORTED: 
            result = transactionManager.notSupported(transactionIsolationLevel, supplier); 
            break; 
        case REQURES_NEW: 
        default: 
            result = transactionManager.requiresNew(transactionIsolationLevel, supplier); 
            break; 
        } 
 
        return result; 
    } 
 
 
    private DomaTransactional readTransactionMetadata( 
            MethodInvocation methodInvocation) { 
        Method method = methodInvocation.getMethod(); 
        Class<?> targetClass = methodInvocation.getThis().getClass(); 
 
        DomaTransactional transactional = method 
                .getAnnotation(DomaTransactional.class); 
        if (transactional == null) { 
            transactional = targetClass.getAnnotation(DomaTransactional.class); 
        } 
 
        if (transactional == null) { 
            transactional = Internal.class.getAnnotation(DomaTransactional.class);
        } 
 
        return transactional; 
    } 
 
    @DomaTransactional 
    private static class Internal { 
    } 
}