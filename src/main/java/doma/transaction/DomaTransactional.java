package doma.transaction;

import java.lang.annotation.ElementType; 
import java.lang.annotation.Inherited; 
import java.lang.annotation.Retention; 
import java.lang.annotation.RetentionPolicy; 
import java.lang.annotation.Target; 
 
import org.seasar.doma.jdbc.tx.TransactionAttribute; 


/**
 * Doma Transactional Annotation
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE }) 
@Retention(RetentionPolicy.RUNTIME) 
@Inherited 
public @interface DomaTransactional { 
 
    TransactionAttribute attribute() default TransactionAttribute.REQURED; 
}