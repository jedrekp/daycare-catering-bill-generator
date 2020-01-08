package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.repository.DaycareEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DaycareEmployeeService {

    private final DaycareEmployeeRepository daycareEmployeeRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


}
