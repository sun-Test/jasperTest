package sun.jasperreport.test.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sun.jasperreport.test.model.Person;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JasperReportService {

    @Autowired
    JRFileVirtualizer jrFileVirtualizer;

    @Autowired
    PersonService personService;

    @Autowired
    private JRSwapFileVirtualizer jrSwapFileVirtualizer;

    @Value("${jasper.out.directory:./}")
    private String outDir;

    @Value("${image_path:src/main/resources/sunf01.png}")
    private String imageFile;

    @Autowired
    JasperReport jasperReport;

    public ResponseEntity<InputStreamResource> generateReportWithSfv() {
        log.info("jasper generation start");

        List<Person> personList = personService.generateData();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(personList);
        File imagePath = new File(imageFile);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(JRParameter.REPORT_VIRTUALIZER, jrSwapFileVirtualizer);
        parameters.put("dataSource", dataSource);
        parameters.put("image_path", imagePath.getAbsolutePath());
        String name = String.format("TReport_%d.pdf", System.currentTimeMillis() / 1000);

        log.info("jasper generation end");
        return generateReport( name, parameters, dataSource);
    }

    private ResponseEntity<InputStreamResource> generateReport(String outFileName, Map<String, Object> parameters, JRBeanCollectionDataSource dataSource) {
        FileInputStream fileInputStream;
        String outPath = String.format("%s/%s", outDir, outFileName);

        try {
/*			DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
			JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.xpath.executer.factory",
					"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesignFile);*/
            //JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File(jasperDesign));
/*			BufferedInputStream bufferedIn = new BufferedInputStream(new FileInputStream(fileIn), 65536); // in bytes
			Document document = JRXmlUtils.parse(bufferedIn);
			parameters.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);*/
            //JRDataSource ds = new JRXmlDataSource(new File("C:\\tmp\\inputs\\T001.XML"));
            JasperPrint p = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            SimpleOutputStreamExporterOutput c = new SimpleOutputStreamExporterOutput(byteArrayOutputStream);
            exporter.setExporterInput(new SimpleExporterInput(p));
            exporter.setExporterOutput(c);
            exporter.exportReport();

            FileOutputStream outputStream = new FileOutputStream(outPath);
            outputStream.write(byteArrayOutputStream.toByteArray());
            outputStream.close();
            jrSwapFileVirtualizer.cleanup();

            fileInputStream = new FileInputStream(outPath);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
            responseHeaders.setContentDispositionFormData("attachment", outFileName);
            responseHeaders.setContentLength(fileInputStream.available());
            return new ResponseEntity<InputStreamResource>(new InputStreamResource(fileInputStream), responseHeaders, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jrFileVirtualizer.cleanup();
        }
        return null;
    }
}
