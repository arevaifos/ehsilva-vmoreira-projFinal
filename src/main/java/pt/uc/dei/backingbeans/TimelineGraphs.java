package pt.uc.dei.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

@ManagedBean(name = "timelineGraphs")
@ViewScoped
public class TimelineGraphs implements Serializable {  

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(TimelineGraphs.class);

	@Inject
	private ReportsAllocations reportsAllocations;
	private TimelineModel model;  
	private Date start;  
	private Date end;  
	private Map <String, List<TimelineEvent>> timelineData = new ConcurrentHashMap<String, List<TimelineEvent>>();


	@PostConstruct 
	public void init() {  
		refreshGraphic();
	}  

	public void refreshGraphic(){
		model = new TimelineModel();  
		start=reportsAllocations.getBeginDate();
		end=reportsAllocations.getEndDate();
		for(int i=0; i<reportsAllocations.getReports().size();i++){
			String availability="color";
			List<TimelineEvent> timeLineEvents=new ArrayList<TimelineEvent>();
			TimelineEvent event;
			String key=reportsAllocations.getReports().get(i).getEmailUser();
			if(timelineData.containsKey(key)){
				timeLineEvents=timelineData.get(key);
				event=new TimelineEvent(reportsAllocations.getReports().get(i).getNameActivity(), reportsAllocations.getReports().get(i).getBegindateAllocation(), reportsAllocations.getReports().get(i).getEnddateAllocation(), false, key, availability.toLowerCase());
				timeLineEvents.add(event);
				timelineData.replace(key, timeLineEvents);
				model.add(event);  

			}else{
				event=new TimelineEvent(reportsAllocations.getReports().get(i).getNameActivity(), reportsAllocations.getReports().get(i).getBegindateAllocation(), reportsAllocations.getReports().get(i).getEnddateAllocation(), false, key, availability.toLowerCase());
				timeLineEvents.add(event);
				timelineData.put(key,timeLineEvents);
				model.add(event);  
			}
		}
	}

	public TimelineModel getModel() {  
		return model;  
	}  

	public Date getStart() {  
		return start;  
	}  

	public Date getEnd() {  
		return end;  
	}

	public Map<String, List<TimelineEvent>> getTimelineData() {
		return timelineData;
	}

	public void setTimelineData(Map<String, List<TimelineEvent>> timelineData) {
		this.timelineData = timelineData;
	}



}  
