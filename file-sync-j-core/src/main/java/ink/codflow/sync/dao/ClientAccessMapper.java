package ink.codflow.sync.dao;

import ink.codflow.sync.do.ClientAccess;

public interface ClientAccessMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientAccess record);

    int insertSelective(ClientAccess record);

    ClientAccess selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientAccess record);

    int updateByPrimaryKey(ClientAccess record);
}