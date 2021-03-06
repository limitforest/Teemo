package com.ciotc.teemo.dao;

import com.ciotc.teemo.domain.Patients;
import com.ciotc.teemo.domain.PatientsExample;
import com.ibatis.sqlmap.client.SqlMapClient;
import java.sql.SQLException;
import java.util.List;

public class PatientsDAOImpl implements PatientsDAO {

	/**
	 * This field was generated by Apache iBATIS ibator. This field corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	private SqlMapClient sqlMapClient;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	public PatientsDAOImpl(SqlMapClient sqlMapClient) {
		super();
		this.sqlMapClient = sqlMapClient;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	public int countByExample(PatientsExample example) throws SQLException {
		Integer count = (Integer) sqlMapClient.queryForObject(
				"patients.ibatorgenerated_countByExample", example);
		return count.intValue();
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	public int deleteByExample(PatientsExample example) throws SQLException {
		int rows = sqlMapClient.delete("patients.ibatorgenerated_deleteByExample", example);
		return rows;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	public int deleteByPrimaryKey(String id) throws SQLException {
		Patients key = new Patients();
		key.setId(id);
		int rows = sqlMapClient.delete("patients.ibatorgenerated_deleteByPrimaryKey", key);
		return rows;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	public void insert(Patients record) throws SQLException {
		sqlMapClient.insert("patients.ibatorgenerated_insert", record);
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	public void insertSelective(Patients record) throws SQLException {
		sqlMapClient.insert("patients.ibatorgenerated_insertSelective", record);
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	public List selectByExample(PatientsExample example) throws SQLException {
		List list = sqlMapClient.queryForList("patients.ibatorgenerated_selectByExample", example);
		return list;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	public Patients selectByPrimaryKey(String id) throws SQLException {
		Patients key = new Patients();
		key.setId(id);
		Patients record = (Patients) sqlMapClient.queryForObject(
				"patients.ibatorgenerated_selectByPrimaryKey", key);
		return record;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	public int updateByExampleSelective(Patients record, PatientsExample example)
			throws SQLException {
		UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
		int rows = sqlMapClient.update("patients.ibatorgenerated_updateByExampleSelective", parms);
		return rows;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	public int updateByExample(Patients record, PatientsExample example) throws SQLException {
		UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
		int rows = sqlMapClient.update("patients.ibatorgenerated_updateByExample", parms);
		return rows;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	public int updateByPrimaryKeySelective(Patients record) throws SQLException {
		int rows = sqlMapClient.update("patients.ibatorgenerated_updateByPrimaryKeySelective",
				record);
		return rows;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	public int updateByPrimaryKey(Patients record) throws SQLException {
		int rows = sqlMapClient.update("patients.ibatorgenerated_updateByPrimaryKey", record);
		return rows;
	}

	/**
	 * This class was generated by Apache iBATIS ibator. This class corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	private static class UpdateByExampleParms extends PatientsExample {
	private Object record;
	
	public UpdateByExampleParms(Object record, PatientsExample example) {
		super(example);
		this.record = record;
	}
	
	public Object getRecord() {
		return record;
	}
	}
}