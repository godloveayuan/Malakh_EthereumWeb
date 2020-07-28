package com.ethereum.dao;

import com.ethereum.bean.SecurityRule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Repository
public interface SecurityRuleDao {

    List<SecurityRule> selectAll();

    List<SecurityRule> selectFilter(SecurityRule queryParam);

    List<SecurityRule> selectByUnique(SecurityRule queryParam);

    List<SecurityRule> selectByRuleType(@Param("type") Integer type);

    SecurityRule selectById(@Param("id") Integer id);

    SecurityRule selectByName(@Param("name") String name);

    int insert(SecurityRule securityRule);

    int update(SecurityRule securityRule);

    int delete(int id);
}
