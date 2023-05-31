package pt.uc.dei.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the State_project database table.
 * 
 */
@Entity
@NamedQueries({ 
	@NamedQuery(name = "State_project.findAll", 	 query= "SELECT s FROM State_project s"),
	@NamedQuery(name = "State_project.findByState", query= "SELECT s FROM State_project s WHERE s.stateproj=:stateproj"),
	@NamedQuery(name = "State_project.findById",   query= "SELECT s FROM State_project s WHERE s.idStateProj=:idStateProj")
})


public class State_project implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idStateProj;

	private String stateproj;

	//bi-directional many-to-one association to Project
	@OneToMany(mappedBy="stateProject")
	private List<Project> projects;

	public State_project() {
	}

	public int getIdStateProj() {
		return this.idStateProj;
	}

	public void setIdStateProj(int idStateProj) {
		this.idStateProj = idStateProj;
	}

	public String getStateproj() {
		return this.stateproj;
	}

	public void setStateproj(String stateproj) {
		this.stateproj = stateproj;
	}

	public List<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Project addProject(Project project) {
		getProjects().add(project);
		project.setStateProject(this);

		return project;
	}

	public Project removeProject(Project project) {
		getProjects().remove(project);
		project.setStateProject(null);

		return project;
	}

}