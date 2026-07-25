package com.fanhoufang.solver.common.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.fanhoufang.solver.common.enums.ServerStatus;

@MappedTypes(ServerStatus.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class ServerStatusTypeHandler extends BaseTypeHandler<ServerStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ServerStatus parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, java.sql.Types.OTHER);
        } else {
            ps.setObject(i, parameter.name(), java.sql.Types.OTHER);
        }
    }

    @Override
    public ServerStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : ServerStatus.valueOf(value);
    }

    @Override
    public ServerStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : ServerStatus.valueOf(value);
    }

    @Override
    public ServerStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : ServerStatus.valueOf(value);
    }
}
