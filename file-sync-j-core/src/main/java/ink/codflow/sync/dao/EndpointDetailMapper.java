package ink.codflow.sync.dao;

import ink.codflow.sync.do.EndpointDetail;

public interface EndpointDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EndpointDetail record);

    int insertSelective(EndpointDetail record);

    EndpointDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EndpointDetail record);

    int updateByPrimaryKey(EndpointDetail record);
}