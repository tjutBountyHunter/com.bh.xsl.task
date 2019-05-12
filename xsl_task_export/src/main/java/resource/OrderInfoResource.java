package resource;

import vo.OrderReqVo;
import vo.PageObject;


public interface OrderInfoResource {

    PageObject getList(OrderReqVo orderReqVo);
}
