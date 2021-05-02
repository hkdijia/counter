package com.gotkx.counter.util;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 构造为单例
 */

@Component
public class DbUtil {

    private static DbUtil dbUtil = null;

    private DbUtil(){};

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @PostConstruct
    private void init(){
        dbUtil = new DbUtil();
        //第四部
        dbUtil.setSqlSessionTemplate(this.sqlSessionTemplate);
    }

    public static long getId(){
        Long res = dbUtil.getSqlSessionTemplate().selectOne("testMappper.queryBalance");
        return res == null ? -1 : res;
    }


    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
}
