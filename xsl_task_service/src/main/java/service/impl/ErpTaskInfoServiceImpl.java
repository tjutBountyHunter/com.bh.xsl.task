package service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xsl.task.vo.ErpTaskInfoReqVo;
import com.xsl.task.vo.PageObject;
import com.xsl.task.vo.Task;
import com.xsl.task.vo.TaskInfoReqVo;
import com.xsl.user.UserInfoResouce;
import com.xsl.user.vo.UserVo;
import mapper.XslTaskMapper;
import mapper.XslTaskTagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.ErpTaskInfoService;
import util.DateUtils;
import xsl.pojo.XslTask;
import xsl.pojo.XslTaskExample;
import xsl.pojo.XslTaskTag;
import xsl.pojo.XslTaskTagExample;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/7/14 13:28
 */

@Service
public class ErpTaskInfoServiceImpl implements ErpTaskInfoService {

    Logger logger = LoggerFactory.getLogger(ErpTaskInfoServiceImpl.class);

    @Autowired
    private XslTaskMapper xslTaskMapper;
    @Autowired
    private XslTaskTagMapper xslTaskTagMapper;
    @Resource
    private UserInfoResouce userInfoResouce;

    @Override
    public PageObject selectTaskAll(ErpTaskInfoReqVo erpTaskInfoReqVo) {
        Integer page = erpTaskInfoReqVo.getPage();
        Integer rows = erpTaskInfoReqVo.getRows();
        Integer id = erpTaskInfoReqVo.getId();
        Byte state = erpTaskInfoReqVo.getState();

        PageObject object = new PageObject();
        try{
            XslTaskExample example = new XslTaskExample();
            XslTaskExample.Criteria criteria = example.createCriteria();
            //进行判断防止程序崩溃
            if( id != null ){//任务ID
                criteria.andIdEqualTo(id);
            }
            if(state != null){//任务状态
                criteria.andStateEqualTo(state);
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
                UserVo xslUser = userInfoResouce.getUserInfoMasterId(masterId);
                taskInfoReqVo.setPhone(xslUser.getPhone());
                taskInfoReqVos.add(taskInfoReqVo);
            }

            object.setData(taskInfoReqVos);
            PageInfo<XslTask> info = new PageInfo<XslTask>(list);//得到分页的信息
            //得到分页的总数量
            object.setTotal(info.getTotal());
        }catch (Exception e){
            logger.error("获取任务信息异常警报 :" + e.getMessage());
            throw new RuntimeException(e);
        }finally {
            return object;
        }
    }

    @Override
    public boolean insertXslTask(List<Task> xslTasks) {
        String tag = "任务分页添加";
        //进行添加，查看是否是null
        if( xslTasks == null || xslTasks.size() == 0){
            return false;
        }
        for(Task task : xslTasks){

            try{
                task.setSendid("666666");
                XslTask xslTask = new XslTask();
                BeanUtils.copyProperties(task, xslTask);
                int n = xslTaskMapper.insertSelective(xslTask);
                if( n < 0 ){
                    return false;
                }

            }catch (Exception e){
                logger.error(tag + "异常警报  :" + e.getMessage());
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean updateXslTask(List<Task> xslTasks) {
        String tag = "任务更新";
        if(xslTasks == null || xslTasks.size() == 0){
            return false;
        }

        for(Task task : xslTasks){
            try{
                XslTask xslTask = new XslTask();
                BeanUtils.copyProperties(task, xslTask);
                int n = xslTaskMapper.updateByPrimaryKeySelective(xslTask);
                if(n < 0){
                    logger.error(tag + "是失败!");
                    return false;
                }

            }catch (Exception e){
                logger.error(tag + "异常警报  :" + e.getMessage());
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean deleteXslTask(List<Task> xslTasks) {
        String tag = "任务删除";
        //进行null的判断
        if(xslTasks != null){
            for(Task task : xslTasks){
                try{
                    delXslTask(task);
                }catch (Exception e){
                    logger.error(tag + "异常警报 :" + e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean delXslTask(Task task) {
        if (task != null) {
            //-1代表冻结的意思，进行逻辑删除
            task.setState((byte) (-1));
            XslTask xslTask = new XslTask();
            BeanUtils.copyProperties(task, xslTask);

            int n = xslTaskMapper.updateByPrimaryKeySelective(xslTask);

            if (n < 0) {
                return false;
            }
            //2.进行任务相关的标签删除
            XslTaskTag xslTaskTag = new XslTaskTag();
            xslTaskTag.setState(false);

            //3.进行任务id为此逻辑删除id的任务标签全部删除
            XslTaskTagExample example = new XslTaskTagExample();
            XslTaskTagExample.Criteria criteria = example.createCriteria();
            criteria.andTaskidEqualTo(xslTask.getTaskid());
            xslTaskTagMapper.updateByExampleSelective(xslTaskTag, example);
        }
        return true;
    }
}
