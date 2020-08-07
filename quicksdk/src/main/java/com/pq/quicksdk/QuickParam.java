package com.pq.quicksdk;
import com.quicksdk.entity.GameRoleInfo;
import com.quicksdk.entity.OrderInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class QuickParam {
    public static GameRoleInfo getRoleInfo(String jsonStr) throws JSONException {
        GameRoleInfo g=new GameRoleInfo();
        JSONObject obj=new JSONObject(jsonStr);
        g.setServerID(obj.getString("serverID"));//数字字符串，不能含有中文字符
        g.setServerName(obj.getString("serverName"));
        g.setGameRoleName(obj.getString("gameRoleName"));
        g.setGameRoleID(obj.getString("gameRoleID"));
        g.setGameBalance(obj.getString("gameBalance"));
        g.setVipLevel(obj.getString("vipLevel"));//设置当前用户vip等级，必须为数字整型字符串,请勿传"vip1"等类似字符串
        g.setGameUserLevel(obj.getString("gameUserLevel"));//设置游戏角色等级
        g.setPartyName(obj.getString("partyName"));//设置帮派名称
        g.setRoleCreateTime(obj.getString("roleCreateTime")); //UC，当乐与1881，TT渠道必传，值为10位数时间戳
        g.setPartyId(obj.getString("partyId")); //360渠道参数，设置帮派id，必须为整型字符串
        g.setGameRoleGender(obj.getString("gameRoleGender"));//360渠道参数
        g.setGameRolePower(obj.getString("gameRolePower")); //360,TT语音渠道参数，设置角色战力，必须为整型字符串
        g.setPartyRoleId(obj.getString("partyRoleId")); //360渠道参数，设置角色在帮派中的id
        g.setPartyRoleName(obj.getString("partyRoleName")); //360渠道参数，设置角色在帮派中的名称
        g.setProfessionId(obj.getString("professionId")); //360渠道参数，设置角色职业id，必须为整型字符串
        g.setProfession(obj.getString("profession")); //360渠道参数，设置角色职业名称
        g.setFriendlist(obj.getString("friendlist")); //360渠道参数，设置好友关系列表，格式请参考：http://open.quicksdk.net/help/detail/aid/190
        return g;
    }
    public static GameRoleInfo getPayRoleInfo(String jsonStr) throws JSONException {
        GameRoleInfo g=new GameRoleInfo();
        JSONObject jobj=new JSONObject(jsonStr);
        g.setServerID(jobj.getString("serverID"));//数字字符串，不能含有中文字符
        g.setServerName(jobj.getString("serverName"));
        g.setGameRoleName(jobj.getString("gameRoleName"));
        g.setGameRoleID(jobj.getString("gameRoleID"));
        g.setGameBalance(jobj.getString("gameBalance"));
        g.setVipLevel(jobj.getString("vipLevel"));//设置当前用户vip等级，必须为数字整型字符串,请勿传"vip1"等类似字符串
        g.setGameUserLevel(jobj.getString("gameUserLevel"));//设置游戏角色等级
        g.setPartyName(jobj.getString("partyName"));//设置帮派名称
        return g;
    }
    public static OrderInfo getOrderInfo(String jsonStr) throws  JSONException{
        OrderInfo o=new OrderInfo();
        JSONObject jobj=new JSONObject(jsonStr);
        o.setCpOrderID(jobj.getString("cpOrderID"));
        o.setGoodsName(jobj.getString("goodsName"));//商品名称，不带数量
        o.setCount(jobj.getInt("count"));//游戏币数量
        o.setAmount(jobj.getInt("amount"));
        o.setGoodsID(jobj.getString("goodsID"));
        o.setGoodsDesc(jobj.getString("goodsDesc"));
        o.setPrice(jobj.getDouble("price"));
        o.setExtrasParams(jobj.getString("extrasParams"));
        return o;
    }
}
