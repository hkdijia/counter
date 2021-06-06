package com.gotkx.counter.util;

import com.google.common.collect.ImmutableMap;
import com.gotkx.counter.bean.res.Account;
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

    //////////////////////////////身份认证/////////////////////////////////////
    public static Account queryAccount(long uid, String password){
        return dbUtil.getSqlSessionTemplate().selectOne(
                "userMapper.queryAccount",
                ImmutableMap.of("UId",uid,"Password",password)
        );
    }

    public static void updateLoginTime(long uid,String nowDate, String nowTime){
        dbUtil.getSqlSessionTemplate().update(
                "userMapper.updateAccountLoginTime",
                ImmutableMap.of(
                        "UId",uid,
                        "modifyDate",nowDate,
                        "modifyTime",nowTime
                )
        );
    }


    public static int updatePwd(long uid, String oldPwd, String newPwd) {
        return dbUtil.getSqlSessionTemplate().update(
                        "userMapper.updatePwd",
                                ImmutableMap.of("UId", uid,
                                "NewPwd", newPwd,
                                "OldPwd", oldPwd)
        );
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
