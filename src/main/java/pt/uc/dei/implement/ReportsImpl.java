package pt.uc.dei.implement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import pt.uc.dei.dao.ReportDao;
import pt.uc.dei.helpers.ProjectWithCPISPI;
import pt.uc.dei.helpers.ReportAllocation;
import pt.uc.dei.helpers.ReportIndicator;


@Named("reportImpl")
public class ReportsImpl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(ReportsImpl.class);

	@Inject
	private ReportDao reportDao;

	private List<ReportIndicator> reportsIndicators = new ArrayList<>();
	private List<ProjectWithCPISPI> projectsWithCPISPI=new ArrayList<>();


	public List<ReportIndicator> fillReportIndicator(){
		reportsIndicators=reportDao.fillReports();
		return reportsIndicators;
	}

	public List<ProjectWithCPISPI> calculateCPI(){
		reportsIndicators=fillReportIndicator();
		List<String> codeProject=fillCodeProject();
		float pv=0;
		float ev=0;
		float ac=0;
		float cpi=0;
		float spi=0;
		float sv=0;
		float cv=0;
		int totalTimeSpend=0;
		int totalTimePlan=0;
		int totalTimeLeft=0;
		float percentage=0;
		Date beginDate;
		Date endDate;
		int userGestor;

		for(int i=0;i<codeProject.size();i++){
			
			beginDate=fillBeginDateByProject(codeProject.get(i));
			endDate=fillEndDateByProject(codeProject.get(i));
			userGestor=fillUserGestorByProject(codeProject.get(i));
			totalTimeSpend=fillTotalTimeSpendByProject(codeProject.get(i));
			totalTimeLeft=fillTotalTimeLeftByProject(codeProject.get(i));
			totalTimePlan=fillTotalTimePlanByProject(codeProject.get(i));
			ac=fillTotalAcByProject(codeProject.get(i));
			pv=fillTotalPvByProject(codeProject.get(i));
			if(totalTimePlan==0){
				percentage=0;
			}else{
			percentage=(float) ((totalTimeSpend*100.00)/((totalTimeLeft+totalTimeSpend)*100.00));
			}
			logger.debug("percentage ---> "+percentage);
			ev=percentage*pv;
			cpi=ev/ac;
			logger.debug("CPI ---> "+cpi);
			spi=ev/pv;
			logger.debug("SPI ---> "+spi);
			sv=ev-pv;
			logger.debug("SV ---> "+sv);
			cv=ev-ac;
			logger.debug("CV ---> "+cv);
			ProjectWithCPISPI project = new ProjectWithCPISPI(codeProject.get(i), beginDate, endDate,pv, cpi, spi, totalTimePlan, totalTimeSpend, totalTimeLeft, userGestor, ev, cv, ac, sv);
			projectsWithCPISPI.add(project);
			
		}
		return projectsWithCPISPI;
	}
	
	private int fillUserGestorByProject(String string) {
		int userGestor=0;
		for(int j=0; j<reportsIndicators.size();j++){
			if(reportsIndicators.get(j).getCodigoProject().equals(string)){
				userGestor=reportsIndicators.get(j).getUserGestor();
				return userGestor;
			}
		}
		return userGestor;
	}

	private Date fillEndDateByProject(String string) {
		Date endDate=new Date();
		for(int j=0; j<reportsIndicators.size();j++){
			if(reportsIndicators.get(j).getCodigoProject().equals(string)){
				endDate=reportsIndicators.get(j).getEnddate();
				return endDate;
			}
		}
		return endDate;
	}

	private Date fillBeginDateByProject(String string) {
		Date beginDate=new Date();
		for(int j=0; j<reportsIndicators.size();j++){
			if(reportsIndicators.get(j).getCodigoProject().equals(string)){
				beginDate=reportsIndicators.get(j).getBegindate();
				return beginDate;
			}
		}
		return beginDate;
	}

	private float fillTotalPvByProject(String string) {
		float budget=0;
		for(int j=0; j<reportsIndicators.size();j++){
			if(reportsIndicators.get(j).getCodigoProject().equals(string)){
				budget=reportsIndicators.get(j).getBudget();
				return budget;
			}
		}
		return budget;
	}

	private float fillTotalAcByProject(String string) {
		float ac=0;
		for(int j=0; j<reportsIndicators.size();j++){
			if(reportsIndicators.get(j).getCodigoProject().equals(string)){
				ac=ac+((reportsIndicators.get(j).getTimeSpend()*reportsIndicators.get(j).getCost()));
			}
		}
		return ac;
	}

	private int fillTotalTimePlanByProject(String string) {
		int totalTimePlan=0;
		for(int j=0; j<reportsIndicators.size();j++){
			if(reportsIndicators.get(j).getCodigoProject().equals(string)){
				totalTimePlan=totalTimePlan+reportsIndicators.get(j).getTimePlan();
			}
		}
		return totalTimePlan;
	}

	private int fillTotalTimeLeftByProject(String string) {
		int totalTimeLeft=0;
		for(int j=0; j<reportsIndicators.size();j++){
			if(reportsIndicators.get(j).getCodigoProject().equals(string)){		
				totalTimeLeft=totalTimeLeft+reportsIndicators.get(j).getTimeLeft();
			}
		}
		return totalTimeLeft;	
	}

	private int fillTotalTimeSpendByProject(String string) {
		int totalTimeSpend=0;
		for(int j=0; j<reportsIndicators.size();j++){
			if(reportsIndicators.get(j).getCodigoProject().equals(string)){
				totalTimeSpend=totalTimeSpend+reportsIndicators.get(j).getTimeSpend();
			}
		}
		return totalTimeSpend;			
	}


	public List<String> findProjectinBD(){
		return reportDao.findProject();
	}

	public List<ReportAllocation> fillReportAllocation(String codeProject, Date beginDate, Date endDate){

		return	reportDao.findReportAllocationinDB(codeProject, beginDate, endDate);
	}

	public List<String> fillCodeProject(){
		return reportDao.findProject();
	}

	public List<ReportIndicator> getReportsIndicators() {
		return reportsIndicators;
	}

	public void setReportsIndicators(List<ReportIndicator> reportsIndicators) {
		this.reportsIndicators = reportsIndicators;
	}

	public List<ProjectWithCPISPI> getProjectsWithCPISPI() {
		return projectsWithCPISPI;
	}

	public void setProjectsWithCPISPI(List<ProjectWithCPISPI> projectsWithCPISPI) {
		this.projectsWithCPISPI = projectsWithCPISPI;
	}



}
