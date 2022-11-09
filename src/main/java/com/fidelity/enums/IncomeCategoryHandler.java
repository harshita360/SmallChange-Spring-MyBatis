package com.fidelity.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class IncomeCategoryHandler implements TypeHandler<IncomeCategory>{

	@Override
	public void setParameter(PreparedStatement ps, int i, IncomeCategory parameter, JdbcType jdbcType)
			throws SQLException {
		// TODO Auto-generated method stub
		System.out.println(parameter);
		ps.setString(i, parameter.getCode());
		
	}

	@Override
	public IncomeCategory getResult(ResultSet rs, String columnName) throws SQLException {
		// TODO Auto-generated method stub
		return IncomeCategory.of(rs.getString(columnName));
	}

	@Override
	public IncomeCategory getResult(ResultSet rs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return IncomeCategory.of(rs.getString(columnIndex));
	}

	@Override
	public IncomeCategory getResult(CallableStatement cs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return IncomeCategory.of(cs.getString(columnIndex));
	}

}
