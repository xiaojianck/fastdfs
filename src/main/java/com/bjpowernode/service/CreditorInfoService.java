package com.bjpowernode.service;

import com.bjpowernode.model.CreditorInfo;

import java.util.List;

/**
 * Author:箫剑
 * 2019/8/7
 */

public interface CreditorInfoService {

    List<CreditorInfo> getAllCreditorInfo();

    int updateCreditorInfo(CreditorInfo creditorInfo);

    CreditorInfo getCreditorInfoById(Integer id);

    int deleteContract(Integer id);
}
