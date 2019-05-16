package resourceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import resource.OrderInfoResource;
import service.XslOrderService;
import vo.PageObject;
import vo.OrderReqVo;

@Controller
public class OrderInfoResourceImpl implements OrderInfoResource {
    @Autowired
    private XslOrderService xslOrderService;
    public PageObject getList(OrderReqVo orderReqVo) {
        return xslOrderService.selectOrderAll(orderReqVo);
    }
}
