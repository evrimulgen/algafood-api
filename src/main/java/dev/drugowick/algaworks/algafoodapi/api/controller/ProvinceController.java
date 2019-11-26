package dev.drugowick.algaworks.algafoodapi.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.drugowick.algaworks.algafoodapi.domain.model.Cuisine;
import dev.drugowick.algaworks.algafoodapi.domain.model.Province;
import dev.drugowick.algaworks.algafoodapi.domain.repository.ProvinceRepository;

@RestController
@RequestMapping(value = "/provinces")
public class ProvinceController {

	private ProvinceRepository provinceRepository;
	
	public ProvinceController(ProvinceRepository provinceRepository) {
		this.provinceRepository = provinceRepository;
	}

	@GetMapping
	public List<Province> list() {
		return provinceRepository.list();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Province save(@RequestBody Province province) {
		return provinceRepository.save(province);
	}
	
	@GetMapping(value = {"/{id}"})
	public ResponseEntity<Province> get(@PathVariable Long id) {
		Province province = provinceRepository.get(id);
		
		if (province != null) {
			return ResponseEntity.ok(province);
		}
		
		return ResponseEntity.notFound().build();
	}
	
}