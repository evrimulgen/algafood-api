package dev.drugowick.algaworks.algafoodapi.jpa.test.permission;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import dev.drugowick.algaworks.algafoodapi.AlgafoodApiApplication;
import dev.drugowick.algaworks.algafoodapi.domain.model.Permission;
import dev.drugowick.algaworks.algafoodapi.domain.repository.PermissionRepository;

public class FindByIdPermissionMain {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
				
		PermissionRepository permissionRepository = applicationContext.getBean(PermissionRepository.class);
		
		Permission permission = permissionRepository.get(2L);
		
		
		System.out.printf("%s - %s\n", permission.getName(), permission.getDescription());
	}
}