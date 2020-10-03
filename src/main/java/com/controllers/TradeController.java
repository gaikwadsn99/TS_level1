package com.controllers;

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
		
		@RequestMapping(value="/detection", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
		public ResponseEntity<Object>  findFrontRunning(){
			
			List<Trade> trade = dao.orderByTime();
			DetectionAlgo d=new DetectionAlgo();
			ArrayList<ArrayList<Trade>> arr =d.DetectionAl(trade);
			trade.clear();
			
			FileWriter f = new FileWriter();
			f.CreateTable(arr);	
			return new ResponseEntity<Object>(arr, HttpStatus.OK);
		}
	

		
		/*@RequestMapping(value="/detectfrontrun/downloadpdf", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
		public ResponseEntity downloadFileFromLocal() {
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
