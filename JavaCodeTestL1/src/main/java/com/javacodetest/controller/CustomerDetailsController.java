package com.javacodetest.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javacodetest.service.CustomerDetailsService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;




@RestController
public class CustomerDetailsController {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerDetailsController.class);
	
	
	@Autowired
	CustomerDetailsService customerDetailsService;
	

	@ApiOperation(value="Get Customer Details",code=200, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = 
		{@ApiResponse(code=200,message ="ok"),
	@ApiResponse(code=400,message ="BAD REQUEST"),
	@ApiResponse(code=404,message ="Not Found"),
	@ApiResponse(code=500,message ="Internal Server Error")
	})
	@GetMapping("/api/v1")
	public ResponseEntity<Object> getCustomerDetails( @ApiParam(name="customer_id")  @RequestParam(name="customer_id") Integer customerId) {
		logger.info("CustomerDetailsController -- inside getCustomerDetails method ");
		Map<String,Object>  responseMap = null;
		try {
			responseMap = customerDetailsService.getCustomerDetails(customerId);
		}catch(Exception e) {
			logger.error("CustomerDetailsController -- Exception occured due to "+e.getMessage());
		}
		return new ResponseEntity<Object>(responseMap, HttpStatus.OK);
	}
}
