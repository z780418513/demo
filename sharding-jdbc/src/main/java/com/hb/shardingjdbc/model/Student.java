package com.hb.shardingjdbc.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
* 作者： duay
* 时间：2022/06/09
* 描述：shardingjdbc实体类
*/
@Data
@TableName("student")
public class Student {
    /** 主键id
    * org.springframework.dao.DuplicateKeyException:
    * Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException:
    * Duplicate entry '0' for key 'PRIMARY'
    *
    * 当类型是 int和不是包装类long时，shardingjdbc的雪花算法不会起效，会报上面的错
    * 主键id是Long，数据库的id可以是varchar类型
    */
    private Long id;
    /** 名字 */
    private String name;
    /** 老师id */
    private int tearchid;
    /** 状态 */
    private String status;

    private String className;
    private String stNo;
}
