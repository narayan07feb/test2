package com.optum.ogn.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class WCPMemberProductEligibilityResponse {
	
	private WCPExceptionDetail exceptionDetail;
	private WCPResponseHeader responseHeader;
	private List<WCPConsumerDetailsResponse> consumerDetailsList;
	
	
	@ApiModelProperty(value = "")
	@JsonProperty("responseHeader")
	public WCPResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(WCPResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	
	
	@ApiModelProperty(value = "")
	@JsonProperty("consumerDetails")
	public List<WCPConsumerDetailsResponse> getConsumerDetailsList() {
		return consumerDetailsList;
	}
	public void setConsumerDetailsList(List<WCPConsumerDetailsResponse> consumerDetailsList) {
		this.consumerDetailsList = consumerDetailsList;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("exceptionDetail")
	public WCPExceptionDetail getExceptionDetail() {
		return exceptionDetail;
	}
	public void setExceptionDetail(WCPExceptionDetail exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}
	
	
	
	
	
	
	
	
	
}
