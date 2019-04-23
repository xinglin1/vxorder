package com.xmcc.dao;

import java.util.List;

/**
 * @author 张兴林
 * @date 2019-04-17 14:37
 */
public interface BatchInsertDao<T>  {

    void batchInsert(List<T> list);

}
