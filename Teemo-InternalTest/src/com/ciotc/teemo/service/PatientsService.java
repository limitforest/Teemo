package com.ciotc.teemo.service;

import java.sql.SQLException;
import java.util.List;
import com.ciotc.teemo.SqlMapClientUtil;
import com.ciotc.teemo.dao.PatientsDAO;
import com.ciotc.teemo.dao.PatientsDAOImpl;
import com.ciotc.teemo.domain.Patients;
import com.ciotc.teemo.domain.PatientsExample;


public class PatientsService {

	private static PatientsService service = null;

	private PatientsDAO dao = null;

	public synchronized static PatientsService getInstance() {
		if (service == null)
			service = new PatientsService();
		return service;
	}

	private PatientsService() {
		dao = new PatientsDAOImpl(SqlMapClientUtil.getSqlMapClient());
	}
	
	public int countByExample(PatientsExample example) throws SQLException {
        return dao.countByExample(example);
    }

    public int deleteByPrimaryKey(String id) throws SQLException {
        return dao.deleteByPrimaryKey(id);
    }

    public void insert(Patients record) throws SQLException {
       dao.insert(record);
    }

    public void insertSelective(Patients record) throws SQLException {
       dao.insertSelective(record);
    }

    public List selectByExample(PatientsExample example) throws SQLException {
       return dao.selectByExample(example);
    }

    public Patients selectByPrimaryKey(String id) throws SQLException {
      return dao.selectByPrimaryKey(id);
    }

    public int updateByExampleSelective(Patients record, PatientsExample example) throws SQLException {
       return dao.updateByExampleSelective(record, example);
    }

    public int updateByExample(Patients record, PatientsExample example) throws SQLException {
      return dao.updateByExample(record, example);
    }
    
    public int updateByPrimaryKeySelective(Patients record) throws SQLException {
       return dao.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(Patients record) throws SQLException {
        return dao.updateByPrimaryKey(record);
    }

}
