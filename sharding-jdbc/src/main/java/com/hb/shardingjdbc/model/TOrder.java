package com.hb.shardingjdbc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 
 * @description 
 * @author zhaochengshui
 * @date 2023/4/16 
 */

@Data
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_order")
public class TOrder {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField(value = "`name`")
    private String name;

    @TableField(value = "price")
    private BigDecimal price;

    @TableField(value = "paytype")
    private String paytype;

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_PRICE = "price";

    public static final String COL_PAYTYPE = "paytype";
}