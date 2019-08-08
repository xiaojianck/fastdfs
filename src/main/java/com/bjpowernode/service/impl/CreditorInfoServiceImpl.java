package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.CreditorInfoMapper;
import com.bjpowernode.model.CreditorInfo;
import com.bjpowernode.model.CreditorInfoExample;
import com.bjpowernode.service.CreditorInfoService;
import com.bjpowernode.utils.FastDFS1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Author:箫剑
 * 2019/8/7
 */
@Service
public class CreditorInfoServiceImpl implements CreditorInfoService {
    @Autowired
    private CreditorInfoMapper creditorInfoMapper;
    @Override
    public List<CreditorInfo> getAllCreditorInfo() {
        CreditorInfoExample example = new CreditorInfoExample();
        return creditorInfoMapper.selectByExample(example);
    }

    @Override
    public int updateCreditorInfo(CreditorInfo creditorInfo) {
        return creditorInfoMapper.updateByPrimaryKeySelective(creditorInfo);
    }

    @Override
    public CreditorInfo getCreditorInfoById(Integer id) {
        return creditorInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional //加上该注解控制事务
    public int deleteContract(Integer id) {
        // 1 删除失败；0 删除成功
        int result = 1;
        //根据债权id获取债权信息
        CreditorInfo creditorInfo = creditorInfoMapper.selectByPrimaryKey(id);
        /**
         * 注意：事务控制的数据库，所以我们先对数据库进行更新，在操作FastDFS
         * 如果操作FastDFS失败了，那么对数据库的操作回滚
         */
        //更新数据库债权表的合同路径及组
        int updateRow = creditorInfoMapper.updateConstractById(id);
        if(updateRow > 0){
            //如果数据库更新成功，那么删除FastDFS上的文件
            int num = FastDFS1.fileDelete(creditorInfo.getGroupname(),creditorInfo.getRemotefilepath());
            if(num == 0){
                //如果删除成功，那么将整个操作结果设置为0，表示成功
                result = 0;
            }else{
                //如果删除FastDFS上的文件失败，我们抛出一个运行异常，回滚事务
                throw new RuntimeException("FastDFS文件删除失败");
            }
        }

        return result;
    }

}
