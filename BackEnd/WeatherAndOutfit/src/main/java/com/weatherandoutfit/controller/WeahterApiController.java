package com.weatherandoutfit.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
				.queryParam("lat", lat)
				.queryParam("lon", lon)
				.queryParam("appid",	apiKey)
				.queryParam("units", "metric");
		
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping(value ="/forecast/coordinate")
	public ResponseEntity<Object> getForecastByCoordinates(@RequestParam double lat, @RequestParam double lon){
	
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(FORECAST_URL)
				.queryParam("lat", lat)
				.queryParam("lon", lon)
				.queryParam("appid", apiKey)
				.queryParam("units", "metric");
		
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping(value ="/city")
	public ResponseEntity<Object> getWeatherByCity(@RequestParam String city){
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
				.queryParam("q", city)
				.queryParam("appid", apiKey)
				.queryParam("units", "metric");
		
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping(value ="/forecast/city")
	public ResponseEntity<Object> getForecastByCity(@RequestParam String city){
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(FORECAST_URL)
				.queryParam("q", city)
				.queryParam("appid", apiKey)
				.queryParam("units", "metric");
		
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
		return ResponseEntity.ok(response.getBody());
	}
	
}
