package service.impl;

import com.xsl.task.vo.HunterInfo;
import com.xsl.task.vo.TaskInfo;
import com.xsl.user.UserInfoResouce;
import com.xsl.user.vo.HunterVo;
import com.xsl.user.vo.MasterVo;
import com.xsl.user.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import service.CommonUseService;
import service.TagService;
import xsl.pojo.XslTask;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/7/14 10:47
 */
@Service
public class CommonUseServiceImpl implements CommonUseService {

    @Resource
    private UserInfoResouce userInfoResouce;

    @Autowired
    private TagService tagService;

    @Override
    public HunterInfo getHunterInfo(String hunterId) {
        //获取猎人信息
        HunterVo hunter = userInfoResouce.getHunterInfo(hunterId);
        HunterInfo hunterInfo = new HunterInfo();
        BeanUtils.copyProperties(hunter, hunterInfo);
        UserVo user = userInfoResouce.getUserInfoByHunterId(hunterId);
        hunterInfo.setPhone(user.getPhone());
        hunterInfo.setName(user.getName());
        hunterInfo.setTxUrl("http://47.93.200.190/images/default.png");

        String userTx = userInfoResouce.getUserTx(hunter.getUserid());
        if(!StringUtils.isEmpty(userTx)){
            hunterInfo.setTxUrl(userTx);
        }

        return hunterInfo;
    }

    @Override
    public TaskInfo initTaskInfo(XslTask xslTask) {
        TaskInfo taskInfoVo = new TaskInfo();
        String masterId = xslTask.getSendid();
        MasterVo masterInfo = userInfoResouce.getMasterInfo(masterId);
        UserVo userInfo = userInfoResouce.getUserInfoMasterId(masterId);

        //获取任务标签
        String taskid = xslTask.getTaskid();
        List taskTags = tagService.getTaskTags(taskid);

        BeanUtils.copyProperties(xslTask, taskInfoVo);
        BeanUtils.copyProperties(masterInfo, taskInfoVo);
        taskInfoVo.setMasterlevel(masterInfo.getLevel());
        BeanUtils.copyProperties(userInfo, taskInfoVo);
        taskInfoVo.setTaskId(xslTask.getTaskid());
        taskInfoVo.setTaskTitle(xslTask.getTasktitle());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        taskInfoVo.setCreateDate(sdf.format(xslTask.getCreatedate()));
        taskInfoVo.setTxUrl("http://47.93.200.190/images/default.png");
        String userTx = userInfoResouce.getUserTx(masterInfo.getUserid());
        if(!StringUtils.isEmpty(userTx)){
            taskInfoVo.setTxUrl(userTx);
        }
        taskInfoVo.setMasterlevel(masterInfo.getLevel());
        taskInfoVo.setMasterId(xslTask.getSendid());
        taskInfoVo.setDeadLineDate(sdf.format(xslTask.getDeadline()));
        taskInfoVo.setUpdatedate(sdf.format(xslTask.getUpdatedate()));
        taskInfoVo.setTags(taskTags);
        return taskInfoVo;
    }
}
