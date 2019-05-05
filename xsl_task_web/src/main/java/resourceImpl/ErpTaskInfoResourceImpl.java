package resourceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mapper.XslMasterMapper;
import mapper.XslTaskCategoryMapper;
import mapper.XslTaskMapper;
import mapper.XslTaskTagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import resource.ErpTaskInfoResource;
import resource.UserInfoResouce;
import util.DateUtils;
import vo.*;
import xsl.pojo.XslTask;
import xsl.pojo.XslTaskExample;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ErpTaskInfoResourceImpl implements ErpTaskInfoResource {
	/* 任务服务 */
	Logger logger = LoggerFactory.getLogger(ErpTaskInfoResourceImpl.class);

	@Resource
	private XslTaskMapper xslTaskMapper;
	@Resource
	private XslTaskCategoryMapper xslTaskCategoryMapper;
	@Resource
	private XslTaskTagMapper xslTaskTagMapper;
	@Resource
	private XslMasterMapper xslMasterMapper;
	@Resource
	private UserInfoResouce userInfoResouce;


	@Override
	public PageObject SelectTaskAll(ErpTaskInfoReqVo erpTaskInfoReqVo) {
		 Integer page = erpTaskInfoReqVo.getPage();
		 Integer rows = erpTaskInfoReqVo.getRows();
		 Integer key = erpTaskInfoReqVo.getKey();
		 Byte key1 = erpTaskInfoReqVo.getKey1();

		PageObject object = new PageObject();
		try{
			XslTaskExample example = new XslTaskExample();
			XslTaskExample.Criteria criteria = example.createCriteria();
			//进行判断防止程序崩溃
			if( key != null ){//任务ID
				criteria.andIdEqualTo(key);
			}
			if( key1 != null){//任务状态
				criteria.andStateEqualTo(key1);
			}
			PageHelper.startPage(page,rows);//进行分页
			List<XslTask> list = xslTaskMapper.selectByExample(example);

			List<TaskInfoReqVo> taskInfoReqVos = new ArrayList<>();
			for (XslTask xslTask : list){
				TaskInfoReqVo taskInfoReqVo = new TaskInfoReqVo();
				BeanUtils.copyProperties(xslTask, taskInfoReqVo);
				taskInfoReqVo.setCreatedate(DateUtils.getDateTimeToString(xslTask.getCreatedate()));
				taskInfoReqVo.setUpdatedate(DateUtils.getDateTimeToString(xslTask.getUpdatedate()));
				taskInfoReqVo.setDeadline(DateUtils.getDateTimeToString(xslTask.getDeadline()));

				String masterId = xslTask.getSendid();
				XslUser xslUser = userInfoResouce.getUserInfoMasterId(masterId);
				taskInfoReqVo.setPhone(xslUser.getPhone());
				taskInfoReqVos.add(taskInfoReqVo);
			}

			object.setData(taskInfoReqVos);
			PageInfo<XslTask> info = new PageInfo<XslTask>(list);//得到分页的信息
			//得到分页的总数量
			object.setTotal(info.getTotal());
		}catch (Exception e){
			logger.error("获取任务信息异常警报 :" + e.getMessage());
		}finally {
			return object;
		}
	}
}
