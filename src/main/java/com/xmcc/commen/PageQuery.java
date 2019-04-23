package com.xmcc.commen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;


/**
 * @author 张兴林
 * @date 2019-04-17 20:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQuery {

    @Min(value = 1,message = "页数不能小于1")
    private Integer page = 1;

    private Integer size = 10;


}
