package com.medici.app.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.medici.app.model.Company;
import com.medici.app.producer.Publisher;

/**
 * 
 * @author a73s
 *
 */
@RestController
public class CompanyResource {

	@Autowired
	private Publisher publisher;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Company save(@RequestBody Company message) {
		Company payload = publisher.send(message);
		return payload;
	}

}
