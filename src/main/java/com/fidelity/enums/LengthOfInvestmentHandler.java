package com.fidelity.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class LengthOfInvestmentHandler implements TypeHandler<LengthOfInvetsment>{

	@Override
	public void setParameter(PreparedStatement ps, int i,  LengthOfInvetsment parameter, JdbcType jdbcType)
			throws SQLException {
		// TODO Auto-generated method stub
		ps.setString(i, parameter.getCode());
		
	}

	@Override
	public LengthOfInvetsment getResult(ResultSet rs, String columnName) throws SQLException {
		// TODO Auto-generated method stub
		return LengthOfInvetsment.of(rs.getString(columnName));
		
	}

	@Override
	public LengthOfInvetsment getResult(ResultSet rs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return LengthOfInvetsment.of(rs.getString(columnIndex));
	}

	@Override
	public LengthOfInvetsment getResult(CallableStatement cs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return LengthOfInvetsment.of(cs.getString(columnIndex));
	}

	

}
