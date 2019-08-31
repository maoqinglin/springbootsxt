package com.ireadygo.springbootsxt.mapper;

import com.ireadygo.springbootsxt.bean.Department;
import org.apache.ibatis.annotations.*;

// 指定这是一个操作数据库的 Mapper
//@Mapper
public interface DepartmentMapper {

    @Select("select * from department where id=#{id}")
    Department getDeptById(Integer id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into department(department_name) values(#{departmentName})")
    int insertDept(Department department);

    @Delete("delete from department where id = #{id}")
    int deleteDeptById(Integer id);

    @Update("update department set departmentName=#{department_name} where id = #{id}")
    int updateDept(Department department);
}
