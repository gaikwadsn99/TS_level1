package com.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.detection.DetectionAlgo;
import com.export.FileWriter;
import com.generator.GenerateTradeList;
import com.pojo.Market;
import com.pojo.Trade;
import com.repository.MarketRepository;
import com.repository.TradeRepository;
import com.service.TradeService;
@RestController
@CrossOrigin
public class TradeController {
	 @Autowired

	 TradeRepository dao;

	 @Autowired
	 MarketRepository market;
	 
	private static final Logger logger = LoggerFactory.getLogger(TradeController.class);

	// @GetMapping("/trades")

	@RequestMapping(value="/trades/add" ,method =RequestMethod.POST , consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<Trade> saveTrade(@RequestBody Trade trade) {

		Trade added = dao.save(trade);

		logger.info("trade added :" + trade);
		// System.out.println(trade);
		ResponseEntity<Trade> response = new ResponseEntity<Trade>(added, HttpStatus.CREATED);

		return response;
	}

	@RequestMapping(value="/generate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<List<Trade>> generateTrades() {
		logger.info("Iside generate method");

		/*List<Trade> li = new ArrayList<>();
		GenerateTradeList g = new GenerateTradeList();
		logger.info("Iside generate method");

		li=g.Generate();*/
		//logger.debug(li.toString());
		GenerateTradeList g = new GenerateTradeList();
	    List[] a = g.Generate();
	    //dao.saveAll(a[0]);
	    //System.out.println(a[1].size());
		
/*		 for (int i = 0;i<300;i++) { 
			//  System.out.println(li.get(i).toString());
		 
			  //logger.info("trade added:" + a[].get(i).toString());
			 
		//	 Trade trade=new Trade();
		//	 trade= (Trade) a[0].get(i);
			  dao.save((Trade) a[0].get(i));
		    market.save((Market) a[1].get(i));
			  
		
		 
		 }
		
*/
	    
	    dao.saveAll(a[0]);
	    market.saveAll(a[1]);
		 System.out.println("Table generated");
		 List<Trade> trade = dao.findAll();

			if (!trade.isEmpty()) {
				ResponseEntity<List<Trade>> response = new ResponseEntity<List<Trade>>(trade, HttpStatus.OK);
				logger.info("trades found are :" + response);
				return response;
			} else {
				ResponseEntity<List<Trade>> response = new ResponseEntity<List<Trade>>(trade, HttpStatus.NOT_FOUND);
				logger.info("trades found are :" + response);
				return response;

			}
	}
	
		@RequestMapping(value="/trades", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
		public ResponseEntity<List<Trade>> findAllTrades() {

			List<Trade> trade = dao.orderByTime();
			if (!trade.isEmpty()) {
				ResponseEntity<List<Trade>> response = new ResponseEntity<List<Trade>>(trade, HttpStatus.OK);
				logger.info("trades found are :" + response);
				return response;
			} else {
				ResponseEntity<List<Trade>> response = new ResponseEntity<List<Trade>>(trade, HttpStatus.NOT_FOUND);
				logger.info("trades found are :" + response);
				return response;

			}

		}
		
		@RequestMapping(value="/detectionfront", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
		public ResponseEntity<Object>  findFrontRunning(){
			
			List<Trade> trade = dao.orderByTime();
			DetectionAlgo d=new DetectionAlgo();
			ArrayList<ArrayList<Trade>> arr =d.DetectionAl(trade);
			trade.clear();
			
			FileWriter f = new FileWriter();
           	f.CreateTable(arr,"./FrontRunningScenarios.pdf", "./FrontRunningScenarios.xlsx",false);	
			return new ResponseEntity<Object>(arr, HttpStatus.OK);
		}
		
		
		
		

			
		
	
		@GetMapping("/detectwash")
		public Void  findWashTrades(){
			
			
			List<Trade> trade = dao.findAll();
			DetectionAlgo det=new DetectionAlgo();
			String a = "./WashTradeScenarios";
			String b = "./WashTradeScenarios";
			
			ArrayList<ArrayList<Trade>>li = new ArrayList<ArrayList<Trade>>();
			for(int i =0;i<3;i++)
				li.add(new ArrayList<Trade>());
			for(int i = 0;i<300;i++)
			{
				if(trade.get(i).getCustomerId()==221)
				{switch(trade.get(i).getBrokerName()) {
					case "Irene Adler":	li.get(0).add(trade.get(i));
								break;
					case "Jack Stapleton" :	li.get(1).add(trade.get(i));
								break;
					case "James Moriarity":	li.get(2).add(trade.get(i));
								break;
				}}
			}
			ArrayList<ArrayList<Trade>> arr1;
			FileWriter f = new FileWriter();
			
			for(int i=0;i<3;i++)
			{

				arr1 = det.DetectWash(li.get(i)); 
				System.out.println(arr1.size());
				if(arr1.size()!=0) {
					System.out.println("In Wsh Trade detection");
					f.CreateTable(arr1, a+Integer.toString(i+1)+".pdf", b+Integer.toString(i+1)+".xlsx",true);}
				else
				{
					File myObj = new File(a+Integer.toString(i+1)+".pdf"); 
				    myObj.delete();
				    File myObj1 = new File(a+Integer.toString(i+1)+".xlsx"); 
				    myObj1.delete();
				}
				
			}
			
			return null;
		}
		
		@RequestMapping(value="/detectwash/{brokername}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
		public ResponseEntity<Object>  findWashTradeByBroker(@PathVariable String brokername){
			
			List<Trade> trade =  dao.findAll();
			ArrayList<ArrayList<Trade>> arr1;
			ArrayList<Trade> li =new ArrayList<Trade>();
			DetectionAlgo det=new DetectionAlgo();
			for(int i = 0;i<300;i++)
			{
				if(trade.get(i).getCustomerId()==221)
				{
					System.out.println("Brokername  :"+trade.get(i).getBrokerName());
					if(trade.get(i).getBrokerName().equalsIgnoreCase(brokername)){
						
						li.add(trade.get(i));
					}
				}
			}
			
			arr1= det.DetectWash(li);
			System.out.println("arr1 size  "+ arr1.size());
			System.out.println("li size in fwtb " + li.size());
			System.out.println("Brokername" + brokername);
					
			
			
			return new ResponseEntity<Object>(arr1, HttpStatus.OK);
		}
		
		
		
		
		
		
		
		
		
		
		
		/*@RequestMapping(value="/detectfrontrun/downloadpdf", method = RequestMethod.GET, produces = "application/pdf")
		public ResponseEntity<InputStreamResource> downloadFileFromLocal() {
			Path path = Paths.get("./FrontRunningScenarios.pdf");
			UrlResource resource = null;
			try {
				resource = new UrlResource(path.toUri());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok()				
					.body(resource);
		}*/
		
		
		
		
		

	/*	@RequestMapping(value="/detectfrontrun/downloadpdf", method = RequestMethod.GET, produces = "application/pdf")
		public ResponseEntity<InputStreamResource> downloadFileFromLocal_v1() throws IOException {
			
			 System.out.println("Calling pdf");
			  ClassPathResource pdfFile = new ClassPathResource("./FrontRunningScenarios.pdf" );
			  HttpHeaders headers = new HttpHeaders();
			  headers.setContentType(org.springframework.http.MediaType.parseMediaType("application/pdf"));
			  headers.add("Access-Control-Allow-Origin", "*");
			  headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
			  headers.add("Access-Control-Allow-Headers", "Content-Type");
			  headers.add("Content-Disposition", "filename=" + "FrontRunningScenario");
			  headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			  headers.add("Pragma", "no-cache");
			  headers.add("Expires", "0");
			 
			  headers.setContentLength(pdfFile.contentLength());
			  ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(
			    new InputStreamResource(pdfFile.getInputStream()), headers, HttpStatus.OK);
			  return response;
		}
		*/
		
		
		/*@GetMapping("/download/{fileName:.+}")
		public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) {
			Path path = Paths.get(fileBasePath + fileName);
			Resource resource = null;
			try {
				resource = new UrlResource(path.toUri());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		}*/
		
		
		
		/*@GetMapping("/download/{fileName:.+}")
		public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) {
			String fileBasePath="./";
			Path path = Paths.get(fileBasePath + fileName);
			Resource resource = null;
			try {
				resource = new UrlResource(path.toUri());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		}*/
	
		
		

}
