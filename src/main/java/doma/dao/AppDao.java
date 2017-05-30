package doma.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Script;

import doma.DomaConfig;

@Dao(config = DomaConfig.class)
public interface AppDao {

    @Script
    void create();

    @Script
    void drop();

}
