package ink.codflow.sync.dao;

import ink.codflow.sync.do.FkClientAccount;

public interface FkClientAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FkClientAccount record);

    int insertSelective(FkClientAccount record);

    FkClientAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FkClientAccount record);

    int updateByPrimaryKey(FkClientAccount record);
}