package listener;

import org.springframework.beans.factory.annotation.Autowired;
import service.OrderService;
import util.GsonSingle;
import vo.CreateOrderReqVo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class CreateOrderLisener implements MessageListener {
	@Autowired
	private OrderService orderService;

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {

			String text = textMessage.getText();
			CreateOrderReqVo createOrderReqVo = GsonSingle.getGson().fromJson(text, CreateOrderReqVo.class);
			orderService.createOrder(createOrderReqVo);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
