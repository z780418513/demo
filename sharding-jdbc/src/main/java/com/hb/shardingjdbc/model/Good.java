package com.hb.shardingjdbc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/14
 */

@Data
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "good")
public class Good {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField(value = "`name`")
    private String name;

    @TableField(value = "price")
    private String price;

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_PIRCE = "pirce";
}
