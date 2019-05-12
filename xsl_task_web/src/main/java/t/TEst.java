package  t;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import resource.UserInfoResouce;
import resource.UserResource;
import vo.UserReqVo;

import java.util.Stack;


public class TEst {
    @Autowired
    private UserInfoResouce userInfoResouce;

@Test
    public void test(){
    Stack a=new Stack<Character>();
    Object a;
    a.
//    System.out.println(userInfoResouce.getSchoolByName("天津理工大学").toString());
    userInfoResouce.getSchoolByName("天津理工大学");
}
}