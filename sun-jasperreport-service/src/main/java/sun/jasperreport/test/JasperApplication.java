package sun.jasperreport.test;

import java.io.File;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRSwapFile;

@SpringBootApplication
public class JasperApplication {

	@Value("${jasper.swapfile.directory:./}")
	private String jasperSwapFileDirectory;
	@Value("${jasper.swapfile.maxsize:500}")
	private Integer jasperSwapFileMaxSize;
	@Value("${jasper.swapfile.blocksize:2048}")
	private Integer jasperSwapFileBlockSize;
	@Value("${jasper.swapfile.mingrow:100}")
	private Integer jasperSwapFileMinGrowCount;

	public static void main(String[] args) {
		SpringApplication.run(JasperApplication.class, args);
	}

	@Bean
	JasperReport report() throws JRException {
		JasperReport jasperReport = null;
		File compiledFile = new File("src/main/resources/jasper/sun_A4.jasper");
		DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
		JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.xpath.executer.factory",
				"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		if (compiledFile.exists()) {
			jasperReport = (JasperReport) JRLoader.loadObject(compiledFile);
		} else {
			jasperReport = JasperCompileManager.compileReport("src/main/resources/jasper/sun_A4.jrxml");
		}

		return jasperReport;
	}
	
	@Bean
	JRFileVirtualizer fileVirtualizer() {
		return new JRFileVirtualizer(jasperSwapFileMaxSize, jasperSwapFileDirectory);
	}
	
	@Bean
	JRSwapFileVirtualizer JRSwapFileVirtualizer() {
		// blockSize: how big is a block in byte
		//minGrowCount: if the swapfile is full, it will grow with file.length() + minGrowCount * blockSize
		JRSwapFile sf = new JRSwapFile(jasperSwapFileDirectory, jasperSwapFileBlockSize, jasperSwapFileMinGrowCount);
		// maxSize: is the maximum number of report pages that will be stored in primary memory (RAM) before sections of the report are stored in virtual memory (disk).
		return new JRSwapFileVirtualizer(50, sf, true);
	}
	
}
