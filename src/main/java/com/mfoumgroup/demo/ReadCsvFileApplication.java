package com.mfoumgroup.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@SpringBootApplication
public class ReadCsvFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadCsvFileApplication.class, args);
	}

	
}


@RestController
@RequestMapping("/csv")
class CsvFileController{
	
	@PostMapping("read")
	public ResponseEntity<List<ResponseModel>> readCsv(@RequestBody MultipartFile file) {
		
		if(file.isEmpty()) throw new RuntimeException("file must not be empty");
		String delimated = ",";
		List<ResponseModel> models = new ArrayList<>();
		try(Scanner scanner = new Scanner(file.getInputStream())) {
			int counter = 0;
			while(scanner.hasNext()) {
				String line = scanner.next();
				// au cas où le fichier Csv à une entête
				if(counter == 0) {
					counter++;
					continue;
				}
				String splitLine[] = line.split(delimated);
				if(splitLine.length == 0) {
					continue;
				}
				ResponseModel model = new ResponseModel();
				String key = splitLine[0];
				model.setKey(key);
				List<String> data = new ArrayList<>();
				for (int i = 1; i < splitLine.length; i++) {
					data.add(splitLine[i]);
				}
				model.setOthers(data);
				models.add(model);
			}
			return new ResponseEntity<List<ResponseModel>>(models, HttpStatus.OK);
		} catch (Exception e) {
			throw new RuntimeException("Error to read Csv => "+e.getMessage());
		}
		
	}
	
}

class ResponseModel{
	private String key;
	private List<String> others = new ArrayList<>();
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<String> getOthers() {
		return others;
	}
	public void setOthers(List<String> others) {
		this.others = others;
	}
	
	
}

