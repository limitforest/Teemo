package com.ciotc.teemo.dao;

import com.ciotc.teemo.domain.Patients;
import com.ciotc.teemo.domain.PatientsExample;
import java.sql.SQLException;
import java.util.List;

public interface PatientsDAO {

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int countByExample(PatientsExample example) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int deleteByExample(PatientsExample example) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int deleteByPrimaryKey(String id) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	void insert(Patients record) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	void insertSelective(Patients record) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	List selectByExample(PatientsExample example) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	Patients selectByPrimaryKey(String id) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int updateByExampleSelective(Patients record, PatientsExample example) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int updateByExample(Patients record, PatientsExample example) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int updateByPrimaryKeySelective(Patients record) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table patients
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int updateByPrimaryKey(Patients record) throws SQLException;
	
}