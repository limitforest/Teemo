package com.ciotc.teemo.service;

import java.sql.SQLException;
import java.util.List;

import com.ciotc.teemo.SqlMapClientUtil;
import com.ciotc.teemo.dao.MoviesDAO;
import com.ciotc.teemo.dao.MoviesDAOImpl;
import com.ciotc.teemo.domain.Movies;
import com.ciotc.teemo.domain.MoviesExample;

public class MoviesService {
	private static MoviesService  service = null;
	private MoviesDAO dao = null;
	private MoviesService(){
			dao = new MoviesDAOImpl(SqlMapClientUtil.getSqlMapClient());
	}
	public synchronized static MoviesService getInstance(){
		if(service == null){
			service = new MoviesService();
		}
		return service;
	}
	
	public int countByExample(MoviesExample example) throws SQLException {
        return dao.countByExample(example);
    }

    public int deleteByExample(MoviesExample example) throws SQLException {
        return dao.deleteByExample(example);
    }

    public int deleteByPrimaryKey(String id) throws SQLException {
        return dao.deleteByPrimaryKey(id);
    }

    public void insert(Movies record) throws SQLException {
        dao.insert(record);
    }

    public void insertSelective(Movies record) throws SQLException {
        dao.insertSelective(record);
    }
    public List selectByExample(MoviesExample example) throws SQLException {
       return dao.selectByExample(example);
    }

    public Movies selectByPrimaryKey(String id) throws SQLException {
        return dao.selectByPrimaryKey(id); 
    }

    public int updateByExampleSelective(Movies record, MoviesExample example) throws SQLException {
    	return dao.updateByExampleSelective(record, example);
    }

    public int updateByExample(Movies record, MoviesExample example) throws SQLException {
    	return dao.updateByExample(record, example);
    }

    public int updateByPrimaryKeySelective(Movies record) throws SQLException {
       return dao.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(Movies record) throws SQLException {
       return dao.updateByPrimaryKey(record);

}
	
}
