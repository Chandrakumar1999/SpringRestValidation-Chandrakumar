package com.chandra.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chandra.dto.CustomerDTO;
import com.chandra.exception.ChandraBankException;
import com.chandra.service.CustomerService;

@RestController
@RequestMapping
@Validated
public class CustomerAPI {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private Environment environment;

	@GetMapping(value="/customers")
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() throws ChandraBankException {
	List<CustomerDTO> customerDTO =	customerService.getAllCustomer();
	return new ResponseEntity<>(customerDTO, HttpStatus.OK);
	}
	
	@PostMapping(value="/customers")
	public ResponseEntity<String> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws ChandraBankException{
		Integer customerId=customerService.addCustomer(customerDTO);
		String message= environment.getProperty("API.INSERT_SUCCESS") + customerId;
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}
	@GetMapping(value="/customers/{customerId}")
	public ResponseEntity<CustomerDTO> getCustomerDetails(@PathVariable
		@Min(value=1, message="{customer.customerid.invalid}")
		@Max(value=100,message="{customer.customerid.invalid}")
		Integer customerId) throws ChandraBankException
	{
	 CustomerDTO customerDTO=customerService.getCustomer(customerId);
	 return new ResponseEntity<>(customerDTO, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/customers/{customerId}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Integer customerId) throws ChandraBankException{
		customerService.deleteCustomer(customerId);
		String message= environment.getProperty("API.DELETE_SUCCESS");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	@PutMapping(value="/customers/{customerId}")
	public ResponseEntity<String> updateCustomer(@PathVariable Integer customerId,
			@RequestBody CustomerDTO customerDTO) throws ChandraBankException{
		customerService.updateCustomer(customerId,customerDTO.getEmailId());
		String message= environment.getProperty("API.UPDATE_SUCCESS");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

}
