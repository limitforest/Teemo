package com.ciotc.teemo.dao;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

import com.ciotc.teemo.SqlMapClientUtil;
import com.ciotc.teemo.domain.Movies;
import com.ciotc.teemo.domain.Patients;
import com.ciotc.teemo.domain.PatientsExample;
import com.ciotc.teemo.service.PatientsService;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		/*
		 * MoviesDAO dao = new MoviesDAOImpl( SqlMapClientUtil.getSqlMapClient(
		 * ) ); Movies m = new Movies( ); for(int i =2 ; i<15 ; i++){
		 * m.setPatientid( ""+i+"" ); m.setDescription("第"+i+"个测试用户");
		 * m.setLinkphoto("photoUrl_+"+i); m.setCreatetime(
		 * DateFormatUtils.format( new Date( ), "yyyy-MM-dd HH:mm:ss" ) );
		 * dao.insertSelective( m ); }
		 */
//		// dao.insertSelective( m );
//		Patients pnt = new Patients();
//		PatientsService pntService = PatientsService.getInstance();
//		PatientsExample pntExample = new PatientsExample();
////		System.out.println(pntService.selectByPrimaryKey("15").getName());
		
		
//		PatientsExample.Criteria pntCriteria = pntExample.createCriteria();
//		pntCriteria.andIdIsNotNull();
		// pntExample.setOrderByClause(orderByClause);
//		 pntCriteria.andNameEqualTo( "test1");

/*		List<Patients> list = (List<Patients>) pntService
				.selectByExample(pntExample);
		*/
//		 for (int i=0;i<list.size();i++){ }
		 
		/*for (Patients p : list) {
			System.out.println("姓名:" + p.getName() + "\t 年龄：" + p.getAge());
		}*/
		
	/*	String [ ] pntArray = new String []{};
		for(Patients pnt : list){
			pnt.getName();
		}
		
		for(int i = 0;i<list.size();i++){
			pntArray[i] = list.get(i).getName();
			System.out.println(pntArray[i]);
		}
		*/
	}

}
