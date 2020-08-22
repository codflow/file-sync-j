package ink.codflow.sync.dao;

import ink.codflow.sync.do.ClientAccessData;

public interface ClientAccessDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientAccessData record);

    int insertSelective(ClientAccessData record);

    ClientAccessData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientAccessData record);

    int updateByPrimaryKey(ClientAccessData record);
}