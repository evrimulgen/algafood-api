package dev.drugowick.algaworks.algafoodapi.api.controller;

import dev.drugowick.algaworks.algafoodapi.api.controller.utils.ObjectMerger;
import dev.drugowick.algaworks.algafoodapi.domain.model.Cuisine;
import dev.drugowick.algaworks.algafoodapi.domain.repository.CuisineRepository;
import dev.drugowick.algaworks.algafoodapi.domain.service.CuisineCrudService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/cuisines")
public class CuisineController {

	/**
	 * I don't like it but, for the sake of simplicity for now, operations that do not require a
	 * transaction are using the repository (get, list) and operations that require a transaction
	 * are using the service layer (delete, save, update).
	 */

	private CuisineRepository cuisineRepository;
	private CuisineCrudService cuisinesCrudService;

	public CuisineController(CuisineRepository cuisineRepository, CuisineCrudService cuisinesCrudService) {
		this.cuisineRepository = cuisineRepository;
		this.cuisinesCrudService = cuisinesCrudService;
	}

	@GetMapping
	public List<Cuisine> list() {
		return cuisineRepository.findAll();
	}

	@PostMapping
	public ResponseEntity<Cuisine> save(@RequestBody Cuisine cuisine) {
		// Temporary. Client should not send an ID when posting. See #2.
		if (cuisine.getId() != null) {
			return ResponseEntity.badRequest()
					.build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(cuisinesCrudService.save(cuisine));
	}

	@GetMapping(value = {"/{id}"})
	public Cuisine get(@PathVariable Long id) {
		return cuisinesCrudService.findOrElseThrow(id);
	}

	@PutMapping(value = "/{id}")
	public Cuisine update(@PathVariable Long id, @RequestBody Cuisine cuisine) {
		Cuisine cuisineToUpdate = cuisinesCrudService.findOrElseThrow(id);

		BeanUtils.copyProperties(cuisine, cuisineToUpdate, "id");
		// The save method will update when an existing ID is being passed.
		return cuisinesCrudService.save(cuisineToUpdate);
	}

	@PatchMapping("/{id}")
	public Cuisine partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> cuisineMap) {
		Cuisine cuisineToUpdate = cuisinesCrudService.findOrElseThrow(id);

		ObjectMerger.mergeRequestBodyToGenericObject(cuisineMap, cuisineToUpdate, Cuisine.class);

		return update(id, cuisineToUpdate);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable Long id) {
		cuisinesCrudService.delete(id);
	}

	@GetMapping(value = "/by-name")
	public List<Cuisine> cuisinesByName(@RequestParam("name") String name) {
		return cuisineRepository.byNameLike(name);
	}

}
