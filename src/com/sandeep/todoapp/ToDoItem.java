package com.sandeep.todoapp;

import java.util.Date;

public class ToDoItem {
	private long id;
	private String name;
	private String description;
	private int priority;
	private Date created;
	private Date dueDate;
	private Date remind;
	private int repeat;
	private int completed;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getRemind() {
		return remind;
	}
	
	public void setRemind(Date remind) {
		this.remind = remind;
	}
	public long getRepeat() {
		return repeat;
	}
	
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	
	public int isCompleted() {
		return completed;
	}
	public void setCompleted(int completed) {
		this.completed = completed;
	}
}
