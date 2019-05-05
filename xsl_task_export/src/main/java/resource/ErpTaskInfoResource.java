package resource;

import vo.ErpTaskInfoReqVo;
import vo.PageObject;

public interface ErpTaskInfoResource {
	/**
	 * 页面数据的查询
	 * @return 返回一个数据的列表包含data和total
	 */
	PageObject SelectTaskAll(ErpTaskInfoReqVo erpTaskInfoReqVo);




}
