package resourceImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import resource.UserInfoResouce;
import resource.UserResource;
import vo.UserReqVo;
import vo.XslUserRegister;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
//
@RunWith(SpringJUnit4ClassRunner.class)//固定写法
@ContextConfiguration({"classpath:spring/applicationContext-*.xml"})//spring与mybatis整合的配置文件
public class tes {
    private UserInfoResouce userInfoResource;

    @Test
    public void test() throws IOException {

      Stack a=new Stack<Character>();

//        userInfoResource.getSchoolByName("天津理工大学");
        System.out.println(userInfoResource.getSchoolByName("天津理工大学"));

         }

    }







