package com.dugumashen.springrestdocdemo.service;

import com.dugumashen.springrestdocdemo.entity.User;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 用户(User)表服务接口
 *
 * @author makejava
 * @since 2021-01-05 15:34:18
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<User> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User update(User user);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    void insertBatch(List<User> user);

    Integer countUser();

    /**
     * 使用user作为参数包装查询User
     *
     * @param user 用户参数
     * @return User集合
     */
    PageInfo<User> queryAll(User user, int pageNum, int pageSize);

    List<Map<String, String>> jdbcQuery();
    List<Map<String,Object>> jdbc2Query();

    List<Map<String, String>> selectAll();

}