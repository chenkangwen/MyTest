package com.example.testservice;

import com.example.testapi.dto.MpUserDTO;
import com.example.testapi.vo.MpUserVO;
import com.example.testservice.boot.TestServiceApplication;
import com.example.testservice.dao.MpUserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestServiceApplication.class)
@EnableAutoConfiguration
public class MyBatisTest {


    public static void main(String[] args) throws IOException {
        // 1. 加载配置文件
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        // 2. 创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            // 3. 获取Mapper接口
            MpUserMapper userMapper = session.getMapper(MpUserMapper.class);

            // 4. 执行查询
            MpUserVO mpUserVO = userMapper.getVOById(1L);
            System.out.println(mpUserVO);

            // 5. 执行插入
            MpUserDTO mpUserDTO = new MpUserDTO();
            mpUserDTO.setCode("张三");
            mpUserDTO.setJsCode("zhangsan@example.com");
            userMapper.insertMpUserDTO(mpUserDTO);

            // 6.提交事务
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}