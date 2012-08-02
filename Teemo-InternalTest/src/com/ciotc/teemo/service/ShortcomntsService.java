package com.ciotc.teemo.service;

import java.sql.SQLException;
import java.util.List;

import com.ciotc.teemo.SqlMapClientUtil;
import com.ciotc.teemo.dao.ShortcomntsDAO;
import com.ciotc.teemo.dao.ShortcomntsDAOImpl;
import com.ciotc.teemo.domain.Shortcomnts;
import com.ciotc.teemo.domain.ShortcomntsExample;

public class ShortcomntsService {
	private static ShortcomntsService service = null;
	private ShortcomntsDAO dao = null;
	
	
	public synchronized static ShortcomntsService getInstance(){
		if(service == null){
			service = new ShortcomntsService();
		}
			return service;
		}
	
	private ShortcomntsService(){
		dao = new ShortcomntsDAOImpl(SqlMapClientUtil.getSqlMapClient());
	}

	
	  public int countByExample(ShortcomntsExample example) throws SQLException {
	        return dao.countByExample(example);
	    }

	    public int deleteByExample(ShortcomntsExample example) throws SQLException {
	        return dao.deleteByExample(example);
	    }

	    public int deleteByPrimaryKey(String id) throws SQLException {
	        return dao.deleteByPrimaryKey(id);
	    }

	    public void insert(Shortcomnts record) throws SQLException {
	        dao.insert(record);
	    }

	    public void insertSelective(Shortcomnts record) throws SQLException {
	        dao.insertSelective(record);
	    }

	    public List selectByExample(ShortcomntsExample example) throws SQLException {
	        return dao.selectByExample(example);
	    }

	    public Shortcomnts selectByPrimaryKey(String id) throws SQLException {
	        return dao.selectByPrimaryKey(id);
	    }

	    public int updateByExampleSelective(Shortcomnts record, ShortcomntsExample example) throws SQLException {
	        return dao.updateByExampleSelective(record, example);
	    }

	    public int updateByExample(Shortcomnts record, ShortcomntsExample example) throws SQLException {
	        return dao.updateByExample(record, example);
	    }

	    public int updateByPrimaryKeySelective(Shortcomnts record) throws SQLException {
	        return dao.updateByPrimaryKeySelective(record);
	    }

	    public int updateByPrimaryKey(Shortcomnts record) throws SQLException {
	        return dao.updateByPrimaryKey(record);
	    }

	
	

}
