package com.xsl.task;

import com.xsl.task.vo.PageObject;
import com.xsl.task.vo.OrderReqVo;

public interface OrderInfoResource {

    PageObject getList(OrderReqVo orderReqVo);
}
