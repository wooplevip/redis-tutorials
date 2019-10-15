package com.woople.redis.sedis;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class SedisExamples {
    public static void main(String[] args) throws Exception{
        String model = "{\"version\":\"1.0\",\"defaultSchema\":\"SEDIS\",\"schemas\":[{\"name\":\"SEDIS\",\"type\":\"custom\",\"factory\":\"com.woople.calcite.adapter.redis.RedisSchemaFactory\",\"operand\":{\"sedis.redis.cluster.nodes\":\"10.1.236.179:6379,10.1.236.179:6380,10.1.236.179:6381\",\"sedis.redis.table\":{\"tableName\":\"BAZ\",\"fields\":\"ID:VARCHAR,NAME:VARCHAR\",\"keys\":\"ID\"}}}]}";
        Connection connection = DriverManager.getConnection("jdbc:calcite:model=inline:" + model);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from BAZ where ID='2'");
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnSize = metaData.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> result = new LinkedHashMap<>();
            for (int i = 1; i < columnSize + 1; i++) {
                result.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            System.out.println(result);
        }

        statement.close();
        connection.close();
    }
}
