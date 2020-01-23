package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.entity.DaycareEmployee;
import jedrekp.daycarecateringbillgenerator.repository.DaycareEmployeeRepository;
import jedrekp.daycarecateringbillgenerator.utility.DaycareRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.text.MessageFormat;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class DaycareEmployeeService {

    private final DaycareEmployeeRepository daycareEmployeeRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DaycareEmployee findSingleEmployeeByIdWithAllDetails(long daycareEmployeeId) {
        return daycareEmployeeRepository.findByIdWithAllDetails(daycareEmployeeId).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Daycare employee #{0} does not exist.", daycareEmployeeId)));
    }

    public Collection<DaycareEmployee> findAllDaycareEmployees() {
        return daycareEmployeeRepository.findAll();
    }

    public Collection<DaycareEmployee> findDaycareEmployeesWithSpecificRole(DaycareRole daycareRole) {
        return daycareEmployeeRepository.findAllByDaycareRoleOrderByLastNameAscFirstNameAsc(daycareRole);
    }

    public DaycareEmployee addNewGroupSupervisor(DaycareEmployee daycareEmployee) {
        if (daycareEmployee.getDaycareRole() != DaycareRole.GROUP_SUPERVISOR) {
            throw new IllegalArgumentException("You may only create new accounts for group supervisors.");
        }
        checkUsernameAvailability(daycareEmployee.getAppUsername());
        daycareEmployee.setPassword(passwordEncoder.encode(daycareEmployee.getPassword()));
        return daycareEmployeeRepository.save(daycareEmployee);
    }

    private void checkUsernameAvailability(String appUsername) {
        if (daycareEmployeeRepository.existsByAppUsernameIgnoreCase(appUsername)) {
            throw new EntityExistsException("This username is already taken.");
        }
    }

}
