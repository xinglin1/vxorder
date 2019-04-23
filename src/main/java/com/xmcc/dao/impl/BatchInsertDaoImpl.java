package com.xmcc.dao.impl;

import com.xmcc.dao.BatchInsertDao;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author 张兴林
 * @date 2019-04-17 14:38
 */
@Service
public class BatchInsertDaoImpl<T> implements BatchInsertDao<T> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void batchInsert(List<T> list) {
        int size = list.size();
        //循环存入缓存区
        for (int i = 0; i < size; i++) {
            em.persist(list.get(i));
            //每一百条写入数据库，如果不足一百条就直接写入
            if (i % 100 == 0 || i == size - 1){
                em.flush();
                em.clear();
            }
        }
    }

}
