package pt.uc.dei.backingbeans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;


import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.ChartSeries;

import org.apache.log4j.Logger;

import pt.uc.dei.helpers.ProjectWithCPISPI;
import pt.uc.dei.helpers.ReportIndicator;
import pt.uc.dei.implement.ReportsImpl;
import pt.uc.dei.implement.UserImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("reportsIndicators")
@SessionScoped
public class ReportsIndicators implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(ReportsIndicators.class);
	
	private List<ReportIndicator> reports;
	private List<ProjectWithCPISPI> projectsWithCPISPIs;
	
	 private BarChartModel barModel;
	
	@Inject 
	private UserImpl userImpl;

	@Inject 
	private ReportsImpl reportsImpl;
	
	public ReportsIndicators() {
		super();
	}

	@PostConstruct
	public void init() {
		refreshProjectSPICPI();
		createBarModels();
		refreshReports();
		
	}


private BarChartModel initBarModel() {
	projectsWithCPISPIs.clear();
	projectsWithCPISPIs=reportsImpl.calculateCPI();
       BarChartModel model = new BarChartModel();
       ChartSeries cpi = new ChartSeries();
       ChartSeries spi = new ChartSeries();
       for(int i=0; i<projectsWithCPISPIs.size();i++){
    	   logger.debug("SET GRAFICO "+i+" CPI "+ projectsWithCPISPIs.get(i).getProjectCode()+" "+ projectsWithCPISPIs.get(i).getCpi());
    	   logger.debug("SET GRAFICO "+i+" SPI "+ projectsWithCPISPIs.get(i).getProjectCode()+" "+ projectsWithCPISPIs.get(i).getSpi());
    	 cpi.set(projectsWithCPISPIs.get(i).getProjectCode(),projectsWithCPISPIs.get(i).getCpi()); 
    	 spi.set(projectsWithCPISPIs.get(i).getProjectCode(),projectsWithCPISPIs.get(i).getSpi()); 
       }
       cpi.setLabel("CPI");
       spi.setLabel("SPI");


       model.addSeries(cpi);
       model.addSeries(spi);
        
       return model;
   }
    
   private void createBarModels() {
       createBarModel();
   }
    
   private void createBarModel() {
       barModel = initBarModel();
        
//       barModel.setTitle("Bar Chart");
       barModel.setLegendPosition("ne");
       barModel.setSeriesColors("766a62, c4161c");
       barModel.setShadow(true);
  
        
       Axis xAxis = barModel.getAxis(AxisType.X);
       xAxis.setLabel("Project");
        
       Axis yAxis = barModel.getAxis(AxisType.Y);
       yAxis.setLabel("Value");
   }

	

   private void refreshProjectSPICPI() {
	   projectsWithCPISPIs=reportsImpl.calculateCPI();
		
	}
   
	private void refreshReports(){
		reports=new ArrayList<>();
		reports=reportsImpl.fillReportIndicator();
	}
	
	public List<ReportIndicator> getReports() {
		return reports;
	}

	public void setReports(List<ReportIndicator> reports) {
		this.reports = reports;
	}

	public BarChartModel getBarModel() {
		return barModel;
	}

	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}

	public List<ProjectWithCPISPI> getProjectsWithCPISPIs() {
		return projectsWithCPISPIs;
	}

	public void setProjectsWithCPISPIs(List<ProjectWithCPISPI> projectsWithCPISPIs) {
		this.projectsWithCPISPIs = projectsWithCPISPIs;
	}

}
