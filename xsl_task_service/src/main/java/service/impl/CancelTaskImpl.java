package service.impl;

import mapper.XslTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.CancelTask;
import util.ListUtil;
import vo.ResBaseVo;
import xsl.pojo.XslTask;
import xsl.pojo.XslTaskExample;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Service
public class CancelTaskImpl implements CancelTask {
    private static final Logger log =  LoggerFactory.getLogger(CancelTaskImpl.class);
    @Autowired
    private XslTaskMapper xslTaskMapper;


    @Override
    public ResBaseVo cancelTaskDDL() {
        XslTaskExample xslTaskExample = new XslTaskExample();
        List<Byte> values = Arrays.asList(new Byte[]{1, 0});
        xslTaskExample.createCriteria().andStateIn(values).andDeadlineLessThanOrEqualTo(new Date());
        try {
            List<String> taskIds = xslTaskMapper.selectTaskIdByExample(xslTaskExample);

            if(ListUtil.isNotEmpty(taskIds)){
                for(String taskId: taskIds){
                    xslTaskMapper.cancelTaskDDL(taskId);
                }
            }
            return ResBaseVo.ok(taskIds);
        }catch (Exception e){
            log.error("cancelTaskDDL exception:{}", e);
            throw new RuntimeException(e);
        }
    }
}
