package com.weatherandoutfit.controller;


import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/weather")
@Slf4j
@PropertySource("classpath:weather.properties")
@CrossOrigin(origins = "http://localhost:8080")
public class WeahterApiController {
	@Value("${openweathermap.api.key}")
	private String apiKey;
	private final static String URL  = "https://api.openweathermap.org/data/2.5/weather";
	private final static String FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast";
	

	@GetMapping(value = "/coordinate")
	public ResponseEntity<Object> getWeatherByCoordinates(@RequestParam double lat, @RequestParam double lon){
		log.info("{}, {}" , lat, lon);
		log.info("{}", apiKey);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
				.queryParam("lat", lat)
				.queryParam("lon", lon)
				.queryParam("lang", "ko")
				.queryParam("appid",	apiKey)
				.queryParam("units", "metric");
		
		
		
//		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,entity,String.class);
		log.info("Request URL : {}", builder.toUriString() );
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping(value ="/forecast/coordinate")
	public ResponseEntity<Object> getForecastByCoordinates(@RequestParam double lat, @RequestParam double lon){
	
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(FORECAST_URL)
				.queryParam("lat", lat)
				.queryParam("lon", lon)
				.queryParam("appid", apiKey)
				.queryParam("units", "metric")
				.queryParam("lang", "ko");
		
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping(value ="/city")
	public ResponseEntity<Object> getWeatherByCity(@RequestParam String city){
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
				.queryParam("q", city)
				.queryParam("appid", apiKey)
				.queryParam("units", "metric")
				.queryParam("lang", "ko");
		
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping(value ="/forecast/city")
	public ResponseEntity<Object> getForecastByCity(@RequestParam String city){
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(FORECAST_URL)
				.queryParam("q", city)
				.queryParam("appid", apiKey)
				.queryParam("units", "metric")
				.queryParam("lang", "ko");
		
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
		return ResponseEntity.ok(response.getBody());
	}
	
}
