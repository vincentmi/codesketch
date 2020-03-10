# Code Sketch

Vincent Mi (miwenshu@gmail.com)


用于根据数据库设计生成骨架代码.

## 使用

将模板文件拷贝到 ```~/.codesketch/templates```下.


## 模板变量

模板使用 FreeMarker 语法请参考参考 [http://freemarker.foofun.cn/ref.html](http://freemarker.foofun.cn/ref.html)

#### 基本变量
| 变量 | 类型 | 示例 | 说明 |
|---| --- | ---- |---|
| ```${projectPath}```  |String | /User/vicnent/dev/blog| 目标项目路径 |
| ```${table}```  |String | User_Info | 目标项目路径 |
| ```${modelCamel}```  |String | userInfo|表名驼峰 |
| ```${model}```  |String | UserInfo|表名模型名称 |
| ```${modelLine}```  |String | user_info|表名下划线间隔 |
| ```${FieldInfo}```  | ArrayList<FieldInfo> | FieldInfo{}|栏位信息 |
| ```${schema}```  | string | my_db|数据库名称 |

#### FieldInfo

| 变量 | 类型 | 示例 | 说明 |
|---| --- | ---- |---|
| ```${name}```  |String | id | 栏位名称 |
| ```${nameCamel}```  |String | id | 栏位名称驼峰 |
| ```${defaultValue}```  |String | 1 | 默认值 |
| ```${nullable}```  |boolean | false | 是否为空 |
| ```${dataType}```  |String | int | 数据类型 |
| ```${maxLength}```  |int | 10 | 长度 |
| ```${numericPrecision}```  |int | 10 | 精度 |
| ```${numericScale}```  |int | 2 | 小数位数 |
| ```${dataTypeStr}```  |String | int(11) | 数据类型字符串定义 |
| ```${key}```  | String | PRI | 键定义 |
| ```${extra}```  | String | AUTO_INCREMENT  | 额外信息|
| ```${comment}```  | String | 用户名称  | 注释|


