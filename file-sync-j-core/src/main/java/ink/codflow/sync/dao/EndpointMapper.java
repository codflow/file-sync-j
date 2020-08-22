package ink.codflow.sync.dao;

import ink.codflow.sync.do.Endpoint;

public interface EndpointMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Endpoint record);

    int insertSelective(Endpoint record);

    Endpoint selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Endpoint record);

    int updateByPrimaryKey(Endpoint record);
}