package sun.jasperreport.test.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import sun.jasperreport.test.service.JasperReportService;

@RestController
public class JasperController {

	protected Logger logger = Logger.getLogger(JasperController.class.getName());

	@Autowired
	JasperReportService jasperReportService;

	@ResponseBody
	@RequestMapping(value = "/pdf/sfv/generate")
	public ResponseEntity<InputStreamResource> getReportSfv() {
		//return new ResponseEntity("generated", HttpStatus.OK);
		return jasperReportService.generateReportWithSfv();
	}


}
