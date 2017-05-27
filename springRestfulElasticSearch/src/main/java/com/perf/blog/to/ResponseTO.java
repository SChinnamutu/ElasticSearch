package com.perf.blog.to;


public class ResponseTO {

	private String status;
	private Long processtime;
	private String responseContent;
	
	public String getResponseContent() {
		return responseContent;
	}
	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}
	public Long getProcesstime() {
		return processtime;
	}
	public void setProcesstime(Long processtime) {
		this.processtime = processtime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
		
}
