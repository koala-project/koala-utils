package com.koala.utils.gateway.document;


import com.koala.utils.gateway.annotation.Description;

import java.util.List;

@Description("返回值结构描述")
public class RespStruct {
    @Description("结构名")
    public String name;
    @Description("分组名")
    public String groupName;
    @Description("成员")
    public List<FieldInfo> fieldList;
}
