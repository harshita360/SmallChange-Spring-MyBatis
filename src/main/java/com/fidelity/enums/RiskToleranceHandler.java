package com.fidelity.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class RiskToleranceHandler implements TypeHandler<RiskTolerance>{

	@Override
	public void setParameter(PreparedStatement ps, int i, RiskTolerance parameter, JdbcType jdbcType)
			throws SQLException {
		// TODO Auto-generated method stub
		ps.setString(i, parameter.getCode());
		
	}

	@Override
	public RiskTolerance getResult(ResultSet rs, String columnName) throws SQLException {
		// TODO Auto-generated method stub
		return RiskTolerance.of(rs.getString(columnName));
		
	}

	@Override
	public RiskTolerance getResult(ResultSet rs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return RiskTolerance.of(rs.getString(columnIndex));
	}

	@Override
	public RiskTolerance getResult(CallableStatement cs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return RiskTolerance.of(cs.getString(columnIndex));
	}

	

}
