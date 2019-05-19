package service.impl;

import mapper.XslTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.TaskAccountService;

@Service
public class TaskAccountServiceImpl implements TaskAccountService {
@Autowired
private XslTaskMapper xslTaskMapper;
    @Override
    public int totalTask() {
      try {
          return xslTaskMapper.totalTask();
      }
      catch (Exception e){
          throw  new RuntimeException(e);
      }
    }

    @Override
    public int totalMoney() {

        try {
            return xslTaskMapper.totalMoney();
        }
        catch (Exception e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public int totalDoneTask() {

        try {
            return xslTaskMapper.totalDoneTask();
        }
        catch (Exception e){
            throw  new RuntimeException(e);
        }
    }

}
