package com.hb.entiy;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Good {
    private Long id;
    private String name;
    private BigDecimal price;
}
