package resourceImpl;

import com.xsl.task.OrderInfoResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import service.XslOrderService;
import com.xsl.task.vo.PageObject;
import com.xsl.task.vo.OrderReqVo;

@Controller
public class OrderInfoResourceImpl implements OrderInfoResource {

    @Autowired
    private XslOrderService xslOrderService;

    public PageObject getList(OrderReqVo orderReqVo) {
        try {
            return xslOrderService.selectOrderAll(orderReqVo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
