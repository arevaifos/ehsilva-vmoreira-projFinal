package pt.uc.dei.dao;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import pt.uc.dei.helpers.ProjectReportOld;
import pt.uc.dei.helpers.ReportAllocation;
import pt.uc.dei.helpers.ReportIndicator;
import pt.uc.dei.implement.UserImpl;

@Stateless
public class ReportDao {

	@Inject
	private EntityManager em;

	@Inject
	private UserImpl userImpl;

	final static Logger logger = Logger.getLogger(ReportDao.class);

	public List<ReportIndicator> fillReports(){	
		Query q ;
		List<Object[]> results= new ArrayList<>();
		if(userImpl.isDirectorUser()){
			q = em.createNativeQuery(
					"SELECT idProject, userGestor, codigoProject, begindate, enddate, budget, idAllocation, email, begindateAllocation, "
							+"enddateAllocation,  timeLeft, timePlan, timeSpend, cost, percentage FROM View_Reports ");
			results = q.getResultList();
		} else{
			q = em.createNativeQuery(
					"SELECT idProject, userGestor, codigoProject, begindate, enddate, budget, idAllocation, email, begindateAllocation, "
							+"enddateAllocation,  timeLeft, timePlan, timeSpend, cost, percentage FROM View_Reports where userGestor=? ");
			q = q.setParameter(1, userImpl.getLoggedUser().getIduser());
			results = q.getResultList();
		}

		List<ReportIndicator> reportsIndicatorQuery = new ArrayList<>();

		for (Object[] result : results) {

			if (result[0] != null && String.valueOf(result[0]).trim().length() > 0) {

				int idProject = Integer.parseInt(String.valueOf(result[0]));

				int userGestor = Integer.parseInt(String.valueOf(result[1]));

				String codigoProject = String.valueOf(result[2]);

				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String stringdate = String.valueOf(result[3]);
				Date beginDate=new Date();
				try {
					beginDate = dt.parse(stringdate);
				} catch (ParseException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}

				stringdate = String.valueOf(result[4]);
				Date endDate=new Date();
				try {
					endDate = dt.parse(stringdate);
				} catch (ParseException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}

				float budget =Float.parseFloat(String.valueOf(result[5]));

				int idAllocation = Integer.parseInt(String.valueOf(result[6]));

				String email = String.valueOf(result[7]);

				stringdate = String.valueOf(result[8]);
				Date beginDateAllocation=new Date();
				try {
					beginDateAllocation = dt.parse(stringdate);
				} catch (ParseException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}

				stringdate = String.valueOf(result[9]);
				Date endDateAllocation=new Date();
				try {
					endDateAllocation = dt.parse(stringdate);
				} catch (ParseException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}

				int timeLeft = Integer.parseInt(String.valueOf(result[10]));

				int timePlan = Integer.parseInt(String.valueOf(result[11]));

				int timeSpend = Integer.parseInt(String.valueOf(result[12]));

				float cost = Float.valueOf(String.valueOf(result[13]));

				float percentage=Float.valueOf(String.valueOf(result[14]));

				reportsIndicatorQuery.add(new ReportIndicator( beginDate, beginDateAllocation, budget,
						codigoProject, cost, email, endDate, endDateAllocation, idAllocation,
						idProject, percentage, timeLeft, timePlan, timeSpend, userGestor));

			}		

		}
		return reportsIndicatorQuery;
	}

	public List<String> findProject(){
		Query q ;
		List<Object[]> results= new ArrayList<>();
		if(userImpl.isDirectorUser()){
			q = em.createNativeQuery(
					"SELECT DISTINCT codigoProject FROM View_Reports ");
			results = q.getResultList();
		} else{
			q = em.createNativeQuery(
					"SELECT DISTINCT codigoProject FROM View_Reports WHERE userGestor=? ");
			q = q.setParameter(1, userImpl.getLoggedUser().getIduser());
			results = q.getResultList();
		}
		List<String> codeProjectQuery = new ArrayList<>();

		for(int i=0; i<results.size();i++){
			if(results.get(i) != null && String.valueOf(results.get(i)).trim().length() > 0) {
				codeProjectQuery.add(String.valueOf(results.get(i)));
			}

		}

		return codeProjectQuery;
	}




	public List<ReportAllocation> findReportAllocationinDB(String codeProject, Date beginDate, Date endDate) {
		Query q = em.createNativeQuery(
				"SELECT idAllocation, begindateAllocation, enddateAllocation, percentage, email, codigoProject, idActivity, nameActivity "
						+ "FROM View_Reports WHERE codigoProject=? AND beginDateact>= ? AND endDateact<= ? ");

		q = q.setParameter(1, codeProject);
		q = q.setParameter(2, beginDate);
		q = q.setParameter(3, endDate);

		List<Object[]> results = q.getResultList();
		List<ReportAllocation> reportsAllocationsQuery = new ArrayList<>();

		for (Object[] result : results) {
			System.out.println("Author " + result[0] + " " + result[1]);

			if (result[0] != null && String.valueOf(result[0]).trim().length() > 0) {

				int idAllocation = Integer.parseInt(String.valueOf(result[0]));

				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String stringdate = String.valueOf(result[1]);
				Date beginDateAllocation=new Date();
				try {
					beginDateAllocation = dt.parse(stringdate);
				} catch (ParseException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}

				stringdate = String.valueOf(result[2]);
				Date endDateAllocation=new Date();
				try {
					endDateAllocation = dt.parse(stringdate);
					System.out.println("parse date: "+ String.valueOf(endDateAllocation));
				} catch (ParseException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}

				BigDecimal percentageBD = BigDecimal.ZERO;

				if (result[3] != null && String.valueOf(result[3]).trim().length() > 0) {

					percentageBD = new BigDecimal(String.valueOf(result[3]));
				}
				float percentage=percentageBD.byteValue();


				String email = String.valueOf(result[4]);


				String codeProject2 = String.valueOf(result[5]);


				int idActivity = Integer.parseInt(String.valueOf(result[6]));


				String nameActivity = String.valueOf(result[7]);


				reportsAllocationsQuery.add(new ReportAllocation(idAllocation, beginDateAllocation, endDateAllocation, percentage, codeProject2, email, idActivity, nameActivity));

			}		

		}
		return reportsAllocationsQuery;
	}

}
