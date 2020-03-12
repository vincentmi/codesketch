package com.vnzmi.tool.model.mapper;

import com.vnzmi.tool.ArrayUtil;
import com.vnzmi.tool.model.FieldInfo;

import java.util.HashMap;
import java.util.Iterator;

public class TitleResolver {
    public final static HashMap<String,String[]> guessTitles = new HashMap<String,String[]>(){{
        put("ID",new String[] {"id"});
        put("电子邮件",new String[] {"email","email_address","mail"});
        put("地址",new String[] {"addr","address"});
        put("手机号码",new String[] {"mobile","cellphone","mobile_phone","mobilephone"});
        put("电话号码",new String[] {"phone","tel"});
        put("创建时间",new String[] {"created_at","inserted_at","created"});
        put("创建人",new String[] {"created_by","inserted_by","created_user"});
        put("删除时间",new String[] {"deleted_at","removed_at","deleted"});
        put("删除人",new String[] {"deleted_by","removed_by","deleted_user","removed_user"});
        put("更新时间",new String[] {"updated_at","updated","update_time","updated_time"});
        put("更新人",new String[] {"updated_by","updated_user"});
        put("年龄",new String[] {"age"});
        put("生日",new String[] {"dob","birthday","birth"});
        put("名称",new String[] {"name"});
        put("全名",new String[] {"full_name"});
        put("姓",new String[] {"surname","family_name","last_name"});
        put("名",new String[] {"first_name","given_name"});
        put("标题",new String[] {"title","subject"});
        put("昵称",new String[] {"nickname","nick"});
        put("别名",new String[] {"alias"});
        put("性别",new String[] {"gender"});
        put("描述",new String[] {"description","desc"});
        put("是否激活",new String[] {"active","is_active","enable","enabled","is_enable","is_enabled"});
        put("状态",new String[] {"status","state"});
        put("备注",new String[] {"memo","remark"});
        put("数量",new String[] {"amount","total"});
        put("位置序号",new String[] {"position"});
        put("排序编号",new String[] {"sort","sort_order","weight"});
        put("作者",new String[] {"author","author_id"});
        put("登录名",new String[] {"account","username","login_name","loginname"});
        put("密码",new String[] {"password"});
    }};

    /**
     * 获取可能的栏位名称
     * @return
     */
    public static String getGuessedTitle(FieldInfo info)
    {
        String title = null;
        String name = info.getName();
        Iterator<String> keys = guessTitles.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            String[] compare = guessTitles.get(key);
            if (ArrayUtil.inArray(name, compare)) {
                title = key;
                break;
            }
        }

        if(title == null && info.getComment() != "" && info.getComment().length() < 6) {
            return info.getComment();
        }else{
            return info.getName();
        }
    }
}
