package com.bjpowernode.mapper;

import com.bjpowernode.model.CreditorInfo;
import com.bjpowernode.model.CreditorInfoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface CreditorInfoMapper {
    long countByExample(CreditorInfoExample example);

    int deleteByExample(CreditorInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CreditorInfo record);

    int insertSelective(CreditorInfo record);

    List<CreditorInfo> selectByExample(CreditorInfoExample example);

    CreditorInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CreditorInfo record, @Param("example") CreditorInfoExample example);

    int updateByExample(@Param("record") CreditorInfo record, @Param("example") CreditorInfoExample example);

    int updateByPrimaryKeySelective(CreditorInfo record);

    int updateByPrimaryKey(CreditorInfo record);

    int updateConstractById(Integer id);
}