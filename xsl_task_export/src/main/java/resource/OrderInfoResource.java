package resource;

import vo.PageObject;
import vo.OrderReqVo;

public interface OrderInfoResource {

    PageObject getList(OrderReqVo orderReqVo);
}
