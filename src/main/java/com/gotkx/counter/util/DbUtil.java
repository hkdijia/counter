package com.gotkx.counter.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gotkx.counter.bean.res.Account;
import com.gotkx.counter.bean.res.OrderInfo;
import com.gotkx.counter.bean.res.PosiInfo;
import com.gotkx.counter.bean.res.TradeInfo;
import com.gotkx.counter.cache.CacheType;
import com.gotkx.counter.cache.RedisStringCache;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import thirdpart.order.OrderCmd;
import thirdpart.order.OrderStatus;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 构造为单例
 */

@Component
public class DbUtil {

    private static DbUtil dbUtil = null;

    private DbUtil(){}

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
                ImmutableMap.of("uid",uid,"password",password)
        );
    }

    public static void updateLoginTime(long uid,String nowDate, String nowTime){
        dbUtil.getSqlSessionTemplate().update(
                "userMapper.updateAccountLoginTime",
                ImmutableMap.of(
                        "uid",uid,
                        "modifyDate",nowDate,
                        "modifyTime",nowTime
                )
        );
    }


    public static int updatePwd(long uid, String oldPwd, String newPwd) {
        return dbUtil.getSqlSessionTemplate().update(
                        "userMapper.updatePwd",
                                ImmutableMap.of("uid", uid,
                                "newPwd", newPwd,
                                "oldPwd", oldPwd)
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


    //////////////////////////////资金类////////////////////////////////////////
    public static long getBalance(long uid){
        Long res = dbUtil.getSqlSessionTemplate()
                .selectOne("orderMapper.queryBalance",
                        ImmutableMap.of("uid",uid));
        if(res == null){
            return -1;
        }else {
            return res;
        }
    }

    //////////////////////////////持仓类////////////////////////////////////////
    public static List<PosiInfo> getPosiList(long uid){
        //查缓存
        String suid = Long.toString(uid);
        String posiS = RedisStringCache.get(suid, CacheType.POSI);
        if(StringUtils.isEmpty(posiS)){
            //未查到 查库
            List<PosiInfo> tmp = dbUtil.getSqlSessionTemplate().selectList(
                    "orderMapper.queryPosi",
                    ImmutableMap.of("uid", uid)
            );
            List<PosiInfo> result = CollectionUtils.isEmpty(tmp) ? Lists.newArrayList() : tmp;
            //更新缓存
            RedisStringCache.cache(suid,JsonUtil.toJson(result),CacheType.POSI);
            return result;
        }else {
            //查到 命中缓存
            return JsonUtil.fromJsonArr(posiS,PosiInfo.class);
        }
    }

    //////////////////////////////委托类////////////////////////////////////////
    public static List<OrderInfo> getOrderList(long uid){
        //查缓存
        String suid = Long.toString(uid);
        String orderS = RedisStringCache.get(suid, CacheType.ORDER);
        if(StringUtils.isEmpty(orderS)){
            //未查到 查库
            List<OrderInfo> tmp = dbUtil.getSqlSessionTemplate().selectList(
                    "orderMapper.queryOrder",
                    ImmutableMap.of("uid", uid)
            );
            List<OrderInfo> result = CollectionUtils.isEmpty(tmp) ? Lists.newArrayList() : tmp;
            //更新缓存
            RedisStringCache.cache(suid,JsonUtil.toJson(result),CacheType.ORDER);
            return result;
        }else {
            //查到 命中缓存
            return JsonUtil.fromJsonArr(orderS,OrderInfo.class);
        }
    }

    //////////////////////////////成交类////////////////////////////////////////
    public static List<TradeInfo> getTradeList(long uid){
        //查缓存
        String suid = Long.toString(uid);
        String tradeS = RedisStringCache.get(suid, CacheType.TRADE);
        if(StringUtils.isEmpty(tradeS)){
            //未查到 查库
            List<TradeInfo> tmp = dbUtil.getSqlSessionTemplate().selectList(
                    "orderMapper.queryTrade",
                    ImmutableMap.of("uid", uid)
            );
            List<TradeInfo> result = CollectionUtils.isEmpty(tmp) ? Lists.newArrayList() : tmp;
            //更新缓存
            RedisStringCache.cache(suid,JsonUtil.toJson(result), CacheType.TRADE);
            return result;
        }else {
            //查到 命中缓存
            return JsonUtil.fromJsonArr(tradeS,TradeInfo.class);
        }
    }

    //////////////////////////////订单处理类///////////////////////////////////////
    public static int saveOrder(OrderCmd orderCmd){
        Map<String, Object> param = Maps.newHashMap();
        param.put("uid",orderCmd.uid);
        param.put("code",orderCmd.code);
        param.put("direction",orderCmd.direction.getDirection());
        param.put("type",orderCmd.orderType.getType());
        param.put("price",orderCmd.price);
        param.put("oCount",orderCmd.volume);
        param.put("status", OrderStatus.NOT_SET.getCode());
        param.put("data",TimeformatUtil.yyyyMMdd(orderCmd.timestamp));
        param.put("time",TimeformatUtil.hhMMss(orderCmd.timestamp));

        int count = dbUtil.getSqlSessionTemplate().insert(
                "orderMapper.saveOrder",param
        );
        //判断是否成功
        if(count > 0){
            return Integer.parseInt(param.get("id").toString());
        }else {
            return -1;
        }
    }



    //////////////////////////////股票信息查询///////////////////////////////////////
    public static List<Map<String,Object>> queryAllSotckInfo(){
        return dbUtil.getSqlSessionTemplate()
                .selectList("stockMapper.queryStock");
    }

}
