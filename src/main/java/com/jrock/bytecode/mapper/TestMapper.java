package com.jrock.bytecode.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {

    void insertData(String tarSb);

    void insertData2(String data);

    String selectData();
}
