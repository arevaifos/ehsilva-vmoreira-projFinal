package pt.uc.dei.backingbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import pt.uc.dei.helpers.CSVGenerator;
import pt.uc.dei.helpers.ReportAllocation;
import pt.uc.dei.implement.ReportsImpl;
import pt.uc.dei.implement.UserImpl;
import java.io.Serializable;

@Named("reportsAllocations")
@SessionScoped
public class ReportsAllocations implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(ReportsAllocations.class);

	@Inject 
	private UserImpl userImpl;

	@Inject 
	private ReportsImpl reportsImpl;

	private List<ReportAllocation> reports= new ArrayList<>();
	private List<String> codeProjects= new ArrayList<>();

	private int idProjectSelect=2;

	private Date beginDate=new Date();
	private Date endDate=new Date();

	private String codeProjectSelect;

	private boolean search=false;
	
	private CSVGenerator csv=new CSVGenerator();


	public ReportsAllocations() {
		super();
	}

	@PostConstruct
	public void init() {
		codeProjects=reportsImpl.fillCodeProject();

	}

	public void refreshReports(){
		if(userImpl.isDirectorUser()||userImpl.isGestorUser()){
			reports= new ArrayList<>();
			reports=reportsImpl.fillReportAllocation(codeProjectSelect, beginDate, endDate);
			
			if(reports.size()>0){
				search=true;
				csv.generateAllocationReport("Testes.csv", reports);
			}
			//			reports=reportsImpl.fillReportAllocation(idProjectSelect, beginDate, endDate);
		}
	}


	public void cleanAllFields(){
		reports= new ArrayList<>();
		idProjectSelect=0;
		beginDate=new Date();
		endDate=new Date();
		search=false;
	}

	public List<ReportAllocation> getReports() {
		return reports;
	}

	public void setReports(List<ReportAllocation> reports) {
		this.reports = reports;
	}

	public int getIdProjectSelect() {
		return idProjectSelect;
	}

	public void setIdProjectSelect(int idProjectSelect) {
		this.idProjectSelect = idProjectSelect;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<String> getCodeProjects() {
		return codeProjects;
	}

	public void setCodeProjects(List<String> codeProjects) {
		this.codeProjects = codeProjects;
	}

	public String getCodeProjectSelect() {
		return codeProjectSelect;
	}

	public void setCodeProjectSelect(String codeProjectSelect) {
		this.codeProjectSelect = codeProjectSelect;
	}

	public boolean isSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}












}
