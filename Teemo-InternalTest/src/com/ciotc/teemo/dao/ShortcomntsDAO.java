package com.ciotc.teemo.dao;

import com.ciotc.teemo.domain.Shortcomnts;
import com.ciotc.teemo.domain.ShortcomntsExample;
import java.sql.SQLException;
import java.util.List;

public interface ShortcomntsDAO {

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table shortcomnts
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int countByExample(ShortcomntsExample example) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table shortcomnts
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int deleteByExample(ShortcomntsExample example) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table shortcomnts
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int deleteByPrimaryKey(String id) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table shortcomnts
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	void insert(Shortcomnts record) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table shortcomnts
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	void insertSelective(Shortcomnts record) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table shortcomnts
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	List selectByExample(ShortcomntsExample example) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table shortcomnts
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	Shortcomnts selectByPrimaryKey(String id) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table shortcomnts
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int updateByExampleSelective(Shortcomnts record, ShortcomntsExample example)
			throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table shortcomnts
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int updateByExample(Shortcomnts record, ShortcomntsExample example) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table shortcomnts
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int updateByPrimaryKeySelective(Shortcomnts record) throws SQLException;

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table shortcomnts
	 * @ibatorgenerated  Mon Apr 16 15:06:52 CST 2012
	 */
	int updateByPrimaryKey(Shortcomnts record) throws SQLException;
}