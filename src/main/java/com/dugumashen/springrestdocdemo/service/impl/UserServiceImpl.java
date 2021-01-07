package com.dugumashen.springrestdocdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.dugumashen.springrestdocdemo.dao.UserDao;
import com.dugumashen.springrestdocdemo.entity.User;
import com.dugumashen.springrestdocdemo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 用户(User)表服务实现类
 *
 * @author makejava
 * @since 2021-01-05 15:34:18
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    private static final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    private UserDao userDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Integer id) {
        return this.userDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<User> queryAllByLimit(int offset, int limit) {
        return this.userDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User insert(User user) {
        this.userDao.insert(user);
        return user;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User update(User user) {
        this.userDao.update(user);
        return this.queryById(user.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.userDao.deleteById(id) > 0;
    }

    @Override
    public void insertBatch(List<User> users) {
        userDao.insertBatch(users);
    }

    @Override
    public Integer countUser() {
        Integer c=userDao.countData();
        return c;
    }

    @Override
    public PageInfo<User> queryAll(User user,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<User> us=userDao.queryAll(user);
        PageInfo<User> pageInfo=new PageInfo<>(us);
        return pageInfo;
    }

    @Override
    public List<Map<String, String>> jdbcQuery(){
        List<Map<String, String>> result = jdbcTemplate.query("select * from test.ts_user",new  ResultSetExtractor<List<Map<String,String>>>(){

            @Override
            public List<Map<String, String>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                List<Map<String,String>> li=new LinkedList<>();
                while(resultSet.next()){
                    String name=resultSet.getString("name");
                    String password=resultSet.getString("password");
                    String status=resultSet.getString("status");
                    Map<String,String> map=new HashMap<>();
                    map.put("name",name);
                    map.put("password",password);
                    map.put("status",status);
                    li.add(map);
                }
                return li;
            }
        });
        return result;
    }

    @Override
    public List<Map<String,String>> selectAll(){
        List<Map<String,String>> users = userDao.selectAll();
        return users;
    }

    @Override
    public List<Map<String,Object>> jdbc2Query(){
       List<Map<String,Object>> result=jdbcTemplate.queryForList("select * from test.ts_user");
        return result;
    }


}