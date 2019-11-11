package com.viva.CustomerProcessing.entity;

import java.sql.Date;
import java.util.Objects;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;


public class Customer{
	
	private String firstName;
	private String middleName;
	private String lastName;
    private String address;
    private String city;
    private String phoneNumber;
    
    
	
	public Customer() {
		
	}

	public Customer(String firstName, String middleName, String lastName, String phoneNumber, String address,
			String city) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.city = city;		
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
//	@Override
//    public int compareTo(Customer o) {
//        return this.getPhoneNumber().compareTo(o.getPhoneNumber());
//    }
		
	@Override
	public String toString() {
		return "Customer [firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName
				+ ", phoneNumber=" + phoneNumber + ", address=" + address + ", city=" + city
				+"]";
	}
	@Override
	public int hashCode(){
	    return Objects.hash(phoneNumber);
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(this == obj) return true;
		else if(obj instanceof Customer){ // implicit null check
	        Customer other = (Customer) obj;
	        return Objects.equals(this.phoneNumber, other.phoneNumber);
	    }
		else {
			return false;
		}
	}
	
}
