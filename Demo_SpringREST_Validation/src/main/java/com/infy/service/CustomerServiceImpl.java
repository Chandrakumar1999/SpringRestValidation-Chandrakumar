package com.infy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.dto.AddressDTO;
import com.infy.dto.CustomerDTO;
import com.infy.entity.Address;
import com.infy.entity.Customer;
import com.infy.exception.InfyBankException;
import com.infy.repository.CustomerRespository;

@Service(value = "customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRespository customerRespository;

	@Override
	public CustomerDTO getCustomer(Integer customerId) throws InfyBankException {
		Optional<Customer> optional = customerRespository.findById(customerId);
		Customer customer = optional.orElseThrow(() -> new InfyBankException("Service.CUSTOMER_NOT_FOUND"));
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCustomerId(customer.getCustomerId());
		customerDTO.setDateOfBirth(customer.getDateOfBirth());
		customerDTO.setEmailId(customer.getEmailId());
		customerDTO.setName(customer.getName());
		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setAddressId(customer.getAddress().getAddressId());
		addressDTO.setStreet(customer.getAddress().getStreet());
		addressDTO.setCity(customer.getAddress().getCity());
		customerDTO.setAddressDTO(addressDTO);
		return customerDTO;
	}

	@Override
	public Integer addCustomer(CustomerDTO customerDTO) throws InfyBankException {
		Customer customer = new Customer();
		customer.setDateOfBirth(customerDTO.getDateOfBirth());
		customer.setEmailId(customerDTO.getEmailId());
		customer.setName(customerDTO.getName());
		customer.setCustomerId(customerDTO.getCustomerId());
		Address address = new Address();
		address.setStreet(customerDTO.getAddressDTO().getStreet());
		address.setCity(customerDTO.getAddressDTO().getCity());
		customer.setAddress(address);
		customerRespository.save(customer);
		return customer.getCustomerId();
	}

	@Override
	public void updateCustomer(Integer customerId, String emailId) throws InfyBankException {
		Optional<Customer> customer = customerRespository.findById(customerId);
		Customer c = customer.orElseThrow(() -> new InfyBankException("Service.CUSTOMER_NOT_FOUND"));
		c.setEmailId(emailId);
	}

	@Override
	public void deleteCustomer(Integer customerId) throws InfyBankException {
		Optional<Customer> customer = customerRespository.findById(customerId);
		customer.orElseThrow(() -> new InfyBankException("Service.CUSTOMER_NOT_FOUND"));
		customerRespository.deleteById(customerId);
	}

	@Override
	public List<CustomerDTO> getAllCustomers() throws InfyBankException {
		Iterable<Customer> customers = customerRespository.findAll();
		List<CustomerDTO> customerDTOs = new ArrayList<>();
		customers.forEach(customer -> {
			CustomerDTO customerDTO = new CustomerDTO();
			customerDTO.setCustomerId(customer.getCustomerId());
			customerDTO.setDateOfBirth(customer.getDateOfBirth());
			customerDTO.setEmailId(customer.getEmailId());
			customerDTO.setName(customer.getName());
			
			AddressDTO addressDTO = new AddressDTO();
			addressDTO.setAddressId(customer.getAddress().getAddressId());
			addressDTO.setStreet(customer.getAddress().getStreet());
			addressDTO.setCity(customer.getAddress().getCity());
			customerDTO.setAddressDTO(addressDTO);
			customerDTOs.add(customerDTO);
		});
		if (customerDTOs.isEmpty())
			throw new InfyBankException("Service.CUSTOMERS_NOT_FOUND");
		return customerDTOs;
	}

}
